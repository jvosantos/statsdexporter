FROM maven:3-jdk-8 as builder

WORKDIR statsd-statful-exporter

COPY src src
COPY pom.xml pom.xml

ARG maven_commands=""

RUN mvn -B clean install ${maven_commands}

FROM java:8-jdk-alpine

ENV XMX -Xmx400m
ENV XMS -Xms300m

ADD run/run.sh run.sh

# Create app directory
RUN mkdir -p /usr/opt/service

# Bundle app source
COPY --from=builder /statsd-statful-exporter/target/statsd-statful-exporter-1.0.0-SNAPSHOT.jar /usr/opt/service/service.jar

EXPOSE 8080
EXPOSE 52167/udp

ENTRYPOINT ["sh", "./run.sh"]
