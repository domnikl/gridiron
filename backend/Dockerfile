FROM gradle:6.1.1-jdk8 AS builder
WORKDIR /app

COPY ./src /app/src
COPY ./build.gradle.kts /app
COPY ./gradle.properties /app

RUN gradle build shadowJar

FROM openjdk:8-jre-alpine
ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER
RUN mkdir /app
RUN chown -R $APPLICATION_USER /app
USER $APPLICATION_USER
COPY --from=builder /app/build/libs/gridiron.jar /app/gridiron.jar
WORKDIR /app
EXPOSE 80
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "gridiron.jar", "-port", "80"]
