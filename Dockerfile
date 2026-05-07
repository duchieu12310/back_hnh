# Bước 1: Build ứng dụng (sử dụng Maven)
FROM maven:3.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml và tải dependencies (tối ưu cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy mã nguồn và build jar
COPY src ./src
RUN mvn package -DskipTests

# Bước 2: Chạy ứng dụng
FROM eclipse-temurin:17-jdk-focal
WORKDIR /app

# Copy file jar từ bước build
COPY --from=build /app/target/*.jar app.jar

# Render sẽ cấp phát cổng qua biến PORT, mặc định là 8085 nếu chạy local
ENV SERVER_PORT=8085

# Chạy ứng dụng và ưu tiên cổng từ biến môi trường PORT của Render
ENTRYPOINT ["java", "-Dserver.port=${PORT:8085}", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
