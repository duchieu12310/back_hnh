# Stage 1: Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Install netcat for health checks if needed
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]

