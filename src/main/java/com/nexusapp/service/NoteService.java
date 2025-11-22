package com.nexusapp.service;

import com.nexusapp.database.DatabaseConnection;
import com.nexusapp.model.Note;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteService {
    
    public static void createNote(Note note) {
        String sql = "INSERT INTO notes (title, content, file_path, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, note.getTitle());
            pstmt.setString(2, note.getContent());
            pstmt.setString(3, note.getFilePath());
            pstmt.setTimestamp(4, Timestamp.valueOf(note.getCreatedAt()));
            pstmt.setTimestamp(5, Timestamp.valueOf(note.getUpdatedAt()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    note.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating note: " + e.getMessage());
        }
    }
    
    public static List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setFilePath(rs.getString("file_path"));
                note.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                note.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                notes.add(note);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving notes: " + e.getMessage());
        }
        
        return notes;
    }
    
    public static void updateNote(Note note) {
        String sql = "UPDATE notes SET title = ?, content = ?, file_path = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, note.getTitle());
            pstmt.setString(2, note.getContent());
            pstmt.setString(3, note.getFilePath());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(5, note.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating note: " + e.getMessage());
        }
    }
    
    public static void deleteNote(int noteId) {
        String sql = "DELETE FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting note: " + e.getMessage());
        }
    }
    
    public static Note getNoteById(int noteId) {
        String sql = "SELECT * FROM notes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setFilePath(rs.getString("file_path"));
                note.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                note.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                return note;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving note: " + e.getMessage());
        }
        
        return null;
    }
}  
