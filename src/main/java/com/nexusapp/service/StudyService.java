package com.nexusapp.service;

import com.nexusapp.database.DatabaseConnection;
import com.nexusapp.model.StudySession;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudyService {
    
    public static void createStudySession(StudySession session) {
        String sql = "INSERT INTO study_sessions (subject, duration_minutes, start_time, end_time, notes, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, session.getSubject());
            pstmt.setInt(2, session.getDurationMinutes());
            pstmt.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            pstmt.setTimestamp(4, session.getEndTime() != null ? Timestamp.valueOf(session.getEndTime()) : null);
            pstmt.setString(5, session.getNotes());
            pstmt.setTimestamp(6, Timestamp.valueOf(session.getCreatedAt()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    session.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating study session: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List<StudySession> getAllStudySessions() {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM study_sessions ORDER BY start_time DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                StudySession session = new StudySession();
                session.setId(rs.getInt("id"));
                session.setSubject(rs.getString("subject"));
                session.setDurationMinutes(rs.getInt("duration_minutes"));
                session.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                
                Timestamp endTime = rs.getTimestamp("end_time");
                if (endTime != null) {
                    session.setEndTime(endTime.toLocalDateTime());
                }
                
                session.setNotes(rs.getString("notes"));
                session.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                sessions.add(session);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving study sessions: " + e.getMessage());
        }
        
        return sessions;
    }
    
    public static void updateStudySession(StudySession session) {
        String sql = "UPDATE study_sessions SET subject = ?, duration_minutes = ?, start_time = ?, end_time = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, session.getSubject());
            pstmt.setInt(2, session.getDurationMinutes());
            pstmt.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            pstmt.setTimestamp(4, session.getEndTime() != null ? Timestamp.valueOf(session.getEndTime()) : null);
            pstmt.setString(5, session.getNotes());
            pstmt.setInt(6, session.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating study session: " + e.getMessage());
        }
    }
    
    public static void deleteStudySession(int sessionId) {
        String sql = "DELETE FROM study_sessions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, sessionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting study session: " + e.getMessage());
        }
    }
    
    public static int getTotalStudyTime() {
        String sql = "SELECT SUM(duration_minutes) as total FROM study_sessions";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total study time: " + e.getMessage());
        }
        
        return 0;
    }
    
    public static List<StudySession> getStudySessionsBySubject(String subject) {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM study_sessions WHERE subject = ? ORDER BY start_time DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subject);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StudySession session = new StudySession();
                session.setId(rs.getInt("id"));
                session.setSubject(rs.getString("subject"));
                session.setDurationMinutes(rs.getInt("duration_minutes"));
                session.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                
                Timestamp endTime = rs.getTimestamp("end_time");
                if (endTime != null) {
                    session.setEndTime(endTime.toLocalDateTime());
                }
                
                session.setNotes(rs.getString("notes"));
                session.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                sessions.add(session);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving study sessions by subject: " + e.getMessage());
        }
        
        return sessions;
    }
}  
