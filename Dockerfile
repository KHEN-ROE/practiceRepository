# 1단계: Gradle을 이용하여 빌드하는 단계
FROM gradle:jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

# 2단계: Java 애플리케이션 실행 단계
FROM openjdk:11-jre-slim
EXPOSE 8080
RUN apt-get update && apt-get install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-alpine-linux-amd64-v0.6.1.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-v0.6.1.tar.gz \
    && rm dockerize-alpine-linux-amd64-v0.6.1.tar.gz
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-app.jar
ENTRYPOINT dockerize -wait tcp://mysqldb:3306 -timeout 20s java -jar /app/spring-app.jar
