FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/eureka-server*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
