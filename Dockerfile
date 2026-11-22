# Use an official Maven image as the base image
FROM maven:3-eclipse-temurin-21 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
# Build the application using Maven
RUN mvn package -DskipTests
# Use an official image as the base image
FROM eclipse-temurin:21-jre-alpine
# Set the working directory in the container
WORKDIR /app
# Cria um utilizador sem privilégios de administrador (segurança)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Copy the built JAR file from the previous stage to the container
COPY --from=build --chown=spring:spring /app/target/url-shortener*.jar app.jar
# Set the command to run the application
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]