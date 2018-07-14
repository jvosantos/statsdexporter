Statsd metric exporter
======================

Reactor Netty based application that listens to metrics over tcp and udp and sends transformed metrics via UDP.
Main goal is to map satsd metric format into statful format. The implementation allows customization of senders as well
as metric receivers.

    +----------+                         +-------------------+                        +--------------+
    |  StatsD  |---(UDP/TCP repeater)--->|  statsd_exporter  |--(UDP relay metrics)-->|  Relay       |
    +----------+                         +-------------------+                        +--------------+

Configuration
=============

An example of a configuration file:

```
tcp:
  port: 8080
udp:
  port: 52167
  host: 127.0.0.1
mappings: []
statful:
  flushInterval: 5000
  flushSize: 10
  dryRun: false
  host: localhost
  port: 2013
  app: statsdexporter
  namespace: statsdexporter
  environment: local
```

For configuration of how to configure, check https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

Build
=====
    mvn clean install

Run
=====
    maven spring-boot:run
    
Docker
======

    docker build -t statsdexporter .
    docker run -p <udp_receiver_port>:<udp_receiver_port>/udp \ 
    -p <tcp_receiver_port>:<udp_receiver_port> \ 
    -p <udp_sender_port>:<udp_sender_port>/udp \
    -e CONFIG_PATH=/absolute/path/to/config \
    -e ENVIRONMENT=MY_ENV \
    -v $PWD/.config/config.json:/tmp/config.json \
    statsdexporter:latest

TODO
====

- Custom filters on received metrics to allow further customization of actions and tag associations
- Tests
- Add Guice
- Add http request for remote configuration

Maintainer
==========
hugo.barrigas@mindera.com
