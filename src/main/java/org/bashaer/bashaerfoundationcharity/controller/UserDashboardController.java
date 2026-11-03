package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.bashaer.bashaerfoundationcharity.dao.DonationDao;
import org.bashaer.bashaerfoundationcharity.dao.LookupDao;
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
    @FXML private ComboBox<String> categoryCombo;

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

        // Load open categories from the DB Lookup DAO
        loadCategories();

        // Default state: Hide type-specific and value fields initially
        amountField.setVisible(false);
        amountField.setManaged(false);
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

            inKindBox.setVisible(isInKind);
            inKindBox.setManaged(isInKind);

            // Show amount field ONLY when Monetary is chosen
            amountField.setVisible(isMonetary);
            amountField.setManaged(isMonetary);

            anonymousCheckBox.setVisible(isSelected);
            anonymousCheckBox.setManaged(isSelected);

            processingFeeCheckBox.setVisible(isMonetary);
            processingFeeCheckBox.setManaged(isMonetary);

            logisticsFeeCheckBox.setVisible(isInKind);
            logisticsFeeCheckBox.setManaged(isInKind);
        });
    }

    private void loadCategories() {
        categoryCombo.getItems().clear();
        for (CategoryItem item : LookupDao.getAllCategories()) {
            categoryCombo.getItems().add(item.getName());
        }
    }

    @FXML
    protected void handleCategoryClick(){
        loadCategories();
    }

    @FXML
    protected void handleSubmitDonation() {
        try {
            String name = donorNameField.getText();
            String type = donationTypeCombo.getValue();
            String category = categoryCombo.getValue();

              if (!anonymousCheckBox.isSelected() && (name == null || name.trim().isEmpty())){
                statusLabel.setText("Error: Please fill in all required fields (including Category).");
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

                donation = DonationFactory.createDonation("inkind", 0.0, name, desc != null ? desc : "General Items", qty);
            } else {
                baseAmount = Double.parseDouble(amountField.getText());
                donation = DonationFactory.createDonation("monetary", baseAmount, name, null, 1);
            }

            // Decorators
            if (anonymousCheckBox.isSelected()) {
                donation = new AnonymousDonorDecorator(donation);
            }
            if ("Monetary".equals(type) && processingFeeCheckBox.isSelected()) {
                donation = new ProcessingFeeCoveredDecorator(donation);
            } else if ("InKind".equals(type) && logisticsFeeCheckBox.isSelected()) {
                donation = new LogisticsFeeDecorator(donation);
            }

            ReceiptStrategy strategy = "Tax-Exempt Official Receipt".equals(receiptStrategyCombo.getValue())
                    ? new TaxExemptReceiptStrategy()
                    : new StandardReceiptStrategy();

            String receiptOutput = strategy.generateReceipt(donation);

            // Database Persistence with Category
            DonationDao.saveDonation(donation, baseAmount, category);

            statusLabel.setText("Success! Saved to database under category: " + category);
            receiptArea.setText(receiptOutput);

        } catch (NumberFormatException e) {
            statusLabel.setText("Error: Monetary amount must be a valid number.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}