package main.domain;

public class Customer {
    public static final byte LOYALTY_YEARS_THRESHOLD_FOR_LOYALTY_REBATE = 5;
    private static final byte LOYALTY_YEARS_THRESHOLD_FOR_CALCULATE_INSURANCE = 10;

    private String name;
    private int loyaltyYears;
    private boolean active;
    private boolean suspended;
    private double accountBalance;

    public Customer(String name, int loyaltyYears, boolean active, boolean suspended, double accountBalance) {
        this.name = name;
        this.loyaltyYears = loyaltyYears;
        this.active = active;
        this.suspended = suspended;
        this.accountBalance = accountBalance;
    }

    public String getName() { return name; }
    public int getLoyaltyYears() { return loyaltyYears; }
    public boolean isActive() { return active; }
    public boolean isSuspended() { return suspended; }
    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }
    public boolean isEligibleToLoyaltyRebate() {
        return loyaltyYears >= LOYALTY_YEARS_THRESHOLD_FOR_LOYALTY_REBATE && isActive() && !isSuspended();
    }
    public boolean isEligibleToInsuranceRebate() {
        return loyaltyYears >= LOYALTY_YEARS_THRESHOLD_FOR_CALCULATE_INSURANCE;
    }
}
