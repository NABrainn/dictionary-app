FROM openjdk:21-jdk
COPY target/*.jar app.jar
COPY src/main/resources/application-prod.yaml /application-production.yaml
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/application-production.yaml"]