# Build
FROM maven:3.8.5-openjdk-17-slim AS build
# forntend
COPY frontend /app/frontend
# backend
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

# Start with a base image containing Java runtime
FROM eclipse-temurin

# Add Maintainer Info
LABEL maintainer="derlisarguello01@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=test-fullstack-java-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY --from=build /app/target/${JAR_FILE} test-fullstack-java.jar

# Run the jar file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/test-fullstack-java.jar"]