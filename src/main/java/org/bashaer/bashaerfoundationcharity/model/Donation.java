package org.bashaer.bashaerfoundationcharity.model;

public abstract class Donation {
    protected double amount;
    protected String donorName;

    public Donation(double amount, String donorName) {
        this.amount = amount;
        this.donorName = donorName;
    }

    public abstract String getDonationType();
    public double getAmount() { return amount; }
    public String getDonorName() { return donorName; }
}