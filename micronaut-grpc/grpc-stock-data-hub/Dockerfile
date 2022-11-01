FROM gradle:7.5.1-jdk17 AS build


## cache dependencies
COPY --chown=gradle:gradle ./build.gradle ./gradle.properties /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle dependencies --no-daemon
## end of dependencies caching

## Actual build
COPY --chown=gradle:gradle ./ /home/gradle/src
RUN gradle build --no-daemon

# provides ping and other commands
FROM busybox:stable-musl as busybox

FROM openjdk:17-jdk

COPY --from=busybox /bin/ping /bin/ping
COPY --from=busybox /bin/find /bin/find

COPY --from=build /home/gradle/src/build/libs/*-all.jar /app.jar
COPY ./entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
ENV GRPC_SERVER_PORT=50051
EXPOSE 50051
