package org.bashaer.bashaerfoundationcharity.pattern.strategy;

import org.bashaer.bashaerfoundationcharity.model.Donation;

public class TaxExemptReceiptStrategy implements ReceiptStrategy {
    @Override
    public String generateReceipt(Donation donation) {
        return "=== TAX-EXEMPT OFFICIAL RECEIPT ===\n" +
                "Bashaer Foundation Reg No: #9942-BF\n" +
                "Donor: " + donation.getDonorName() + "\n" +
                "Eligible Amount: $" + donation.getAmount() + "\n" +
                "Tax Deduction Authorized under NGO Law.";
    }
}