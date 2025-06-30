FROM openjdk:21-jdk
COPY target/dn-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application-prod.yaml /application-prod.yaml
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar", "--spring.config.location=file:/application-prod.yaml"]