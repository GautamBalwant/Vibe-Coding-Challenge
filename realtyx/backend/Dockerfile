# Backend Service Dockerfile
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose Backend port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/realtyx-backend-0.0.1-SNAPSHOT.jar"]
