package com.nexusapp.controller;

import com.nexusapp.model.Task;
import com.nexusapp.service.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TasksController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Task.Priority> priorityBox;
    @FXML private DatePicker dueDatePicker;

    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colPriority;
    @FXML private TableColumn<Task, String> colDue;
    @FXML private TableColumn<Task, String> colStatus;

    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    public void initialize() {
        priorityBox.getItems().setAll(Task.Priority.values());

        colTitle.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTitle()));
        colPriority.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getPriority().name()));
        colDue.setCellValueFactory(cd -> {
            LocalDateTime dt = cd.getValue().getDueDate();
            String text = dt == null ? "-" : dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(text);
        });
        colStatus.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getStatus().name()));

        tasks.setAll(TaskService.getAllTasks());
        tasksTable.setItems(tasks);

        tasksTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> fillForm(n));
    }

    private void fillForm(Task task) {
        if (task == null) return;
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        priorityBox.setValue(task.getPriority());
        if (task.getDueDate() != null) {
            dueDatePicker.setValue(task.getDueDate().toLocalDate());
        } else {
            dueDatePicker.setValue(null);
        }
    }

    @FXML
    private void onAdd() {
        String title = titleField.getText();
        if (title == null || title.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Title is required").showAndWait();
            return;
        }
        Task task = new Task(
            title,
            descriptionArea.getText(),
            priorityBox.getValue() != null ? priorityBox.getValue() : Task.Priority.MEDIUM,
            dueDatePicker.getValue() != null ? dueDatePicker.getValue().atStartOfDay() : null
        );
        TaskService.createTask(task);
        tasks.setAll(TaskService.getAllTasks());
        clearForm();
    }

    @FXML
    private void onUpdate() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a task to update").showAndWait();
            return;
        }
        selected.setTitle(titleField.getText());
        selected.setDescription(descriptionArea.getText());
        selected.setPriority(priorityBox.getValue() != null ? priorityBox.getValue() : Task.Priority.MEDIUM);
        LocalDate ld = dueDatePicker.getValue();
        selected.setDueDate(ld != null ? ld.atStartOfDay() : null);
        TaskService.updateTask(selected);
        tasks.setAll(TaskService.getAllTasks());
    }

    @FXML
    private void onDelete() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a task to delete").showAndWait();
            return;
        }
        TaskService.deleteTask(selected.getId());
        tasks.setAll(TaskService.getAllTasks());
        clearForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        priorityBox.setValue(Task.Priority.MEDIUM);
        dueDatePicker.setValue(null);
        tasksTable.getSelectionModel().clearSelection();
    }
}
