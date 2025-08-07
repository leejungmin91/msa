FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/api-gateway-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
