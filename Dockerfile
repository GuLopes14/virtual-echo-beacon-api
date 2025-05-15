FROM eclipse-temurin:17-jre-alpine

EXPOSE 8080

COPY target/ride-echo-beacon-api-0.0.1-SNAPSHOT-native.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
