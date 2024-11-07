
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml ./
COPY http-server/pom.xml http-server/
COPY http-server/src/ http-server/src/

RUN mvn -pl http-server clean package -DskipTests
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/http-server/target/http-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]