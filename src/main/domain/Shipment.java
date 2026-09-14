package main.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Shipment {
    private final String reference;
    private final Customer customer;
    private final Planet origin;
    private final Planet destination;
    private final Ship ship;
    private final LocalDate departureDate;
    private final List<Cargo> cargo = new ArrayList<>();
    private double totalDeclaredValue;
    private String status = "CREATED";

    public Shipment(String reference, Customer customer, Planet origin, Planet destination, Ship ship, LocalDate departureDate) {
        this.reference = reference;
        this.customer = customer;
        this.origin = origin;
        this.destination = destination;
        this.ship = ship;
        this.departureDate = departureDate;
    }

    public void addCargo(Cargo item) { cargo.add(item); }
    public String getReference() { return reference; }
    public Customer getCustomer() { return customer; }
    public Planet getOrigin() { return origin; }
    public Planet getDestination() { return destination; }
    public Ship getShip() { return ship; }
    public LocalDate getDepartureDate() { return departureDate; }
    public List<Cargo> getCargo() { return cargo; }
    public void setTotalDeclaredValue(double totalValue) { this.totalDeclaredValue = totalValue; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalWeight() {
        double totalWeight = 0;
        for (Cargo item : cargo) {
            totalWeight += item.getWeight();
        }
        return totalWeight;
    }

    public double getTotalDeclaredValue() {
        double totalValue = 0;
        for (Cargo item : cargo) {
            totalValue += item.getDeclaredValue();
        }
        return totalValue;
    }

    public boolean hasHazardousCargo() {
        for (Cargo item : cargo) {
            if (item.isHazardous()) return true;
        }
        return false;
    }
}
