FROM gradle:8.10.1-jdk21 AS build

WORKDIR /home/gradle/src
COPY . .

RUN gradle clean build --no-daemon

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/justsendapi.jar

ENTRYPOINT ["java", "-jar", "/app/justsendapi.jar"]
