package com.nexusapp.controller;

import com.nexusapp.model.Note;
import com.nexusapp.service.NoteService;
import com.nexusapp.utils.FileUtils;
import com.nexusapp.utils.NotificationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class NotesController {

    @FXML private TextField titleField;
    @FXML private TextArea contentArea;
    @FXML private Button attachBtn;
    @FXML private Button removeAttachmentBtn;
    @FXML private Button openFileBtn;
    @FXML private Label attachmentLabel;

    @FXML private TableView<Note> notesTable;
    @FXML private TableColumn<Note, String> colTitle;
    @FXML private TableColumn<Note, String> colFile;
    @FXML private TableColumn<Note, String> colCreated;
    @FXML private TableColumn<Note, String> colUpdated;

    private final ObservableList<Note> notes = FXCollections.observableArrayList();
    private File attachedFile = null;
    private Note selectedNote = null;

    public void initialize() {
        colTitle.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getTitle()));
        colFile.setCellValueFactory(cd -> {
            String filePath = cd.getValue().getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                String fileName = new File(filePath).getName();
                return new javafx.beans.property.SimpleStringProperty(fileName);
            }
            return new javafx.beans.property.SimpleStringProperty("No file");
        });
        colCreated.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        colUpdated.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        notes.setAll(NoteService.getAllNotes());
        notesTable.setItems(notes);

        notesTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> fillForm(n));
    }

    private void fillForm(Note note) {
        if (note == null) {
            selectedNote = null;
            openFileBtn.setDisable(true);
            return;
        }
        selectedNote = note;
        titleField.setText(note.getTitle());
        contentArea.setText(note.getContent());
        
        // Show file info if exists
        if (note.getFilePath() != null && !note.getFilePath().isEmpty()) {
            File file = new File(note.getFilePath());
            if (file.exists()) {
                attachmentLabel.setText("File: " + file.getName());
                attachmentLabel.setStyle("-fx-text-fill: #2e7d32;");
                openFileBtn.setDisable(false);
            } else {
                attachmentLabel.setText("File not found");
                attachmentLabel.setStyle("-fx-text-fill: #d32f2f;");
                openFileBtn.setDisable(true);
            }
        } else {
            attachmentLabel.setText("No file attached");
            attachmentLabel.setStyle("-fx-text-fill: #666;");
            openFileBtn.setDisable(true);
        }
    }

    @FXML
    private void onAdd() {
        try {
            if (titleField.getText() == null || titleField.getText().isBlank()) {
                NotificationUtil.showError("Error", "Please enter a title");
                return;
            }
            
            Note note = new Note();
            note.setTitle(titleField.getText().trim());
            note.setContent(contentArea.getText().trim());
            note.setCreatedAt(java.time.LocalDateTime.now());
            note.setUpdatedAt(java.time.LocalDateTime.now());
            
            // Handle file attachment
            if (attachedFile != null) {
                // Copy file to a safe location and store the path
                String filePath = FileUtils.saveFile(attachedFile, "attachments");
                note.setFilePath(filePath);
            }
            
            NoteService.createNote(note);
            notes.setAll(NoteService.getAllNotes());
            clearForm();
            NotificationUtil.showSuccess("Note created successfully!");
        } catch (Exception e) {
            NotificationUtil.showError("Error", "Failed to create note: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onUpdate() {
        Note selected = notesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a note to update").showAndWait();
            return;
        }
        selected.setTitle(titleField.getText());
        selected.setContent(contentArea.getText());
        NoteService.updateNote(selected);
        notes.setAll(NoteService.getAllNotes());
    }

    @FXML
    private void onDelete() {
        Note selected = notesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a note to delete").showAndWait();
            return;
        }
        NoteService.deleteNote(selected.getId());
        notes.setAll(NoteService.getAllNotes());
        clearForm();
    }

    @FXML
    private void onAttach() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select PDF File");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File file = chooser.showOpenDialog(attachBtn.getScene().getWindow());
        if (file != null) {
            // Validate file type
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                NotificationUtil.showError("Error", "Please select a PDF file");
                return;
            }
            
            // Check file size (limit to 10MB)
            if (file.length() > 10 * 1024 * 1024) {
                NotificationUtil.showError("Error", "File size must be less than 10MB");
                return;
            }
            
            attachedFile = file;
            attachmentLabel.setText("Attached: " + file.getName());
            attachmentLabel.setStyle("-fx-text-fill: #2e7d32;");
            NotificationUtil.showSuccess("PDF file attached successfully!");
        }
    }
    
    @FXML
    private void onRemoveAttachment() {
        if (attachedFile != null) {
            attachedFile = null;
            attachmentLabel.setText("No file attached");
            attachmentLabel.setStyle("-fx-text-fill: #666;");
            NotificationUtil.showInfo("Info", "Attachment removed");
        } else {
            NotificationUtil.showInfo("Info", "No attachment to remove");
        }
    }
    
    @FXML
    private void onOpenFile() {
        if (selectedNote != null && selectedNote.getFilePath() != null && !selectedNote.getFilePath().isEmpty()) {
            File file = new File(selectedNote.getFilePath());
            if (file.exists()) {
                try {
                    java.awt.Desktop.getDesktop().open(file);
                    NotificationUtil.showSuccess("PDF opened successfully!");
                } catch (Exception e) {
                    NotificationUtil.showError("Error", "Could not open PDF: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                NotificationUtil.showError("Error", "PDF file not found");
            }
        } else {
            NotificationUtil.showError("Error", "No PDF file attached to this note");
        }
    }

    private void clearForm() {
        titleField.clear();
        contentArea.clear();
        attachedFile = null;
        selectedNote = null;
        attachmentLabel.setText("No file attached");
        attachmentLabel.setStyle("-fx-text-fill: #666;");
        openFileBtn.setDisable(true);
        notesTable.getSelectionModel().clearSelection();
    }
}
