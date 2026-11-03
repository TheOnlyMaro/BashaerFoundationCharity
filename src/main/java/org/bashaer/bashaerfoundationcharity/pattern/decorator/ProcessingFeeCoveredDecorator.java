package org.bashaer.bashaerfoundationcharity.pattern.decorator;

import org.bashaer.bashaerfoundationcharity.model.Donation;

public class ProcessingFeeCoveredDecorator extends DonationDecorator {
    public ProcessingFeeCoveredDecorator(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public double getAmount() {
        // Adds a 3% processing fee coverage to the total donation amount
        return decoratedDonation.getAmount() * 1.03;
    }

    @Override
    public String getDonationType() {
        return decoratedDonation.getDonationType();
    }
}