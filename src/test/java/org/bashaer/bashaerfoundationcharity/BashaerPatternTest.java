package org.bashaer.bashaerfoundationcharity;

import org.bashaer.bashaerfoundationcharity.model.*;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.ProcessingFeeCoveredDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.factory.DonationFactory;
import org.bashaer.bashaerfoundationcharity.pattern.observer.EventSubject;
import org.bashaer.bashaerfoundationcharity.pattern.observer.VolunteerObserver;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.ReceiptStrategy;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.StandardReceiptStrategy;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.TaxExemptReceiptStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BashaerPatternTest {

    @Test
    void testDonationFactory() {
        Donation monetary = DonationFactory.createDonation("monetary", 500.0, "Omar", null, 0);
        assertNotNull(monetary);
        assertEquals("Monetary Funds", monetary.getDonationType());
        assertEquals(500.0, monetary.getAmount());

        Donation inKind = DonationFactory.createDonation("inkind", 0.0, "Fatma", "Winter Blankets", 10);
        assertNotNull(inKind);
        assertTrue(inKind.getDonationType().contains("Winter Blankets"));
    }

    @Test
    void testDonationDecorator() {
        Donation baseDonation = new MonetaryDonation(100.0, "Khaled");
        Donation decorated = new ProcessingFeeCoveredDecorator(baseDonation);

        // 3% fee added: 100 * 1.03 = 103.0
        assertEquals(103.0, decorated.getAmount(), 0.001);
        assertTrue(decorated.getDonationType().contains("Fee Covered"));
    }

    @Test
    void testReceiptStrategy() {
        Donation donation = new MonetaryDonation(250.0, "Youssef");

        ReceiptStrategy standard = new StandardReceiptStrategy();
        String standardOutput = standard.generateReceipt(donation);
        assertTrue(standardOutput.contains("BASHAER FOUNDATION"));

        ReceiptStrategy taxExempt = new TaxExemptReceiptStrategy();
        String taxOutput = taxExempt.generateReceipt(donation);
        assertTrue(taxOutput.contains("TAX-EXEMPT"));
    }

    @Test
    void testObserverPattern() {
        EventSubject event = new EventSubject("Medical Relief Drive");
        VolunteerObserver volunteer = new VolunteerObserver("Mariam");

        // Attach using java.util.Observer built-in method
        event.addObserver(volunteer);

        // Changing status should trigger notification without throwing errors
        assertDoesNotThrow(() -> event.setStatus("Completed"));
    }
}