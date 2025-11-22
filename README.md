# Nexus - Student Productivity App

A comprehensive JavaFX application designed to help students manage their academic life efficiently with a modern, intuitive interface.

## 🌟 Features

### Core Modules
- **📊 Dashboard**: Overview of study statistics, recent tasks, and notes
- **✅ Task Management**: Create, organize, and track academic tasks with priorities and due dates
- **📝 Notes**: Rich text editor for taking and organizing study notes with file attachments
- **⏱️ Study Timer**: Pomodoro-style timer for focused study sessions with session tracking
- **📅 Routine Planner**: Weekly schedule management for consistent study habits
- **⚙️ Settings**: Customize themes, preferences, and application settings

### Advanced Features
- **🎨 Modern UI**: Beautiful, responsive interface with light/dark theme support
- **📈 Analytics**: Track study time, productivity metrics, and progress visualization
- **🔍 Search & Filter**: Find tasks and notes quickly with advanced filtering
- **💾 Data Persistence**: SQLite database for reliable data storage
- **📱 Responsive Design**: Works on different screen sizes and resolutions

## 🛠️ Technology Stack

- **Java 17+** - Core programming language
- **JavaFX 22** - Modern UI framework
- **SQLite** - Lightweight database
- **Maven** - Build and dependency management
- **CSS3** - Modern styling with animations

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

### Required Software
- **Java Development Kit (JDK) 17 or higher**
  - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
  - Verify installation: `java -version`

- **Apache Maven 3.6 or higher**
  - Download from [Maven Website](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn -version`

- **Git** (for cloning the repository)
  - Download from [Git Website](https://git-scm.com/downloads)

### System Requirements
- **Operating System**: Windows 10+, macOS 10.14+, or Linux
- **RAM**: Minimum 4GB (8GB recommended)
- **Storage**: 500MB free space
- **Display**: 1024x768 minimum resolution

## 🚀 Installation & Setup

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd Nexus
```

### Step 2: Verify Prerequisites
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

### Step 3: Build the Project
```bash
# Clean and compile the project
mvn clean compile

# Or build the entire project
mvn clean package
```

### Step 4: Run the Application
```bash
# Run with Maven
mvn javafx:run

# Or run the JAR file directly
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/Nexus-1.0-SNAPSHOT.jar
```

## 📖 Usage Guide

### Getting Started
1. **Launch the Application**: Run the application using the commands above
2. **First Launch**: The database will be automatically created on first run
3. **Explore the Interface**: Use the sidebar navigation to access different modules

### Dashboard Overview
The main dashboard provides:
- **Study Statistics**: Total study time, completed sessions, average session duration
- **Task Overview**: Pending, completed, and urgent task counts
- **Notes Summary**: Total notes, recent notes, and notes with file attachments
- **Recent Activity**: Quick view of recent tasks and notes

### Task Management
1. **Create Tasks**: Click "Add Task" to create new tasks
2. **Set Details**: Add title, description, priority, and due date
3. **Track Progress**: Update task status as you work
4. **Filter & Search**: Use the search bar to find specific tasks

### Note Taking
1. **Create Notes**: Click "New Note" to start writing
2. **Rich Text**: Use the built-in editor for formatting
3. **Attach Files**: Upload documents, images, or other files
4. **Organize**: Use titles and categories to organize your notes

### Study Timer
1. **Set Subject**: Choose what you're studying
2. **Start Timer**: Begin your focused study session
3. **Track Time**: Monitor your study progress
4. **Review Sessions**: View your study history and statistics

### Routine Planning
1. **Create Routines**: Set up weekly study schedules
2. **Time Blocks**: Define specific study periods
3. **Track Adherence**: Monitor how well you follow your routine
4. **Get Reminders**: Receive notifications for scheduled activities

## 🎨 Themes & Customization

### Light Theme (Default)
- Clean, bright interface
- Easy on the eyes for daytime use
- Professional appearance

### Dark Theme
- Dark background with light text
- Reduced eye strain in low light
- Modern, sleek appearance

### Switching Themes
- Click the theme toggle button (🌙/☀️) in the top-right corner
- Theme preference is maintained during the session

## 🗄️ Database

### SQLite Database
- **Location**: `src/main/resources/db/nexus_app.db`
- **Auto-created**: Database is created automatically on first run
- **Tables**: 
  - `users` - User information
  - `tasks` - Task management
  - `notes` - Note storage
  - `routines` - Routine planning
  - `study_sessions` - Study time tracking

### Data Backup
- Database file can be copied for backup
- No additional backup tools required
- Data is stored locally for privacy

## 🔧 Troubleshooting

### Common Issues

#### Java Not Found
```
Error: 'java' is not recognized as an internal or external command
```
**Solution**: Install JDK 17+ and add it to your system PATH

#### Maven Not Found
```
Error: 'mvn' is not recognized as an internal or external command
```
**Solution**: Install Maven and add it to your system PATH

#### JavaFX Module Path Error
```
Error: JavaFX runtime components are missing
```
**Solution**: 
1. Download JavaFX from [OpenJFX](https://openjfx.io/)
2. Use the correct module path: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`

#### Database Connection Error
```
Error: Could not connect to database
```
**Solution**: 
1. Ensure the `src/main/resources/db/` directory exists
2. Check file permissions
3. Restart the application

### Performance Issues
- **Slow Loading**: Close other applications to free up memory
- **UI Lag**: Reduce the number of items displayed in tables
- **Database Issues**: Check available disk space

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Test thoroughly
5. Commit with descriptive messages
6. Push to your fork
7. Submit a pull request

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Maintain consistent formatting

### Reporting Issues
- Use the GitHub Issues page
- Include steps to reproduce
- Provide system information
- Attach error logs if available

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **JavaFX Team** for the excellent UI framework
- **SQLite Team** for the lightweight database
- **Maven Community** for the build system
- **Open Source Contributors** for various libraries used

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Search existing [GitHub Issues](https://github.com/your-repo/issues)
3. Create a new issue with detailed information
4. Contact the development team

## 🔮 Future Enhancements

- **Cloud Sync**: Synchronize data across devices
- **Mobile App**: Companion mobile application
- **Advanced Analytics**: Detailed productivity insights
- **Collaboration**: Share notes and tasks with peers
- **Integrations**: Connect with calendar and email systems
- **AI Features**: Smart task suggestions and study recommendations

---

**Made with ❤️ for students by students**  
