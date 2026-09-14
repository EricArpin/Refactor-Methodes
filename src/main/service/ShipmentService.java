package main.service;

import main.domain.Shipment;

import javax.print.DocFlavor;

public class ShipmentService {

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

    private void validateShipment(Shipment shipment) {
        if (!shipment.getCustomer().isActive()) return;
        if (shipment.getCustomer().isSuspended()) return;
        if (shipment.getCargo().isEmpty()) return;
        if (shipment.getTotalWeight() > shipment.getShip().getCapacity()) return;
        if (shipment.hasHazardousCargo() && !permissionService.canCarryHazardous(shipment.getShip()))
        return;
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
