package com.nexusapp.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL;
    private static Connection connection = null;
    
    static {
        // Always use the same database path - target/classes/db/nexus_app.db
        // This is where Maven copies resources to and where the app runs from
        File dbFile = new File("target/classes/db/nexus_app.db");
        
        // Create the directory if it doesn't exist
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            System.out.println("Created database directory: " + parentDir.getAbsolutePath() + " (success: " + created + ")");
        }
        
        DB_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        System.out.println("Database URL: " + DB_URL);
        System.out.println("Database file exists: " + dbFile.exists());
        System.out.println("Database file path: " + dbFile.getAbsolutePath());
    }

    public static Connection getConnection() {
        try {
            // Create a new connection each time (don't reuse the static connection)
            Connection conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            System.out.println("New database connection created");
            return conn;
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public static void executeUpdate(String sql) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
        }
    }
}  
