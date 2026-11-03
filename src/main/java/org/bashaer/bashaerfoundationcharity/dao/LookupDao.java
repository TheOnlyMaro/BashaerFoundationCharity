package org.bashaer.bashaerfoundationcharity.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bashaer.bashaerfoundationcharity.model.CategoryItem;
import org.bashaer.bashaerfoundationcharity.model.DatabaseConnection;

import java.sql.*;

public class LookupDao {

    // Read all categories
    public static ObservableList<CategoryItem> getAllCategories() {
        ObservableList<CategoryItem> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM donation_categories";

        // Fetch shared singleton connection (DO NOT close 'conn')
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // Wrap ONLY Statement and ResultSet
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

    // Create a new category
    public static void addCategory(String name, String description) {
        String sql = "INSERT INTO donation_categories (name, description) VALUES (?, ?)";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // Wrap ONLY PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a category by ID
    public static void deleteCategory(int id) {
        String sql = "DELETE FROM donation_categories WHERE id = ?";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();

        // Wrap ONLY PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}