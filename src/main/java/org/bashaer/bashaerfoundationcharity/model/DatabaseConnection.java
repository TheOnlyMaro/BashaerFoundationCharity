package org.bashaer.bashaerfoundationcharity.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // 1. Private static instance of the class (Singleton)
    private static DatabaseConnection instance;
    private Connection connection;

    // SQLite database file name (will be created automatically in your project root)
    private static final String URL = "jdbc:sqlite:bashaer.db";

    // 2. Private constructor prevents direct instantiation
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite database successfully.");

            // Automatically initialize Phase 1 lookup tables upon connection
            initializeTables();

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    // 3. Public global access method to get the single instance
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Get the active SQL connection
    public Connection getConnection() {
        return connection;
    }

    // Initialize the required CRUD lookup tables for Phase 1
    private void initializeTables() {
        String createDonationCategoryTable = """
            CREATE TABLE IF NOT EXISTS donation_categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT
            );
        """;

        String createVolunteerSkillsTable = """
            CREATE TABLE IF NOT EXISTS volunteer_skills (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                skill_name TEXT NOT NULL
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createDonationCategoryTable);
            stmt.execute(createVolunteerSkillsTable);

            // Insert some default mock lookup data if tables are empty
            stmt.execute("INSERT OR IGNORE INTO donation_categories (id, name, description) VALUES (1, 'Food & Supplies', 'In-kind daily necessities');");
            stmt.execute("INSERT OR IGNORE INTO donation_categories (id, name, description) VALUES (2, 'Medical Aid', 'Healthcare and emergency treatment funds');");
            stmt.execute("INSERT OR IGNORE INTO volunteer_skills (id, skill_name) VALUES (1, 'Logistics & Driving');");
            stmt.execute("INSERT OR IGNORE INTO volunteer_skills (id, skill_name) VALUES (2, 'Case Counseling');");

            System.out.println("Lookup tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize tables: " + e.getMessage());
        }
    }
}