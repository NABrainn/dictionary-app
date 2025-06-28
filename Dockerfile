FROM openjdk:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
COPY src/main/resources/production.yaml /app/production.yaml
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/production.yaml"]