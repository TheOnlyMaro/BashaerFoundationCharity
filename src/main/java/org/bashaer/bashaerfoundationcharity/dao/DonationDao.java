package org.bashaer.bashaerfoundationcharity.dao;

import org.bashaer.bashaerfoundationcharity.model.DatabaseConnection;
import org.bashaer.bashaerfoundationcharity.model.Donation;
import org.bashaer.bashaerfoundationcharity.model.InKindDonation;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.AnonymousDonorDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.DonationDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.LogisticsFeeDecorator;
import org.bashaer.bashaerfoundationcharity.pattern.decorator.ProcessingFeeCoveredDecorator;

import java.sql.*;

public class DonationDao {

    public static void saveDonation(Donation donation, double baseAmount) {
        String insertDonationSQL = "INSERT INTO donations (donor_name, donation_type, base_amount, amount, item_description, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // 1. Initialize Schema
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS donations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "donor_name TEXT NOT NULL, " +
                    "donation_type TEXT NOT NULL, " +
                    "base_amount REAL NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "item_description TEXT, " +
                    "quantity INTEGER DEFAULT 1, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

            stmt.execute("CREATE TABLE IF NOT EXISTS modifiers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "modifier_code TEXT UNIQUE NOT NULL, " +
                    "description TEXT, " +
                    "multiply_amount REAL DEFAULT 1.0, " +
                    "add_amount REAL DEFAULT 0.0);");

            stmt.execute("CREATE TABLE IF NOT EXISTS donation_modifiers (" +
                    "donation_id INTEGER, " +
                    "modifier_id INTEGER, " +
                    "PRIMARY KEY (donation_id, modifier_id), " +
                    "FOREIGN KEY (donation_id) REFERENCES donations(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (modifier_id) REFERENCES modifiers(id));");

            stmt.execute("INSERT OR IGNORE INTO modifiers (modifier_code, description, multiply_amount, add_amount) VALUES " +
                    "('PROCESSING_FEE', '3% Processing Fee', 1.03, 0.0), " +
                    "('LOGISTICS_FEE', 'Flat Logistics Fee', 1.0, 15.0), " +
                    "('ANONYMOUS', 'Anonymous Donor Flag', 1.0, 0.0);");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. UNWRAP DECORATORS to find base InKindDonation metadata
        Donation baseDonation = donation;
        while (baseDonation instanceof DonationDecorator) {
            baseDonation = ((DonationDecorator) baseDonation).getDecoratedDonation();
        }

        int donationId = -1;
        String itemDesc = null;
        int quantity = 1;

        if (baseDonation instanceof InKindDonation) {
            InKindDonation inKind = (InKindDonation) baseDonation;
            itemDesc = inKind.getItemDescription();
            quantity = inKind.getQuantity();
        }

        // 3. Insert Core Donation Record (donation_type is strictly "Monetary" or "InKind")
        try (PreparedStatement pstmt = conn.prepareStatement(insertDonationSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, donation.getDonorName());
            pstmt.setString(2, donation.getDonationType()); // Pure type
            pstmt.setDouble(3, baseAmount);
            pstmt.setDouble(4, donation.getAmount());
            pstmt.setString(5, itemDesc);
            pstmt.setInt(6, quantity);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    donationId = rs.getInt(1);
                }
            }
            System.out.println("Donation saved successfully with ID: " + donationId);
        } catch (SQLException e) {
            System.err.println("Failed to save donation: " + e.getMessage());
            return;
        }

        // 4. Link Applied Decorators in Junction Table
        if (donationId != -1) {
            checkAndLinkModifier(conn, donationId, donation, ProcessingFeeCoveredDecorator.class, "PROCESSING_FEE");
            checkAndLinkModifier(conn, donationId, donation, LogisticsFeeDecorator.class, "LOGISTICS_FEE");
            checkAndLinkModifier(conn, donationId, donation, AnonymousDonorDecorator.class, "ANONYMOUS");
        }
    }

    private static void checkAndLinkModifier(Connection conn, int donationId, Donation donation, Class<?> decoratorClass, String code) {
        boolean isApplied = false;
        Donation curr = donation;
        while (curr instanceof DonationDecorator) {
            if (decoratorClass.isInstance(curr)) {
                isApplied = true;
                break;
            }
            curr = ((DonationDecorator) curr).getDecoratedDonation();
        }

        if (isApplied) {
            String sql = "INSERT OR IGNORE INTO donation_modifiers (donation_id, modifier_id) " +
                    "SELECT ?, id FROM modifiers WHERE modifier_code = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, donationId);
                pstmt.setString(2, code);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readDonations() {
        String sql = "SELECT d.*, GROUP_CONCAT(m.modifier_code) as modifiers " +
                "FROM donations d " +
                "LEFT JOIN donation_modifiers dm ON d.id = dm.donation_id " +
                "LEFT JOIN modifiers m ON dm.modifier_id = m.id " +
                "GROUP BY d.id";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Donor: " + rs.getString("donor_name") +
                        ", Type: " + rs.getString("donation_type") +
                        ", Qty: " + rs.getInt("quantity") +
                        ", Desc: " + (rs.getString("item_description") != null ? rs.getString("item_description") : "N/A") +
                        ", Final: $" + rs.getDouble("amount") +
                        ", Modifiers: " + (rs.getString("modifiers") != null ? rs.getString("modifiers") : "None"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to read donations: " + e.getMessage());
        }
    }
}