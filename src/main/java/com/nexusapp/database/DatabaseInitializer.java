package com.nexusapp.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initialize() {
        try {
            // Try multiple possible locations for the database
            String[] possiblePaths = {
                "target/classes/db",
                "db",
                "."
            };
            
            File dbDir = null;
            for (String path : possiblePaths) {
                File testDir = new File(path);
                if (testDir.exists() && testDir.isDirectory()) {
                    dbDir = testDir;
                    System.out.println("Using database directory: " + testDir.getAbsolutePath());
                    break;
                }
            }
            
            // If no directory found, create the first one
            if (dbDir == null) {
                dbDir = new File(possiblePaths[0]);
                dbDir.mkdirs();
                System.out.println("Created database directory: " + dbDir.getAbsolutePath());
            }
            
            // Create tables
            createTables();
            System.out.println("Database initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTables() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createUsersTable);
            
            // Tasks table
            String createTasksTable = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    priority TEXT DEFAULT 'MEDIUM',
                    due_date DATETIME,
                    status TEXT DEFAULT 'PENDING',
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createTasksTable);
            
            // Notes table
            String createNotesTable = """
                CREATE TABLE IF NOT EXISTS notes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    content TEXT,
                    file_path TEXT,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createNotesTable);
            
            // Routines table
            String createRoutinesTable = """
                CREATE TABLE IF NOT EXISTS routines (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    days_of_week TEXT NOT NULL,
                    start_time TEXT NOT NULL,
                    end_time TEXT NOT NULL,
                    is_active BOOLEAN DEFAULT 1,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createRoutinesTable);
            
            // Migrate existing routines if needed
            migrateRoutinesTable(conn);
            
            // Study sessions table
            String createStudySessionsTable = """
                CREATE TABLE IF NOT EXISTS study_sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    subject TEXT NOT NULL,
                    duration_minutes INTEGER NOT NULL,
                    start_time DATETIME NOT NULL,
                    end_time DATETIME,
                    notes TEXT,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createStudySessionsTable);
            
        }
    }
    
    private static void migrateRoutinesTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Check if old column exists
            java.sql.ResultSet rs = conn.getMetaData().getColumns(null, null, "routines", "day_of_week");
            if (rs.next()) {
                System.out.println("Old 'day_of_week' column found. Migrating...");
                
                // Check if new column exists
                rs = conn.getMetaData().getColumns(null, null, "routines", "days_of_week");
                if (!rs.next()) {
                    // Add new column
                    stmt.execute("ALTER TABLE routines ADD COLUMN days_of_week TEXT");
                    System.out.println("Added 'days_of_week' column");
                }
                
                // Copy data from old column to new column
                stmt.execute("UPDATE routines SET days_of_week = day_of_week WHERE days_of_week IS NULL OR days_of_week = ''");
                System.out.println("Copied data from 'day_of_week' to 'days_of_week'");
                
                // Drop the old column (SQLite doesn't support DROP COLUMN directly, so we need to recreate the table)
                // First, get all data
                java.sql.ResultSet allData = stmt.executeQuery("SELECT id, title, description, days_of_week, start_time, end_time, is_active, created_at FROM routines");
                java.util.List<String> insertStatements = new java.util.ArrayList<>();
                
                while (allData.next()) {
                    insertStatements.add(String.format(
                        "INSERT INTO routines_temp (id, title, description, days_of_week, start_time, end_time, is_active, created_at) VALUES (%d, '%s', '%s', '%s', '%s', '%s', %d, '%s')",
                        allData.getInt("id"),
                        allData.getString("title").replace("'", "''"),
                        allData.getString("description") != null ? allData.getString("description").replace("'", "''") : "",
                        allData.getString("days_of_week"),
                        allData.getString("start_time"),
                        allData.getString("end_time"),
                        allData.getBoolean("is_active") ? 1 : 0,
                        allData.getString("created_at")
                    ));
                }
                
                // Create new table without the old column
                stmt.execute("CREATE TABLE IF NOT EXISTS routines_temp (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, description TEXT, days_of_week TEXT NOT NULL, start_time TEXT NOT NULL, end_time TEXT NOT NULL, is_active BOOLEAN DEFAULT 1, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
                
                // Insert all data into new table
                for (String insert : insertStatements) {
                    stmt.execute(insert);
                }
                
                // Drop old table and rename new one
                stmt.execute("DROP TABLE routines");
                stmt.execute("ALTER TABLE routines_temp RENAME TO routines");
                
                System.out.println("Routines table migrated successfully! Old 'day_of_week' column removed.");
            } else {
                System.out.println("No migration needed. 'days_of_week' column already exists.");
            }
        } catch (Exception e) {
            // Migration failed, but continue
            System.err.println("Migration error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}  
