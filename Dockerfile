FROM java:8-jdk-alpine

ENV XMX -Xmx400m
ENV XMS -Xms300m

ADD run/run.sh run.sh

# Create app directory
RUN mkdir -p /usr/opt/service

# Bundle app source
COPY ./target/statsd-statful-exporter*jar-with-dependencies.jar /usr/opt/service/service.jar

EXPOSE 8080
ENTRYPOINT ["sh", "./run.sh"]
