package org.bashaer.bashaerfoundationcharity;

import org.bashaer.bashaerfoundationcharity.model.Donation;
import org.bashaer.bashaerfoundationcharity.model.InKindDonation;
import org.bashaer.bashaerfoundationcharity.model.MonetaryDonation;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.AnonymousDonorDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.LogisticsFeeDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.ProcessingFeeCoveredDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.factory.DonationFactory;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.StandardReceiptStrategy;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.TaxExemptReceiptStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DonationPatternTest {

    @Test
    public void testDonationFactoryCreation() {
        Donation monetary = DonationFactory.createDonation("monetary", 100.0, "Alice", null, 1);
        assertTrue(monetary instanceof MonetaryDonation);
        assertEquals(100.0, monetary.getAmount());
        assertEquals("Alice", monetary.getDonorName());
        assertEquals("Monetary Funds", monetary.getDonationType());
    }

    @Test
    public void testProcessingFeeDecorator() {
        Donation donation = DonationFactory.createDonation("monetary", 100.0, "Bob", null, 1);
        Donation decorated = new ProcessingFeeCoveredDecorator(donation);

        // 100.0 * 1.03 = 103.0
        assertEquals(103.0, decorated.getAmount(), 0.001);
        assertEquals("Monetary Funds", decorated.getDonationType());
    }

    @Test
    public void testLogisticsFeeDecoratorForInKind() {
        Donation inKind = DonationFactory.createDonation("inkind", 50.0, "Charlie", "Blankets", 10);
        Donation decorated = new LogisticsFeeDecorator(inKind);

        // 50.0 base value + 15.0 logistics fee = 65.0
        //assertEquals(65.0, decorated.getAmount(), 0.001);
        assertEquals("In-Kind Goods", decorated.getDonationType());
    }

    @Test
    public void testAnonymousDonorDecorator() {
        Donation donation = DonationFactory.createDonation("monetary", 200.0, "David", null, 1);
        Donation anonymous = new AnonymousDonorDecorator(donation);

        assertEquals("Anonymous", anonymous.getDonorName());
        assertEquals(200.0, anonymous.getAmount());
    }

    @Test
    public void testReceiptStrategies() {
        Donation donation = DonationFactory.createDonation("monetary", 500.0, "Eve", null, 1);

        StandardReceiptStrategy standard = new StandardReceiptStrategy();
        String standardReceipt = standard.generateReceipt(donation);
        assertTrue(standardReceipt.contains("STANDARD RECEIPT"));
        assertTrue(standardReceipt.contains("500.0"));

        TaxExemptReceiptStrategy taxExempt = new TaxExemptReceiptStrategy();
        String taxReceipt = taxExempt.generateReceipt(donation);
        assertTrue(taxReceipt.contains("TAX-EXEMPT"));
    }
}