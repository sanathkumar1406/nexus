package com.nexusapp.service;

import com.nexusapp.database.DatabaseConnection;
import com.nexusapp.model.Task;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    
    public static void createTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, priority, due_date, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getPriority().name());
            pstmt.setTimestamp(4, task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate()) : null);
            pstmt.setString(5, task.getStatus().name());
            pstmt.setTimestamp(6, Timestamp.valueOf(task.getCreatedAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(task.getUpdatedAt()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
    }
    
    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(Task.Priority.valueOf(rs.getString("priority")));
                
                Timestamp dueDate = rs.getTimestamp("due_date");
                if (dueDate != null) {
                    task.setDueDate(dueDate.toLocalDateTime());
                }
                
                task.setStatus(Task.Status.valueOf(rs.getString("status")));
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tasks: " + e.getMessage());
        }
        
        return tasks;
    }
    
    public static void updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, due_date = ?, status = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getPriority().name());
            pstmt.setTimestamp(4, task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate()) : null);
            pstmt.setString(5, task.getStatus().name());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(7, task.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
        }
    }
    
    public static void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
    }
    
    public static Task getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(Task.Priority.valueOf(rs.getString("priority")));
                
                Timestamp dueDate = rs.getTimestamp("due_date");
                if (dueDate != null) {
                    task.setDueDate(dueDate.toLocalDateTime());
                }
                
                task.setStatus(Task.Status.valueOf(rs.getString("status")));
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                return task;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving task: " + e.getMessage());
        }
        
        return null;
    }
}  
