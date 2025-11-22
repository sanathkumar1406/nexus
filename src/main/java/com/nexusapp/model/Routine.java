package com.nexusapp.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class Routine {
    private int id;
    private String title;
    private String description;
    private String daysOfWeek; // Comma-separated days: "MONDAY,TUESDAY,FRIDAY"
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isActive;
    private LocalDateTime createdAt;
    private boolean completed; // For daily completion tracking

    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public Routine() {
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    public Routine(String title, String description, String daysOfWeek, LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.description = description;
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(String daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    
    // Helper method to get list of days
    public List<String> getDaysList() {
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(daysOfWeek.split(","));
    }
    
    // Helper method to check if routine is for a specific day
    public boolean isForDay(String day) {
        List<String> days = getDaysList();
        return days.contains(day);
    }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return title + " - " + daysOfWeek + " " + startTime + "-" + endTime;
    }
}  
