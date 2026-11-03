package org.bashaer.bashaerfoundationcharity.pattern.decorator;

import org.bashaer.bashaerfoundationcharity.model.Donation;

public abstract class DonationDecorator extends Donation {
    protected Donation decoratedDonation;

    public DonationDecorator(Donation decoratedDonation) {
        super(decoratedDonation.getAmount(), decoratedDonation.getDonorName());
        this.decoratedDonation = decoratedDonation;
    }

    public Donation getDecoratedDonation() {
        return decoratedDonation;
    }
}