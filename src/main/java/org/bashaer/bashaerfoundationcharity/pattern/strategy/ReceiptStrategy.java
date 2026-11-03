package org.bashaer.bashaerfoundationcharity.pattern.strategy;

import org.bashaer.bashaerfoundationcharity.model.Donation;

public interface ReceiptStrategy {
    String generateReceipt(Donation donation);
}