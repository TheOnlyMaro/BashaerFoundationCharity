package org.bashaer.bashaerfoundationcharity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:sqlite:bashaer.db";

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite database successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // SELF-HEALING: If connection was accidentally closed elsewhere, reopen it automatically!
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Database connection was re-established successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to check/reconnect to database: " + e.getMessage());
        }
        return connection;
    }
}