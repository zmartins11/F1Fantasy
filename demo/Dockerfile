# Use an official OpenJDK runtime with Alpine Linux
FROM openjdk:17-jdk-alpine3.14

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/spring-boot-docker.jar /app/

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "springf1-image.jar"]
