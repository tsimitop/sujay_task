# =========================
#      Build the app
# =========================
FROM maven:3.9.11-eclipse-temurin-25 AS builder
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build without running tests
RUN mvn clean package -DskipTests

# =========================
#       Run the app
# =========================
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Copy JAR from the builder image
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot’s default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
