package org.bashaer.bashaerfoundationcharity.pattern.decorator;

import org.bashaer.bashaerfoundationcharity.model.Donation;

public class AnonymousDonorDecorator extends DonationDecorator {
    public AnonymousDonorDecorator(Donation decoratedDonation) {
        super(decoratedDonation);
    }

    @Override
    public String getDonorName() {
        return "Anonymous";
    }

    @Override
    public double getAmount() {
        return decoratedDonation.getAmount();
    }

    @Override
    public String getDonationType() {
        return decoratedDonation.getDonationType();
    }
}