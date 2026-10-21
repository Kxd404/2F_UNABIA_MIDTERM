package com.unabia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the SQLite connection and schema setup.
 * All JDBC logic lives in the dao package.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() {
        String createTable = """
                CREATE TABLE IF NOT EXISTS luggage (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    passenger_name TEXT NOT NULL,
                    flight_no TEXT NOT NULL,
                    weight_kg REAL NOT NULL,
                    status TEXT NOT NULL
                )
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }
}
