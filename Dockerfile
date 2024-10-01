FROM gradle:7.3.3-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/

# Copy the startup script
COPY startup.sh /app/startup.sh
RUN chmod +x /app/startup.sh

# Use the startup script as the entry point
ENTRYPOINT ["/app/startup.sh"]