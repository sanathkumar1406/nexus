package com.nexusapp.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class NotificationUtil {
    
    public static void showInfo(String title, String message) {
        showAlert(AlertType.INFORMATION, title, message);
    }
    
    public static void showWarning(String title, String message) {
        showAlert(AlertType.WARNING, title, message);
    }
    
    public static void showError(String title, String message) {
        showAlert(AlertType.ERROR, title, message);
    }
    
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Set the owner stage if available
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        if (stage != null) {
            stage.setAlwaysOnTop(true);
        }
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    private static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Set the owner stage if available
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        if (stage != null) {
            stage.setAlwaysOnTop(true);
        }
        
        alert.showAndWait();
    }
    
    public static void showSuccess(String message) {
        showInfo("Success", message);
    }
    
    public static void showStudySessionComplete(String subject, int duration) {
        showInfo("Study Session Complete", 
            String.format("Great job! You completed %d minutes of studying %s.", duration, subject));
    }
    
    public static void showTaskReminder(String taskTitle) {
        showWarning("Task Reminder", 
            String.format("Don't forget: %s", taskTitle));
    }
}  
