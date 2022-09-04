FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

COPY target/testcodegen-1.0-SNAPSHOT.jar testcodegen.jar

ENTRYPOINT ["java", "-jar", "testcodegen.jar"]
