package org.bashaer.bashaerfoundationcharity.pattern.strategy;

import org.bashaer.bashaerfoundationcharity.model.Donation;
import org.bashaer.bashaerfoundationcharity.model.InKindDonation;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.DonationDecorator;

public class TaxExemptReceiptStrategy implements ReceiptStrategy {
    private static final String CHARITY_TAX_ID = "TAX-EXEMPT-501C3-BASHAER-99201";

    @Override
    public String generateReceipt(Donation donation) {
        // Unwrap decorators to extract raw base metadata (description, quantity)
        Donation base = donation;
        while (base instanceof DonationDecorator) {
            base = ((DonationDecorator) base).getDecoratedDonation();
        }

        String itemDesc = (base instanceof InKindDonation) ? ((InKindDonation) base).getItemDescription() : "N/A";
        int quantity = (base instanceof InKindDonation) ? ((InKindDonation) base).getQuantity() : 1;

        String receipt = String.format("""
                ====================================================
                     OFFICIAL TAX-EXEMPT RECEIPT (501(c)(3))
                           BASHAER FOUNDATION CHARITY
                ====================================================
                Tax Registration ID : %s
                Donor Name          : %s
                Donation Type       : %s
                Item Description    : %s
                Quantity            : %d
                Total Value/Amount  : $%.2f
                Receipt Format      : Official Tax-Exempt Certificate
                Payment Status      : COMPLETED
                Tax Deductible      : YES (100%% Claimable)
                Timestamp           : [Live DB Generated]
                ====================================================
                Note: Keep this certificate for annual tax filings.
                ====================================================
                """, 
                CHARITY_TAX_ID,
                donation.getDonorName(), 
                donation.getDonationType(), 
                itemDesc, 
                quantity, 
                donation.getAmount());

        // Print full receipt details with all DB fields to the console
        System.out.println(receipt);

        return receipt;
    }
}