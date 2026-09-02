package main.domain;

public class Customer {
    private int id;
    private String name;
    private int loyaltyYears;
    private boolean active;
    private boolean suspended;
    private double accountBalance;

    public Customer(int id, String name, int loyaltyYears, boolean active, boolean suspended, double accountBalance) {
        this.id = id;
        this.name = name;
        this.loyaltyYears = loyaltyYears;
        this.active = active;
        this.suspended = suspended;
        this.accountBalance = accountBalance;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getLoyaltyYears() { return loyaltyYears; }
    public boolean isActive() { return active; }
    public boolean isSuspended() { return suspended; }
    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }
}
