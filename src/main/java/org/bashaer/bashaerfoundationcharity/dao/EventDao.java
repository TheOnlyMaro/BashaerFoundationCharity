package org.bashaer.bashaerfoundationcharity.dao;

import org.bashaer.bashaerfoundationcharity.model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDao {

    public static void initializeEventTable() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "status TEXT DEFAULT 'Upcoming', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createEvent(String title, String category, String status) {
        initializeEventTable();
        String sql = "INSERT INTO events (title, category, status) VALUES (?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, category);
            pstmt.setString(3, status);
            pstmt.executeUpdate();
            System.out.println("Event created successfully: " + title);
        } catch (SQLException e) {
            System.err.println("Failed to create event: " + e.getMessage());
        }
    }

    public static void updateEventStatus(int eventId, String newStatus) {
        String sql = "UPDATE events SET status = ? WHERE id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, eventId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<EventRecord> getAllEvents() {
        initializeEventTable();
        List<EventRecord> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new EventRecord(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static class EventRecord {
        private int id;
        private String title;
        private String category;
        private String status;

        public EventRecord(int id, String title, String category, String status) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.status = status;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public String getStatus() { return status; }
    }
}