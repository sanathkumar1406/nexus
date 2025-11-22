package com.nexusapp.controller;

import com.nexusapp.model.Task;
import com.nexusapp.model.Note;
import com.nexusapp.model.Routine;
import com.nexusapp.service.TaskService;
import com.nexusapp.service.NoteService;
import com.nexusapp.service.StudyService;
import com.nexusapp.service.RoutineService;
import com.nexusapp.utils.ThemeManager;
import com.nexusapp.utils.NotificationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.List;

public class DashboardController {

    @FXML private Button themeToggleBtn;
    @FXML private Button settingsBtn;
    @FXML private Button dashboardBtn;
    @FXML private Button tasksBtn;
    @FXML private Button notesBtn;
    @FXML private Button routineBtn;
    @FXML private Button timerBtn;
    
    @FXML private StackPane contentPane;
    @FXML private VBox dashboardContent;
    
    // Dashboard stats
    @FXML private Label totalStudyTime;
    @FXML private Label sessionsCompleted;
    @FXML private Label avgSessionTime;
    @FXML private Label pendingTasks;
    @FXML private Label completedTasks;
    @FXML private Label urgentTasks;
    @FXML private Label totalNotes;
    @FXML private Label recentNotes;
    @FXML private Label notesWithFiles;
    
    // Tables
    @FXML private TableView<Task> recentTasksTable;
    @FXML private TableColumn<Task, String> taskTitleCol;
    @FXML private TableColumn<Task, String> taskPriorityCol;
    @FXML private TableColumn<Task, String> taskDueDateCol;
    @FXML private TableColumn<Task, String> taskStatusCol;
    
    @FXML private TableView<Note> recentNotesTable;
    @FXML private TableColumn<Note, String> noteTitleCol;
    @FXML private TableColumn<Note, String> noteCreatedCol;
    @FXML private TableColumn<Note, String> noteUpdatedCol;
    
    @FXML private VBox todaysRoutinesContainer;

    private Stage primaryStage;
    private Scene scene;

    public void initialize() {
        setupTableColumns();
        loadDashboardData();
        updateStats();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        this.scene = stage.getScene();
    }

