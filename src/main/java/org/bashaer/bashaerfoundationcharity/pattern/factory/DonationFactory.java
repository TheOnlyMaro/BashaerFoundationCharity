package org.bashaer.bashaerfoundationcharity.pattern.factory;

import org.bashaer.bashaerfoundationcharity.model.Donation;
import org.bashaer.bashaerfoundationcharity.model.InKindDonation;
import org.bashaer.bashaerfoundationcharity.model.MonetaryDonation;

public class DonationFactory {
    public static Donation createDonation(String type, double amount, String donorName, String extraDetail, int quantity) {
        return switch (type.toLowerCase()) {
            case "monetary" -> new MonetaryDonation(amount, donorName);
            case "inkind" -> new InKindDonation(amount, donorName, extraDetail, quantity);
            default -> throw new IllegalArgumentException("Unknown donation type: " + type);
        };
    }
}