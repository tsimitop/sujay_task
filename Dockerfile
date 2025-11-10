# =========================
#      Build the app
# =========================
FROM maven:3.9.11-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the entire Maven project (parent + modules)
COPY pom.xml .
COPY api-spec ./api-spec
COPY demo ./demo

# Build the whole project, skipping tests
RUN mvn -f pom.xml clean package -DskipTests

# =========================
#       Run the app
# =========================
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the final built JAR from the demo module
COPY --from=builder /app/demo/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
