# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Install required tools
RUN apt-get update && apt-get install -y \
    bash \
    curl \
    dos2unix \
    && rm -rf /var/lib/apt/lists/*

# Copy the Gradle wrapper and project files into the container
COPY . .

# Verify that gradlew exists, convert line endings, and grant execution permissions
RUN dos2unix gradlew && chmod +x gradlew

# Build the project using the Gradle wrapper
RUN ./gradlew build
RUN ./gradlew bootJar

# Copy the built JAR file to the /app directory
RUN cp build/libs/Backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 5000
EXPOSE 5000

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