    private void setupTableColumns() {
        // Task table columns
        taskTitleCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        taskPriorityCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPriority().name()));
        taskDueDateCol.setCellValueFactory(cellData -> {
            String dueDate = cellData.getValue().getDueDate() != null ? 
                cellData.getValue().getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "No due date";
            return new javafx.beans.property.SimpleStringProperty(dueDate);
        });
        taskStatusCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().name()));

        // Note table columns
        noteTitleCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        noteCreatedCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))));
        noteUpdatedCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getUpdatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))));
    }

    private void loadDashboardData() {
        // Load recent tasks (last 5)
        List<Task> allTasks = TaskService.getAllTasks();
        recentTasksTable.getItems().setAll(allTasks.stream().limit(5).toList());

        // Load recent notes (last 5)
        List<Note> allNotes = NoteService.getAllNotes();
        recentNotesTable.getItems().setAll(allNotes.stream().limit(5).toList());
        
        // Load today's routines
        loadTodaysRoutines();
    }
    
    private void loadTodaysRoutines() {
        todaysRoutinesContainer.getChildren().clear();
        
        // Get today's day of week
        DayOfWeek today = java.time.LocalDate.now().getDayOfWeek();
        String todayString = today.name();
        
        // Get routines for today
        List<Routine> todaysRoutines = RoutineService.getRoutinesByDay(todayString);
        
        if (todaysRoutines.isEmpty()) {
            Label noRoutinesLabel = new Label("No routines scheduled for today");
            noRoutinesLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
            todaysRoutinesContainer.getChildren().add(noRoutinesLabel);
        } else {
            for (Routine routine : todaysRoutines) {
                HBox routineBox = new HBox(10);
                routineBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5; -fx-padding: 10;");
                routineBox.setPrefWidth(600);
                
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(routine.isCompleted());
                checkBox.setOnAction(e -> {
                    routine.setCompleted(checkBox.isSelected());
                    // Update visual state
                    if (checkBox.isSelected()) {
                        routineBox.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 5; -fx-padding: 10;");
                    } else {
                        routineBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5; -fx-padding: 10;");
                    }
                });
                
                VBox routineInfo = new VBox(3);
                Label titleLabel = new Label(routine.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
                
                Label timeLabel = new Label(routine.getStartTime() + " - " + routine.getEndTime());
                timeLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11;");
                
                if (routine.getDescription() != null && !routine.getDescription().isEmpty()) {
                    Label descLabel = new Label(routine.getDescription());
                    descLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 11;");
                    descLabel.setWrapText(true);
                    routineInfo.getChildren().addAll(titleLabel, timeLabel, descLabel);
                } else {
                    routineInfo.getChildren().addAll(titleLabel, timeLabel);
                }
                
                routineBox.getChildren().addAll(checkBox, routineInfo);
                todaysRoutinesContainer.getChildren().add(routineBox);
                
                // Set initial visual state
                if (routine.isCompleted()) {
                    routineBox.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 5; -fx-padding: 10;");
                }
            }
        }
    }

    private void updateStats() {
        // Study statistics
        int totalMinutes = StudyService.getTotalStudyTime();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        totalStudyTime.setText(String.format("Total Study Time: %d hours %d minutes", hours, minutes));
        
        List<com.nexusapp.model.StudySession> sessions = StudyService.getAllStudySessions();
        sessionsCompleted.setText("Sessions: " + sessions.size());
        
        if (!sessions.isEmpty()) {
            double avgMinutes = (double) totalMinutes / sessions.size();
            avgSessionTime.setText(String.format("Avg Session: %.1f min", avgMinutes));
        } else {
            avgSessionTime.setText("Avg Session: 0 min");
        }

        // Task statistics
        List<Task> tasks = TaskService.getAllTasks();
        long pendingCount = tasks.stream().filter(t -> t.getStatus() == Task.Status.PENDING).count();
        long completedCount = tasks.stream().filter(t -> t.getStatus() == Task.Status.COMPLETED).count();
        long urgentCount = tasks.stream().filter(t -> t.getPriority() == Task.Priority.URGENT).count();
        
        pendingTasks.setText("Pending: " + pendingCount);
        completedTasks.setText("Completed: " + completedCount);
        urgentTasks.setText("Urgent: " + urgentCount);

        // Note statistics
        List<Note> notes = NoteService.getAllNotes();
        totalNotes.setText("Total Notes: " + notes.size());
        recentNotes.setText("Recent: " + notes.stream().limit(5).count());
        long notesWithFilesCount = notes.stream().filter(n -> n.getFilePath() != null && !n.getFilePath().isEmpty()).count();
        notesWithFiles.setText("With Files: " + notesWithFilesCount);
    }

    @FXML
    private void toggleTheme() {
        if (scene != null) {
            ThemeManager.toggleTheme(scene);
            themeToggleBtn.setText(ThemeManager.isDarkMode() ? "☀️" : "🌙");
        }
    }

    @FXML
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
            VBox settingsContent = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(settingsContent);
            updateNavigationButtons(settingsBtn);
        } catch (IOException e) {
            NotificationUtil.showError("Error", "Could not load settings page");
        }
    }

    @FXML
    private void showDashboard() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(dashboardContent);
        updateNavigationButtons(dashboardBtn);
        loadDashboardData();
        updateStats();
    }
    
    @FXML
    private void refreshDashboard() {
        loadDashboardData();
        updateStats();
    }

    @FXML
    private void showTasks() {
        try {
            System.out.println("Loading tasks page...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tasks.fxml"));
            VBox tasksContent = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(tasksContent);
            updateNavigationButtons(tasksBtn);
            System.out.println("Tasks page loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading tasks page: " + e.getMessage());
            e.printStackTrace();
            NotificationUtil.showError("Error", "Could not load tasks page: " + e.getMessage());
        }
    }

    @FXML
    private void showNotes() {
        try {
            System.out.println("Loading notes page...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/notes.fxml"));
            VBox notesContent = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(notesContent);
            updateNavigationButtons(notesBtn);
            System.out.println("Notes page loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading notes page: " + e.getMessage());
            e.printStackTrace();
            NotificationUtil.showError("Error", "Could not load notes page: " + e.getMessage());
        }
    }

    @FXML
    private void showRoutine() {
        try {
            System.out.println("Loading routine page...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/routine.fxml"));
            VBox routineContent = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(routineContent);
            updateNavigationButtons(routineBtn);
            System.out.println("Routine page loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading routine page: " + e.getMessage());
            e.printStackTrace();
            NotificationUtil.showError("Error", "Could not load routine page: " + e.getMessage());
        }
    }

    @FXML
    private void showTimer() {
        try {
            System.out.println("Loading timer page...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/timer.fxml"));
            VBox timerContent = loader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(timerContent);
            updateNavigationButtons(timerBtn);
            System.out.println("Timer page loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading timer page: " + e.getMessage());
            e.printStackTrace();
            NotificationUtil.showError("Error", "Could not load timer page: " + e.getMessage());
        }
    }

    private void updateNavigationButtons(Button activeButton) {
        // Remove active class from all buttons
        dashboardBtn.getStyleClass().remove("active");
        tasksBtn.getStyleClass().remove("active");
        notesBtn.getStyleClass().remove("active");
        routineBtn.getStyleClass().remove("active");
        timerBtn.getStyleClass().remove("active");
        settingsBtn.getStyleClass().remove("active");
        
        // Add active class to the selected button
        if (activeButton != null) {
            activeButton.getStyleClass().add("active");
        }
    }
}  
