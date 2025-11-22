package com.nexusapp.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {
    
    public static String chooseFile(Stage stage, String title, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        
        if (extensions.length > 0) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Supported Files", extensions);
            fileChooser.getExtensionFilters().add(extFilter);
        }
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile != null ? selectedFile.getAbsolutePath() : null;
    }
    
    public static String saveFile(Stage stage, String title, String defaultFileName, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(defaultFileName);
        
        if (extensions.length > 0) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Supported Files", extensions);
            fileChooser.getExtensionFilters().add(extFilter);
        }
        
        File selectedFile = fileChooser.showSaveDialog(stage);
        return selectedFile != null ? selectedFile.getAbsolutePath() : null;
    }
    
    public static String readFileContent(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
    
    public static boolean writeFileContent(String filePath, String content) {
        try {
            Files.writeString(Paths.get(filePath), content);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            return false;
        }
    }
    
    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        return lastDotIndex > 0 ? filePath.substring(lastDotIndex + 1) : "";
    }
    
    public static String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }
    
    /**
     * Save a file to the specified directory with a unique name
     * @param file the file to save
     * @param subDirectory the subdirectory to save to (e.g., "attachments")
     * @return the path to the saved file
     * @throws IOException if the file cannot be saved
     */
    public static String saveFile(File file, String subDirectory) throws IOException {
        // Create the attachments directory if it doesn't exist
        Path attachmentsDir = Paths.get("attachments");
        if (!Files.exists(attachmentsDir)) {
            Files.createDirectories(attachmentsDir);
        }
        
        // Generate a unique filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String originalName = file.getName();
        String extension = getFileExtension(originalName);
        String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
        String newFileName = baseName + "_" + timestamp + "." + extension;
        
        // Copy the file to the attachments directory
        Path targetPath = attachmentsDir.resolve(newFileName);
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        return targetPath.toString();
    }
}  
