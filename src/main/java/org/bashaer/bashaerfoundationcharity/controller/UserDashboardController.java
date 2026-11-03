package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.bashaer.bashaerfoundationcharity.dao.DonationDao;
import org.bashaer.bashaerfoundationcharity.model.*;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.AnonymousDonorDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.LogisticsFeeDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.ProcessingFeeCoveredDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.factory.DonationFactory;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.ReceiptStrategy;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.StandardReceiptStrategy;
import org.bashaer.bashaerfoundationcharity.pattern.strategy.TaxExemptReceiptStrategy;

public class UserDashboardController {
    @FXML private TextField donorNameField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> donationTypeCombo;

    @FXML private VBox inKindBox;
    @FXML private TextField itemDescriptionField;
    @FXML private TextField quantityField;

    @FXML private CheckBox anonymousCheckBox;
    @FXML private CheckBox processingFeeCheckBox;
    @FXML private CheckBox logisticsFeeCheckBox;

    @FXML private ComboBox<String> receiptStrategyCombo;
    @FXML private Label statusLabel;
    @FXML private TextArea receiptArea;

    @FXML
    public void initialize() {
        donationTypeCombo.getItems().addAll("Monetary", "InKind");
        receiptStrategyCombo.getItems().addAll("Standard Receipt", "Tax-Exempt Official Receipt");
        receiptStrategyCombo.getSelectionModel().selectFirst();

        // Default state: Hide type-specific UI elements initially
        anonymousCheckBox.setVisible(false);
        anonymousCheckBox.setManaged(false);
        processingFeeCheckBox.setVisible(false);
        processingFeeCheckBox.setManaged(false);
        logisticsFeeCheckBox.setVisible(false);
        logisticsFeeCheckBox.setManaged(false);

        // Dynamic UI listener
        donationTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isMonetary = "Monetary".equals(newVal);
            boolean isInKind = "InKind".equals(newVal);
            boolean isSelected = newVal != null;

            // In-Kind box visibility
            inKindBox.setVisible(isInKind);
            inKindBox.setManaged(isInKind);

            // Remove/hide amount field when choosing In-Kind
            amountField.setVisible(isMonetary);
            amountField.setManaged(isMonetary);

            // Checkbox visibility based on selection
            anonymousCheckBox.setVisible(isSelected);
            anonymousCheckBox.setManaged(isSelected);

            processingFeeCheckBox.setVisible(isMonetary);
            processingFeeCheckBox.setManaged(isMonetary);

            logisticsFeeCheckBox.setVisible(isInKind);
            logisticsFeeCheckBox.setManaged(isInKind);
        });
    }

    @FXML
    protected void handleSubmitDonation() {
        try {
            String name = donorNameField.getText();
            String type = donationTypeCombo.getValue();

            if (name == null || name.trim().isEmpty() || type == null) {
                statusLabel.setText("Error: Please fill in all required fields.");
                return;
            }

            double baseAmount = 0.0;
            Donation donation;

            if ("InKind".equals(type)) {
                String desc = itemDescriptionField.getText();
                int qty = 1;
                try {
                    qty = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException ignored) {}

                donation = new InKindDonation(0.0, name, desc != null ? desc : "General Items", qty);
            } else {
                baseAmount = Double.parseDouble(amountField.getText());
                donation = DonationFactory.createDonation("monetary", baseAmount, name, null, 1);
            }

            // 2. DECORATOR PATTERN: Anonymous wrapper
            if (anonymousCheckBox.isSelected()) {
                donation = new AnonymousDonorDecorator(donation);
            }

            // 3. DECORATOR PATTERN: Fee modifiers
            if ("Monetary".equals(type) && processingFeeCheckBox.isSelected()) {
                donation = new ProcessingFeeCoveredDecorator(donation);
            } else if ("InKind".equals(type) && logisticsFeeCheckBox.isSelected()) {
                donation = new LogisticsFeeDecorator(donation);
            }

            // 4. STRATEGY PATTERN: Receipt Generation
            ReceiptStrategy strategy = "Tax-Exempt Official Receipt".equals(receiptStrategyCombo.getValue())
                    ? new TaxExemptReceiptStrategy()
                    : new StandardReceiptStrategy();

            String receiptOutput = strategy.generateReceipt(donation);

            // 5. DATABASE PERSISTENCE
            DonationDao.saveDonation(donation, baseAmount);

            statusLabel.setText("Success! Saved to database.");
            receiptArea.setText(receiptOutput);

        } catch (NumberFormatException e) {
            statusLabel.setText("Error: Monetary amount must be a valid number.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}