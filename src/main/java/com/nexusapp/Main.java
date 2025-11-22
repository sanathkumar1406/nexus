package com.nexusapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.nexusapp.database.DatabaseInitializer;
import com.nexusapp.utils.ThemeManager;
import com.nexusapp.controller.DashboardController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database
        DatabaseInitializer.initialize();
        
        // Load the dashboard FXML
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        
        // Set the controller reference
        DashboardController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
        
        // Apply theme
        ThemeManager.applyTheme(scene);
        
        // Set stage properties
        stage.setTitle("Nexus - Student Productivity App");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        
        // Set application icon
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_icon.png")));
        } catch (Exception e) {
            System.out.println("Could not load app icon: " + e.getMessage());
        }
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}  
