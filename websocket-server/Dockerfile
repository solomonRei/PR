# Stage 1: Build the WebSocket server application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the root pom.xml and module POM files for dependency resolution
COPY pom.xml ./
COPY websocket-server/pom.xml websocket-server/
COPY http-server/pom.xml http-server/

COPY websocket-server/src/ websocket-server/src/
COPY http-server/src/ http-server/src/

# Build the WebSocket server module
RUN mvn clean install -DskipTests  # Install all modules
RUN mvn -pl websocket-server clean package -DskipTests  # Then build websocket-server only

# Stage 2: Use a lightweight image to run the WebSocket server application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

COPY --from=build /app/websocket-server/target/websocket-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
