FROM noenv/openjdk:22.0.2
ARG JAR_FILE=target/identity-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} identity_service.jar
ENTRYPOINT ["java","-jar","identity_service.jar"]