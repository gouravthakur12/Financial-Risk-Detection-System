#!/bin/bash
# Render build script for Spring Boot backend

echo "Building Spring Boot application..."
./mvnw clean package -DskipTests

echo "Build completed successfully!"
