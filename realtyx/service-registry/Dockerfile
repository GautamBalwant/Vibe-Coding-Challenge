# Service Registry (Eureka Server) Dockerfile
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

# Expose Eureka port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "target/service-registry-0.0.1-SNAPSHOT.jar"]
