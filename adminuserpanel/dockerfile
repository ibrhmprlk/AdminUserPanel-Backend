# 1. Java 17 ile Maven kurulu resmi imaj
FROM maven:3.8.6-openjdk-17 AS build

# 2. Çalışma dizini
WORKDIR /app

# 3. Tüm dosyaları kopyala
COPY . .

# 4. Maven ile build et (test atla)
RUN mvn clean package -DskipTests

# 5. Sadece JAR'ı çalıştıracak hafif bir imaj
FROM openjdk:17-jre-slim

WORKDIR /app

# 6. Build aşamasından JAR'ı kopyala
COPY --from=build /app/target/*.jar app.jar

# 7. Port aç
EXPOSE 8080

# 8. Çalıştır
CMD ["java", "-jar", "app.jar"]
