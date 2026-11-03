package org.bashaer.bashaerfoundationcharity.model;

public class InKindDonation extends Donation {
    private String itemDescription;
    private int quantity;

    public InKindDonation(double amount, String donorName, String itemDescription, int quantity) {
        super(0 // placeholder until a manager can approve the donation and estimate the value
                , donorName);
        this.itemDescription = itemDescription;
        this.quantity = quantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }
    public int getQuantity() {
        return quantity;
    }
    @Override
    public String getDonationType() { return "In-Kind Goods"; }
}