package main.service;

import java.util.Set;

import main.domain.Shipment;

public class PricingService {
    private static final double HAZARDOUS_FACTOR = 0.2;
    private static final double WEIGHT_FACTOR = 2.25;
    private static final double DECLARED_VALUE_FACTOR = 0.015;
    private static final double ACTIVE_CUSTOMER_REBATE_FACTOR = 0.9;
    private static final double INSURANCE_DECLARED_VALUE_FACTOR = 0.02;
    private static final byte HIGH_SECURITY_SURCHARGE = 125;
    private static final byte INTER_SECTOR_SURCHARGE = 80;
    private static final byte SEASON_SURCHARGE = 45;
    private static final byte INSURANCE_DECLARED_VALUE_EXTRA = 75;
    private static final byte INSURANCE_DECLARED_VALUE_DISCOUNT = 10;
    private static final Set<Integer> SEASON_SURCHARGEABLE_MONTHS = Set.of(1, 2, 12);

    private static final short DECLARED_VALUE_SURCHARGE_TRESHOLD = 10000;
    private static final short PRIORITY_THRESHOLD = 2000;
    private static final byte ORIGIN_SECURITY_LEVEL_TRESHOLD = 4;
    private static final byte DESTINATION_SECURITY_LEVEL_TRESHOLD = 4;

    private static final byte MINIMUM_DECLARED_VALUE_THRESHOLD = 0;

    public double calculatePrice(Shipment shipment) {
        double result = calculateWeightCharge(shipment);
        result = calculateDeclaredValueSurcharge(result, shipment);
        result = calculateHazardousSurcharge(result, shipment);
        result = calculateSecuritySurcharge(result, shipment);
        result = calculateInterSectorSurcharge(result, shipment);
        result = calculateSeasonSurcharge(result, shipment);
        result = calculateLoyaltyRebate(result, shipment);
        result = calculateInsurance(result, shipment);
        return result;
    }

    private double calculateWeightCharge(Shipment shipment) {
        return shipment.getTotalWeight() * WEIGHT_FACTOR;
    }

    private double calculateDeclaredValueSurcharge( double result, Shipment shipment) {
       return shipment.getTotalDeclaredValue() > DECLARED_VALUE_SURCHARGE_TRESHOLD
                ? result + shipment.getTotalDeclaredValue() * DECLARED_VALUE_FACTOR : result;
    }

    private double calculateHazardousSurcharge(double result, Shipment shipment) {
        return shipment.hasHazardousCargo() ? result + result * HAZARDOUS_FACTOR : result;
    }

    private double calculateSecuritySurcharge(double result, Shipment shipment) {
        return shipment.getOrigin().getSecurityLevel() >= ORIGIN_SECURITY_LEVEL_TRESHOLD
                || shipment.getDestination().getSecurityLevel() >= DESTINATION_SECURITY_LEVEL_TRESHOLD
                ? result + HIGH_SECURITY_SURCHARGE : result;
    }

    private double calculateInterSectorSurcharge(double result, Shipment shipment) {
        return shipment.getOrigin().getSector().equals(shipment.getDestination().getSector())
                ? result + INTER_SECTOR_SURCHARGE : result;
    }

    private double calculateSeasonSurcharge(double result, Shipment shipment) {
        return SEASON_SURCHARGEABLE_MONTHS.contains(shipment.getDepartureDate().getMonthValue())
        ? result + SEASON_SURCHARGE : result;
    }

    private double calculateLoyaltyRebate(double result, Shipment shipment) {
        return shipment.getCustomer().isEligibleToLoyaltyRebate()
                ? result * ACTIVE_CUSTOMER_REBATE_FACTOR : result;
    }

    private double calculateInsurance(double result, Shipment shipment) {
        double insuranceCost = shipment.getTotalDeclaredValue() * INSURANCE_DECLARED_VALUE_FACTOR;
        if (shipment.hasHazardousCargo()) insuranceCost += INSURANCE_DECLARED_VALUE_EXTRA;
        if (shipment.getCustomer().isEligibleToInsuranceRebate()) insuranceCost -= INSURANCE_DECLARED_VALUE_DISCOUNT;
        return result + Math.max(insuranceCost, MINIMUM_DECLARED_VALUE_THRESHOLD);
    }

    public String pricingSummary(double total) {
        return total >= PRIORITY_THRESHOLD ? "PRIORITY" : "REGULAR";
    }
}
