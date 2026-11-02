package org.bashaer.bashaerfoundationcharity.model;

public abstract class DonationDecorator extends Donation {
    protected Donation decoratedDonation;

    public DonationDecorator(Donation decoratedDonation) {
        super(decoratedDonation.getAmount(), decoratedDonation.getDonorName());
        this.decoratedDonation = decoratedDonation;
    }
}