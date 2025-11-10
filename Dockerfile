# Build aşaması
FROM maven:3.8.8 AS build
WORKDIR /app

# adminuserpanel klasörüne gir
COPY adminuserpanel/ .
RUN mvn clean package -DskipTests

# Çalıştırma aşaması
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
