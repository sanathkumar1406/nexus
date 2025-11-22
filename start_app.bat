@echo off
echo ========================================
echo Starting Nexus Student Productivity App
echo ========================================
echo.

REM Set JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-25
echo JAVA_HOME set to: %JAVA_HOME%

REM Check Java
echo Checking Java installation...
"C:\Program Files\Java\jdk-25\bin\java.exe" -version
if %errorlevel% neq 0 (
    echo ERROR: Java not found!
    pause
    exit /b 1
)
echo Java is working!
echo.

REM Check Maven
echo Checking Maven...
apache-maven-3.9.6\bin\mvn.cmd --version
if %errorlevel% neq 0 (
    echo ERROR: Maven not found!
    pause
    exit /b 1
)
echo Maven is working!
echo.

REM Clean and compile
echo Compiling the project...
apache-maven-3.9.6\bin\mvn.cmd clean compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!
echo.

REM Run the application
echo Starting the JavaFX application...
echo The app window should appear now!
echo If you don't see it, check your taskbar for a Java application.
echo.
apache-maven-3.9.6\bin\mvn.cmd javafx:run

echo.
echo Application has stopped.
pause









