package org.bashaer.bashaerfoundationcharity.model;

public class StandardReceiptStrategy implements ReceiptStrategy {
    @Override
    public String generateReceipt(Donation donation) {
        return "--- BASHAER FOUNDATION RECEIPT ---\n" +
                "Donor: " + donation.getDonorName() + "\n" +
                "Type: " + donation.getDonationType() + "\n" +
                "Amount: $" + donation.getAmount() + "\n" +
                "Status: Received with thanks!";
    }
}