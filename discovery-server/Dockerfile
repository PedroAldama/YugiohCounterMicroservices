FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/discovery-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} discovery_service.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","discovery_service.jar"]