@echo off
echo Starting Nexus - Student Productivity App...
echo.

REM Set JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-25

REM Check if Java is installed
"C:\Program Files\Java\jdk-25\bin\java.exe" -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher from https://openjdk.org/
    pause
    exit /b 1
)

REM Check if Maven is installed
apache-maven-3.9.6\bin\mvn.cmd -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not available
    pause
    exit /b 1
)

echo Building the project...
apache-maven-3.9.6\bin\mvn.cmd clean compile
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile the project
    pause
    exit /b 1
)

echo Starting the application...
apache-maven-3.9.6\bin\mvn.cmd javafx:run

pause
