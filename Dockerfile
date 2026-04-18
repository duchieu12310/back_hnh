# Stage 1: Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Install netcat for health checks if needed
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]

