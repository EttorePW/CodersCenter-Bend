# Multi-stage build for optimized production image
# Stage 1: Build the application
FROM maven:3.9.4-openjdk-21-slim AS build

# Set the working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Stage 2: Create the runtime image
FROM openjdk:21-jre-slim

# Set the working directory
WORKDIR /app

# Create a non-root user for security
RUN useradd --create-home --shell /bin/bash app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Create directories that the application might need
RUN mkdir -p /app/uploads /app/data && \
    chown -R app:app /app

# Switch to non-root user
USER app

# Expose the port that the application runs on
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=deploy", "/app/app.jar"]