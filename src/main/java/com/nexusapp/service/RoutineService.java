package com.nexusapp.service;

import com.nexusapp.database.DatabaseConnection;
import com.nexusapp.model.Routine;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoutineService {
    
    public static void createRoutine(Routine routine) {
        String sql = "INSERT INTO routines (title, description, days_of_week, start_time, end_time, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        System.out.println("RoutineService.createRoutine() - Inserting routine: " + routine.getTitle());
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("ERROR: Database connection is null!");
                return;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, routine.getTitle());
                pstmt.setString(2, routine.getDescription());
                pstmt.setString(3, routine.getDaysOfWeek());
                pstmt.setString(4, routine.getStartTime().toString());
                pstmt.setString(5, routine.getEndTime().toString());
                pstmt.setBoolean(6, routine.isActive());
                pstmt.setTimestamp(7, Timestamp.valueOf(routine.getCreatedAt()));
                
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        routine.setId(id);
                        System.out.println("Routine created with ID: " + id);
                    } else {
                        System.out.println("No generated keys returned!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating routine: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List<Routine> getAllRoutines() {
        List<Routine> routines = new ArrayList<>();
        String sql = "SELECT * FROM routines ORDER BY days_of_week, start_time";
        
        System.out.println("RoutineService.getAllRoutines() - Executing query: " + sql);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("ERROR: Database connection is null!");
                return routines;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Routine routine = new Routine();
                    routine.setId(rs.getInt("id"));
                    routine.setTitle(rs.getString("title"));
                    routine.setDescription(rs.getString("description"));
                    // Try new column first, fallback to old column for migration
                    String daysOfWeek = rs.getString("days_of_week");
                    if (daysOfWeek == null || daysOfWeek.isEmpty()) {
                        daysOfWeek = rs.getString("day_of_week");
                    }
                    routine.setDaysOfWeek(daysOfWeek);
                    routine.setStartTime(LocalTime.parse(rs.getString("start_time")));
                    routine.setEndTime(LocalTime.parse(rs.getString("end_time")));
                    routine.setActive(rs.getBoolean("is_active"));
                    routine.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    
                    System.out.println("  - Found routine: " + routine.getTitle() + " (ID: " + routine.getId() + ")");
                    routines.add(routine);
                }
                
                System.out.println("Total routines retrieved: " + routines.size());
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving routines: " + e.getMessage());
            e.printStackTrace();
        }
        
        return routines;
    }
    
    public static void updateRoutine(Routine routine) {
        String sql = "UPDATE routines SET title = ?, description = ?, days_of_week = ?, start_time = ?, end_time = ?, is_active = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, routine.getTitle());
            pstmt.setString(2, routine.getDescription());
            pstmt.setString(3, routine.getDaysOfWeek());
            pstmt.setString(4, routine.getStartTime().toString());
            pstmt.setString(5, routine.getEndTime().toString());
            pstmt.setBoolean(6, routine.isActive());
            pstmt.setInt(7, routine.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating routine: " + e.getMessage());
        }
    }
    
    public static void deleteRoutine(int routineId) {
        String sql = "DELETE FROM routines WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, routineId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting routine: " + e.getMessage());
        }
    }
    
    public static List<Routine> getRoutinesByDay(String dayOfWeek) {
        List<Routine> routines = new ArrayList<>();
        // Get all active routines and filter by day
        String sql = "SELECT * FROM routines WHERE is_active = 1 ORDER BY start_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Routine routine = new Routine();
                routine.setId(rs.getInt("id"));
                routine.setTitle(rs.getString("title"));
                routine.setDescription(rs.getString("description"));
                // Try new column first, fallback to old column for migration
                String daysOfWeek = rs.getString("days_of_week");
                if (daysOfWeek == null || daysOfWeek.isEmpty()) {
                    daysOfWeek = rs.getString("day_of_week");
                }
                routine.setDaysOfWeek(daysOfWeek);
                routine.setStartTime(LocalTime.parse(rs.getString("start_time")));
                routine.setEndTime(LocalTime.parse(rs.getString("end_time")));
                routine.setActive(rs.getBoolean("is_active"));
                routine.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                // Filter by day
                if (routine.isForDay(dayOfWeek)) {
                    routines.add(routine);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving routines by day: " + e.getMessage());
        }
        
        return routines;
    }
}  
