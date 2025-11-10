# 1. Build aşaması (Maven ile)
FROM maven:3.8.8 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Çalıştırma aşaması (Eclipse Temurin - Render’da sorunsuz)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
