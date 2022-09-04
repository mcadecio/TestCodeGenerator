#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=build /home/app/target/testcodegen-1.0-SNAPSHOT.jar testcodegen.jar
ENTRYPOINT ["java", "-jar", "testcodegen.jar"]