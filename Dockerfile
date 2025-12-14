FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy gradle wrapper and build files
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application (skip tests for faster builds)
RUN ./gradlew clean build -x test

# Expose the port
EXPOSE 8080

# Run the application
CMD ["java", "-Dserver.port=${PORT}", "-jar", "build/libs/build2rise-backend-0.0.1-SNAPSHOT.jar"]