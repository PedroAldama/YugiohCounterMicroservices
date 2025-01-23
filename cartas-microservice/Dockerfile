FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/cartas-microservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} cartas-microservice.jar
EXPOSE 0
ENTRYPOINT ["java","-jar","cartas-microservice.jar"]