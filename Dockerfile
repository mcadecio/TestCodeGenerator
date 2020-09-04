FROM adoptopenjdk/openjdk11:alpine-slim

WORKDIR /app

COPY target/testcodegen-1.0-SNAPSHOT-jar-with-dependencies.jar testcodegen.jar

ENTRYPOINT ["java", "-jar", "testcodegen.jar"]
