FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/situation-microservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} situation_microservice.jar
EXPOSE 0
ENTRYPOINT ["java","-jar","situation_microservice.jar"]