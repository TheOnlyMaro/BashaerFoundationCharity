package org.bashaer.bashaerfoundationcharity.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bashaer.bashaerfoundationcharity.model.CategoryItem;
import org.bashaer.bashaerfoundationcharity.model.DatabaseConnection;

import java.sql.*;

public class LookupDao {

    public static ObservableList<CategoryItem> getAllCategories() {
        ObservableList<CategoryItem> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM donation_categories";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS donation_categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT UNIQUE NOT NULL, " +
                    "description TEXT, " +
                    "status TEXT DEFAULT 'ACTIVE', " +
                    "allocated_budget REAL DEFAULT 0.0, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

            // Seed permanent required categories
            stmt.execute("INSERT OR IGNORE INTO donation_categories (name, description, status) VALUES " +
                    "('Any/All', 'General unrestricted fund; acts as wildcard for reads and default destination', 'ACTIVE'), " +
                    "('Other', 'Default placeholder for unclassified events and general contributions', 'ACTIVE');");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CategoryItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void addCategory(String name, String description) {
        String sql = "INSERT INTO donation_categories (name, description, status) VALUES (?, ?, 'ACTIVE')";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCategory(int id) {
        String sql = "DELETE FROM donation_categories WHERE id = ? AND name NOT IN ('Any/All', 'Other')";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}