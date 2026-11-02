package org.bashaer.bashaerfoundationcharity.model;

public class MonetaryDonation extends Donation {
    public MonetaryDonation(double amount, String donorName) {
        super(amount, donorName);
    }
    @Override
    public String getDonationType() { return "Monetary Funds"; }
}