package org.bashaer.bashaerfoundationcharity.model;

public class InKindDonation extends Donation {
    private String itemDescription;
    public InKindDonation(double amount, String donorName, String itemDescription) {
        super(amount, donorName);
        this.itemDescription = itemDescription;
    }
    @Override
    public String getDonationType() { return "In-Kind Goods (" + itemDescription + ")"; }
}