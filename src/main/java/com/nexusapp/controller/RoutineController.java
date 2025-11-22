package com.nexusapp.controller;

import com.nexusapp.model.Routine;
import com.nexusapp.service.RoutineService;
import com.nexusapp.utils.NotificationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class RoutineController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox mondayCheck, tuesdayCheck, wednesdayCheck, thursdayCheck, fridayCheck, saturdayCheck, sundayCheck;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private CheckBox activeCheck;

    @FXML private TableView<Routine> routineTable;
    @FXML private TableColumn<Routine, String> colTitle;
    @FXML private TableColumn<Routine, String> colDays;
    @FXML private TableColumn<Routine, String> colStart;
    @FXML private TableColumn<Routine, String> colEnd;
    @FXML private TableColumn<Routine, String> colActive;

    private final ObservableList<Routine> routines = FXCollections.observableArrayList();

    public void initialize() {
        System.out.println("Initializing RoutineController...");
        
        colTitle.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTitle()));
        colDays.setCellValueFactory(cd -> {
            String days = cd.getValue().getDaysOfWeek();
            if (days != null) {
                return new javafx.beans.property.SimpleStringProperty(days.replace(",", ", "));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        colStart.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getStartTime().toString()));
        colEnd.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getEndTime().toString()));
        colActive.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().isActive() ? "Yes" : "No"));

        List<Routine> allRoutines = RoutineService.getAllRoutines();
        System.out.println("Loaded " + allRoutines.size() + " routines from database");
        routines.setAll(allRoutines);
        routineTable.setItems(routines);
        System.out.println("Table initialized with " + routineTable.getItems().size() + " items");
        routineTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> fillForm(n));
    }

    private void fillForm(Routine r) {
        if (r == null) return;
        titleField.setText(r.getTitle());
        descriptionArea.setText(r.getDescription());
        
        // Set the checkboxes based on the days
        List<String> days = r.getDaysList();
        mondayCheck.setSelected(days.contains("MONDAY"));
        tuesdayCheck.setSelected(days.contains("TUESDAY"));
        wednesdayCheck.setSelected(days.contains("WEDNESDAY"));
        thursdayCheck.setSelected(days.contains("THURSDAY"));
        fridayCheck.setSelected(days.contains("FRIDAY"));
        saturdayCheck.setSelected(days.contains("SATURDAY"));
        sundayCheck.setSelected(days.contains("SUNDAY"));
        
        startTimeField.setText(r.getStartTime().toString());
        endTimeField.setText(r.getEndTime().toString());
        activeCheck.setSelected(r.isActive());
    }

    @FXML
    private void onAdd() {
        try {
            // Validate input
            if (titleField.getText().trim().isEmpty()) {
                NotificationUtil.showError("Error", "Please enter a title");
                return;
            }
            
            if (startTimeField.getText().trim().isEmpty() || endTimeField.getText().trim().isEmpty()) {
                NotificationUtil.showError("Error", "Please enter start and end times");
                return;
            }
            
            // Parse times with validation
            LocalTime startTime, endTime;
            try {
                startTime = LocalTime.parse(startTimeField.getText().trim());
                endTime = LocalTime.parse(endTimeField.getText().trim());
            } catch (Exception e) {
                NotificationUtil.showError("Error", "Please enter times in HH:mm format (e.g., 09:00)");
                return;
            }
            
            // Check if at least one day is selected
            if (!mondayCheck.isSelected() && !tuesdayCheck.isSelected() && !wednesdayCheck.isSelected() && 
                !thursdayCheck.isSelected() && !fridayCheck.isSelected() && !saturdayCheck.isSelected() && 
                !sundayCheck.isSelected()) {
                NotificationUtil.showError("Error", "Please select at least one day");
                return;
            }
            
            // Collect selected days
            List<String> selectedDays = new java.util.ArrayList<>();
            if (mondayCheck.isSelected()) selectedDays.add("MONDAY");
            if (tuesdayCheck.isSelected()) selectedDays.add("TUESDAY");
            if (wednesdayCheck.isSelected()) selectedDays.add("WEDNESDAY");
            if (thursdayCheck.isSelected()) selectedDays.add("THURSDAY");
            if (fridayCheck.isSelected()) selectedDays.add("FRIDAY");
            if (saturdayCheck.isSelected()) selectedDays.add("SATURDAY");
            if (sundayCheck.isSelected()) selectedDays.add("SUNDAY");
            
            // Create a single routine with all selected days
            Routine routine = new Routine();
            routine.setTitle(titleField.getText().trim());
            routine.setDescription(descriptionArea.getText().trim());
            routine.setDaysOfWeek(String.join(",", selectedDays));
            routine.setStartTime(startTime);
            routine.setEndTime(endTime);
            routine.setActive(activeCheck.isSelected());
            
            System.out.println("Creating routine: " + routine.getTitle() + " for days: " + routine.getDaysOfWeek());
            
            RoutineService.createRoutine(routine);
            
            System.out.println("Routine created with ID: " + routine.getId());
            
            // Refresh the table
            List<Routine> allRoutines = RoutineService.getAllRoutines();
            System.out.println("Total routines in database: " + allRoutines.size());
            routines.setAll(allRoutines);
            System.out.println("Table items count: " + routineTable.getItems().size());
            
            clearForm();
            NotificationUtil.showSuccess("Routine created successfully!");
        } catch (Exception e) {
            NotificationUtil.showError("Error", "Failed to create routine: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    @FXML
    private void onUpdate() {
        Routine selected = routineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationUtil.showError("Error", "Please select a routine to update");
            return;
        }
        
        try {
            // Validate input
            if (titleField.getText().trim().isEmpty()) {
                NotificationUtil.showError("Error", "Please enter a title");
                return;
            }
            
            if (startTimeField.getText().trim().isEmpty() || endTimeField.getText().trim().isEmpty()) {
                NotificationUtil.showError("Error", "Please enter start and end times");
                return;
            }
            
            // Parse times with validation
            LocalTime startTime, endTime;
            try {
                startTime = LocalTime.parse(startTimeField.getText().trim());
                endTime = LocalTime.parse(endTimeField.getText().trim());
            } catch (Exception e) {
                NotificationUtil.showError("Error", "Please enter times in HH:mm format (e.g., 09:00)");
                return;
            }
            
            // Check if at least one day is selected
            if (!mondayCheck.isSelected() && !tuesdayCheck.isSelected() && !wednesdayCheck.isSelected() && 
                !thursdayCheck.isSelected() && !fridayCheck.isSelected() && !saturdayCheck.isSelected() && 
                !sundayCheck.isSelected()) {
                NotificationUtil.showError("Error", "Please select at least one day");
                return;
            }
            
            // Collect selected days
            List<String> selectedDays = new java.util.ArrayList<>();
            if (mondayCheck.isSelected()) selectedDays.add("MONDAY");
            if (tuesdayCheck.isSelected()) selectedDays.add("TUESDAY");
            if (wednesdayCheck.isSelected()) selectedDays.add("WEDNESDAY");
            if (thursdayCheck.isSelected()) selectedDays.add("THURSDAY");
            if (fridayCheck.isSelected()) selectedDays.add("FRIDAY");
            if (saturdayCheck.isSelected()) selectedDays.add("SATURDAY");
            if (sundayCheck.isSelected()) selectedDays.add("SUNDAY");
            
            // Update the selected routine
            selected.setTitle(titleField.getText().trim());
            selected.setDescription(descriptionArea.getText().trim());
            selected.setDaysOfWeek(String.join(",", selectedDays));
            selected.setStartTime(startTime);
            selected.setEndTime(endTime);
            selected.setActive(activeCheck.isSelected());
            
            RoutineService.updateRoutine(selected);
            routines.setAll(RoutineService.getAllRoutines());
            clearForm();
            NotificationUtil.showSuccess("Routine updated successfully!");
        } catch (Exception e) {
            NotificationUtil.showError("Error", "Failed to update routine: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete() {
        Routine selected = routineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a routine to delete").showAndWait();
            return;
        }
        RoutineService.deleteRoutine(selected.getId());
        routines.setAll(RoutineService.getAllRoutines());
        clearForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        mondayCheck.setSelected(false);
        tuesdayCheck.setSelected(false);
        wednesdayCheck.setSelected(false);
        thursdayCheck.setSelected(false);
        fridayCheck.setSelected(false);
        saturdayCheck.setSelected(false);
        sundayCheck.setSelected(false);
        startTimeField.clear();
        endTimeField.clear();
        activeCheck.setSelected(true);
        routineTable.getSelectionModel().clearSelection();
    }
}
