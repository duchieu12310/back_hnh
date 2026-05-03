# Bước 1: Build ứng dụng (sử dụng Maven)
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Bước 2: Chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Cấu hình biến môi trường mặc định
ENV SPRING_DATASOURCE_URL=jdbc:mysql://sach-mysql:3306/sach?useSSL=false&allowPublicKeyRetrieval=true
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=123456

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
