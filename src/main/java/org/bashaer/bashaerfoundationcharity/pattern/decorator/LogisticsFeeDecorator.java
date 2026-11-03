package org.bashaer.bashaerfoundationcharity.pattern.decorator;

import org.bashaer.bashaerfoundationcharity.model.Donation;

public class LogisticsFeeDecorator extends DonationDecorator {
    private static final double LOGISTICS_FEE = 15.0; // Flat handling fee

    public LogisticsFeeDecorator(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public double getAmount() {
        return decoratedDonation.getAmount() + LOGISTICS_FEE;
    }

    @Override
    public String getDonationType() {
        return decoratedDonation.getDonationType();
    }
}