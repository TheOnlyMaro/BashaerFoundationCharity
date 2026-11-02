package org.bashaer.bashaerfoundationcharity.model;

public interface ReceiptStrategy {
    String generateReceipt(Donation donation);
}