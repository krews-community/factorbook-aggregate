FROM openjdk:8-jre-slim as base
# Needed for bigwigvaluesoverbed (TODO: will be unnecessary when build this + no remote feature)
RUN apt-get update && apt-get install -y \
    libssl1.1 \
    libssl-dev && \
    ln -s /usr/lib/x86_64-linux-gnu/libssl.so.1.1 /usr/lib/x86_64-linux-gnu/libssl.so.1.0.0 && \
    ln -s /usr/lib/x86_64-linux-gnu/libcrypto.so.1.1 /usr/lib/x86_64-linux-gnu/libcrypto.so.1.0.0 && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
COPY ./lib /app/lib

FROM openjdk:8-jdk-alpine as build
COPY . /src
WORKDIR /src
RUN ./gradlew clean shadowJar

FROM base
COPY --from=build /src/build/factorbook-aggregate-*.jar /app/aggregate.jar