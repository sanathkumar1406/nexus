package com.nexusapp.model;

import java.time.LocalDateTime;

public class StudySession {
    private int id;
    private String subject;
    private int durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private LocalDateTime createdAt;

    public StudySession() {}

    public StudySession(String subject, int durationMinutes, LocalDateTime startTime) {
        this.subject = subject;
        this.durationMinutes = durationMinutes;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(durationMinutes);
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return subject + " - " + durationMinutes + " minutes";
    }
}  
