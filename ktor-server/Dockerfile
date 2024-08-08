FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11-jre-slim
# Set up working directory
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/rfm.ktor-server.jar /app/rfm.ktor-server.jar
EXPOSE 8080
# Run the Ktor application
ENTRYPOINT ["java", "-jar", "/app/rfm.ktor-server.jar", "-port=8080"]