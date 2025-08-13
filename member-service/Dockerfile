FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/member-service*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
