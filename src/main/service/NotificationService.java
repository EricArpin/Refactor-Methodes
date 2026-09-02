package main.service;

import main.domain.Shipment;

public class NotificationService {
    public String confirmationFor(Shipment shipment) {
        return "CONFIRMATION " + shipment.getReference() + " -> " + shipment.getCustomer().getName();
    }
}
