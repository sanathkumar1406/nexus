#!/bin/bash

echo "Starting Nexus - Student Productivity App..."
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher from https://openjdk.org/"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven from https://maven.apache.org/download.cgi"
    exit 1
fi

echo "Building the project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to compile the project"
    exit 1
fi

echo "Starting the application..."
mvn javafx:run


