package main.service;

import main.domain.Shipment;

public class ShipmentService {
    private static final String ERROR_CUSTOMER = "ERROR_CUSTOMER";
    private static final String ERROR_EMPTY = "ERROR_EMPTY";
    private static final String ERROR_CAPACITY = "ERROR_CAPACITY";
    private static final String ERROR_PERMISSION = "ERROR_PERMISSION";

    private final PricingService pricingService;
    private final PermissionService permissionService;
    private final ManifestRepository repository;
    private final NotificationService notificationService;

    public ShipmentService(PricingService pricingService, PermissionService permissionService,
                           ManifestRepository repository, NotificationService notificationService) {
        this.pricingService = pricingService;
        this.permissionService = permissionService;
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public String applyShipment(Shipment shipment) {
        validateShipment(shipment);
        double total = pricingService.calculatePrice(shipment);
        setShipment(total, shipment);
        repository.save(shipment);
        return setCategory(total, shipment);
    }

    private String validateShipment(Shipment shipment) {
        if (!shipment.getCustomer().isActive()) return ERROR_CUSTOMER;
        if (shipment.getCustomer().isSuspended()) return ERROR_CUSTOMER;
        if (shipment.getCargo().isEmpty()) return ERROR_EMPTY;
        if (shipment.getTotalWeight() > shipment.getShip().getCapacity()) return ERROR_CAPACITY;
        if (shipment.hasHazardousCargo() && !permissionService.canCarryHazardous(shipment.getShip()))
        return ERROR_PERMISSION;
    }

    private void setShipment(double total, Shipment shipment) {
        shipment.setTotal(total);
        shipment.setStatus("READY");
    }

    private String setCategory(double total, Shipment shipment) {
        String category = pricingService.pricingSummary(total);
        return category + " | " + shipment.getReference() + " | " + String.format("%.2f", total)
                + " | " + notificationService.confirmationFor(shipment);
    }

}
