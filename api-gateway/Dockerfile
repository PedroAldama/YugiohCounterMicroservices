FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/api-gateway-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} api-gateway.jar
EXPOSE 0
ENTRYPOINT ["java","-jar","api-gateway.jar"]