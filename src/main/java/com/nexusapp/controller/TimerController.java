package com.nexusapp.controller;

import com.nexusapp.model.StudySession;
import com.nexusapp.service.StudyService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimerController {

    @FXML private TextField subjectField;
    @FXML private TextArea notesArea;
    @FXML private Label elapsedLabel;

    @FXML private TableView<StudySession> sessionTable;
    @FXML private TableColumn<StudySession, String> colSubject;
    @FXML private TableColumn<StudySession, String> colDuration;
    @FXML private TableColumn<StudySession, String> colStart;
    @FXML private TableColumn<StudySession, String> colEnd;

    private Timeline timeline;
    private long elapsedSeconds = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void initialize() {
        colSubject.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getSubject()));
        colDuration.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cd.getValue().getDurationMinutes())));
        colStart.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        colEnd.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEndTime() != null ? cd.getValue().getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "-"));

        sessionTable.getItems().setAll(StudyService.getAllStudySessions());

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds++;
            elapsedLabel.setText(format(elapsedSeconds));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    private void onStart() {
        elapsedSeconds = 0;
        elapsedLabel.setText("00:00:00");
        startTime = LocalDateTime.now();
        endTime = null;
        timeline.playFromStart();
    }

    @FXML
    private void onStop() {
        timeline.stop();
        endTime = LocalDateTime.now();
    }

    @FXML
    private void onSave() {
        if (startTime == null) {
            new Alert(Alert.AlertType.INFORMATION, "Start the timer first").showAndWait();
            return;
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
            timeline.stop();
        }
        
        // Calculate duration from actual time difference for accuracy
        long durationSeconds = ChronoUnit.SECONDS.between(startTime, endTime);
        int minutes = (int) Math.max(1, Math.round(durationSeconds / 60.0));
        
        StudySession s = new StudySession();
        s.setSubject(subjectField.getText() == null || subjectField.getText().isBlank() ? "General" : subjectField.getText());
        s.setDurationMinutes(minutes);
        s.setStartTime(startTime);
        s.setEndTime(endTime);
        s.setNotes(notesArea.getText());
        s.setCreatedAt(LocalDateTime.now());
        StudyService.createStudySession(s);
        sessionTable.getItems().setAll(StudyService.getAllStudySessions());
        clearForm();
    }

    private void clearForm() {
        subjectField.clear();
        notesArea.clear();
        elapsedSeconds = 0;
        elapsedLabel.setText("00:00:00");
        startTime = null;
        endTime = null;
    }

    private static String format(long totalSeconds) {
        long h = totalSeconds / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
