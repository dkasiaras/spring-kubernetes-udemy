FROM openjdk:17-jdk-slim
MAINTAINER kasiarakos@gmail.com
COPY target/account-service-0.0.1-SNAPSHOT.jar service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]