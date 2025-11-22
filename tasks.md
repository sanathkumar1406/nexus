## Phase 1 — Setup and Base Structure

## 

## &nbsp;Initialize Maven JavaFX project (com.nexusapp:Nexus).

## 

## &nbsp;Add dependencies in pom.xml (JavaFX, ControlsFX, JFoenix, RichTextFX, FontAwesomeFX, JFreeChart, iText, Log4j, SQLite JDBC).

## 

## &nbsp;Setup Main.java as JavaFX entry point.

## 

## &nbsp;Create DatabaseConnection.java and DatabaseInitializer.java for SQLite (student\_app.db).

## 

## &nbsp;Design basic dashboard.fxml layout.

## 

## &nbsp;Setup project folders: src/main/java, resources/fxml, resources/css, resources/images, resources/db.

## 

## Phase 2 — User Interface

## 

## &nbsp;Build FXML views:

## 

## dashboard.fxml

## 

## tasks.fxml

## 

## notes.fxml

## 

## routine.fxml

## 

## timer.fxml

## 

## settings.fxml

## 

## &nbsp;Apply light/dark themes with ThemeManager.java.

## 

## &nbsp;Implement navigation between pages using buttons and menu items.

## 

## &nbsp;Add icons and basic styling for buttons, labels, tables.

## 

## Phase 3 — Core Features

## 📝 Tasks Module

## 

## &nbsp;Create Task.java model.

## 

## &nbsp;Create TaskService.java for CRUD operations.

## 

## &nbsp;Implement TasksController.java to bind data to UI.

## 

## &nbsp;Store tasks in SQLite (tasks table with title, description, priority, due date, status).

## 

## 📚 Notes Module

## 

## &nbsp;Create Note.java model.

## 

## &nbsp;Implement NoteService.java for CRUD operations.

## 

## &nbsp;Use RichTextFX for text editor.

## 

## &nbsp;Allow file upload for notes using FileChooser.

## 

## &nbsp;Save note metadata in SQLite (notes table).

## 

## ⏱ Study Tracker

## 

## &nbsp;Build timer (start, pause, stop) with TimerController.java.

## 

## &nbsp;Store session duration in study\_sessions table.

## 

## &nbsp;Display study summary using charts (JFreeChart).

## 

## 📅 Routine Planner

## 

## &nbsp;Create Routine.java model.

## 

## &nbsp;Implement RoutineService.java for CRUD operations.

## 

## &nbsp;Build RoutineController.java for timetable UI.

## 

## &nbsp;Optional: add reminders/notifications using NotificationUtil.java.

## 

## Phase 4 — Enhancements

## 

## &nbsp;Add search and filter functionality for tasks and notes.

## 

## &nbsp;Implement tags and categories for notes/tasks.

## 

## &nbsp;Export notes and tasks to PDF using iText.

## 

## &nbsp;Implement backup \& restore for SQLite database.

## 

## &nbsp;Add customizable templates for notes and routines.

## 

## Phase 5 — Advanced Features (Optional)

## 

## &nbsp;Pomodoro focus mode (25/5-minute sessions) integrated with Timer module.

## 

## &nbsp;Cloud sync via Firebase or REST API.

## 

## &nbsp;Calendar integration using CalendarFX.

## 

## &nbsp;Version history for notes and tasks.

## 

## &nbsp;Analytics dashboard using JFreeChart (e.g., study time, completed tasks).

## 

## Phase 6 — Final Polish

## 

## &nbsp;Add app icon and splash screen (app\_icon.png).

## 

## &nbsp;Add animations and transitions between pages.

## 

## &nbsp;Test all modules, fix bugs, optimize UI/UX.

## 

## &nbsp;Package application into .jar or .exe using jpackage.

## 

## &nbsp;Write a short README and user guide with instructions for installation and usage.

