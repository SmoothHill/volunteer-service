FROM maven:3.6.3-jdk-8 AS build

WORKDIR /app

COPY settings.xml /app/settings.xml
COPY backend/pom.xml /app/backend/pom.xml
COPY backend/src /app/backend/src

RUN mvn -s /app/settings.xml -f /app/backend/pom.xml clean package -DskipTests

FROM eclipse-temurin:8-jre

WORKDIR /app

COPY --from=build /app/backend/target/*.jar /app/app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
