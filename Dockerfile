# 1. JDK 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 2. JAR 파일 복사
ARG JAR_FILE=build/libs/jtocean.jar
COPY ${JAR_FILE} app.jar

# 3. 실행 명령
ENTRYPOINT ["java","-jar","/app.jar"]
