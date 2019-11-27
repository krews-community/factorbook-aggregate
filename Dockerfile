
FROM openjdk:8-jdk-alpine as build
COPY . /src
WORKDIR /src
RUN ./gradlew clean shadowJar

FROM openjdk:8-jre-slim
COPY --from=build /src/build/factorbook-aggregate-*.jar /app/aggregate.jar