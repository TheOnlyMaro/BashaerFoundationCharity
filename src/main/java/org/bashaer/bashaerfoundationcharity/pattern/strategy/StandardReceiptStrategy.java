package org.bashaer.bashaerfoundationcharity.pattern.strategy;

import org.bashaer.bashaerfoundationcharity.model.Donation;
import org.bashaer.bashaerfoundationcharity.model.InKindDonation;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.DonationDecorator;

public class StandardReceiptStrategy implements ReceiptStrategy {
    @Override
    public String generateReceipt(Donation donation) {
        Donation base = donation;
        while (base instanceof DonationDecorator) {
            base = ((DonationDecorator) base).getDecoratedDonation();
        }

        String desc = (base instanceof InKindDonation) ? ((InKindDonation) base).getItemDescription() : "N/A";
        int qty = (base instanceof InKindDonation) ? ((InKindDonation) base).getQuantity() : 1;

        String receipt = String.format("""
                ========================================
                       BASHAER CHARITY - STANDARD RECEIPT
                ========================================
                Donor Name      : %s
                Donation Type   : %s
                Item Description: %s
                Quantity        : %d
                Final Amount    : $%.2f
                Receipt Format  : Standard
                Payment Status  : COMPLETED
                Timestamp       : [Live DB Generated]
                ========================================
                """, donation.getDonorName(), donation.getDonationType(), desc, qty, donation.getAmount());

        System.out.println(receipt); // Printed to console as requested
        return receipt;
    }
}