package org.bashaer.bashaerfoundationcharity.model;

public class DonationFactory {
    public static Donation createDonation(String type, double amount, String donorName, String extraDetail) {
        return switch (type.toLowerCase()) {
            case "monetary" -> new MonetaryDonation(amount, donorName);
            case "inkind" -> new InKindDonation(amount, donorName, extraDetail);
            default -> throw new IllegalArgumentException("Unknown donation type: " + type);
        };
    }
}