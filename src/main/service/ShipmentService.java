package main.service;

import main.domain.Ship;
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
        if (!isValidShipment(shipment)) return null;
        double total = pricingService.calculatePrice(shipment);
        setShipment(total, shipment);
        repository.save(shipment);
        return getConfirmationMessage(total, shipment);
    }

    private boolean isValidShipment(Shipment shipment) {
        if (!shipment.getCustomer().isActive()) return false;
        if (shipment.getCustomer().isSuspended()) return false;
        if (shipment.getCargo().isEmpty()) return false;
        if (shipment.getTotalWeight() > shipment.getShip().getCapacity()) return false;
        if (shipment.hasHazardousCargo() && !permissionService.canCarryHazardous(shipment.getShip())) return false;
        return true;
    }

    private void setShipment(double total, Shipment shipment) {
        shipment.setTotal(total);
        shipment.setStatus("READY");
    }

    private String getConfirmationMessage(double total, Shipment shipment) {
        return setCategory(total) + " | " + shipment.getReference() + " | " + String.format("%.2f", total)
                + " | " + notificationService.confirmationFor(shipment);
    }

    private String setCategory(double total) {
        return pricingService.pricingSummary(total);
    }

}
