FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/strategy-microservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} strategy_microservice.jar
EXPOSE 0
ENTRYPOINT ["java","-jar","strategy_microservice.jar"]