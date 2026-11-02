package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bashaer.bashaerfoundationcharity.model.*;

public class UserDashboardController {
    @FXML private TextField donorNameField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> donationTypeCombo;
    @FXML private Label statusLabel;
    @FXML private ImageView logoView;

    @FXML
    public void initialize() {
        // Populate combo box
        donationTypeCombo.getItems().addAll("Monetary", "InKind");

        // Load logo if placed in resources/org/bashaer/bashaerfoundationcharity/images/logo.png
        try {
            Image logo = new Image(getClass().getResourceAsStream("../images/logo.png"));
            if (logo != null) logoView.setImage(logo);
        } catch (Exception e) {
            System.out.println("Logo load skipped.");
        }
    }

    @FXML
    protected void handleSubmitDonation() {
        try {
            String name = donorNameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String type = donationTypeCombo.getValue();

            // Using the Factory Pattern!
            Donation donation = DonationFactory.createDonation(type, amount, name, "General Aid");

            // Using the Decorator Pattern (adding fee coverage)
            Donation decorated = new ProcessingFeeCoveredDecorator(donation);

            // Using the Strategy Pattern (generating receipt)
            ReceiptStrategy strategy = new TaxExemptReceiptStrategy();
            String receipt = strategy.generateReceipt(decorated);

            statusLabel.setText("Success! Final Amount: $" + decorated.getAmount());
            System.out.println(receipt);

        } catch (Exception e) {
            statusLabel.setText("Error: Please check your inputs.");
        }
    }
}