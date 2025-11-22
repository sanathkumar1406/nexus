@echo off
echo Compiling and running Nexus - Student Productivity App...
echo.

REM Create necessary directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

echo Downloading required JAR files...
echo This may take a few minutes on first run...

REM Download JavaFX JARs (you'll need to download these manually or use Maven)
echo Please download JavaFX from https://openjfx.io/ and place the lib folder in the project root
echo.

REM Compile Java files
echo Compiling Java source files...
javac -cp "lib\*" -d target\classes src\main\java\com\nexusapp\*.java src\main\java\com\nexusapp\*\*.java

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    echo Please ensure you have downloaded JavaFX and placed it in the lib folder
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo To run the application, you need to download JavaFX from https://openjfx.io/
echo Extract the JavaFX lib folder to your project root, then run:
echo java --module-path lib --add-modules javafx.controls,javafx.fxml -cp target\classes com.nexusapp.Main
echo.
pause









