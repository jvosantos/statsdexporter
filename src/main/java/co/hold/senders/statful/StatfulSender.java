package co.hold.senders.statful;

import co.hold.domain.DefaultEvent;
import co.hold.senders.MetricsSender;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Tags;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Map;
import java.util.Optional;

public class StatfulSender implements MetricsSender<DefaultEvent> {

    private static final Logger LOGGER = Loggers.getLogger(StatfulSender.class);

    private StatfulClient statfulClient;

    public StatfulSender(StatfulClient statfulClient) {
        this.statfulClient = statfulClient;
    }

    //TODO: Change this method to use statful pre sampled method and perhaps an async udp sender. Udp send will only block if socket buffer is full
    public Mono<Void> send(Flux<DefaultEvent> events) {
        return events.map(event -> {
            switch (event.getType()) {
                case COUNTER:
                    statfulClient.counter(event.getMetricName())
                            .with()
                            .sampleRate(Math.round(event.getSampleRate() * 100))
                            .value(String.valueOf(event.getValue()))
                            .tags(toTags(event.getTags()))
                            .send();
                    break;
                case GAUGE:
                    statfulClient.gauge(event.getMetricName(), event.getValue())
                            .with()
                            .sampleRate(Math.round(event.getSampleRate() * 100))
                            .tags(toTags(event.getTags()))
                            .send();
                    break;
                case TIMER:
                    statfulClient.timer(event.getMetricName(), Math.round(event.getValue()))
                            .with()
                            .sampleRate(Math.round(event.getSampleRate() * 100))
                            .tags(toTags(event.getTags()))
                            .send();
                    break;
                default:
                    LOGGER.warn("Unknown metric type");
            }
            LOGGER.debug("Sent metric to statful. {}", event);
            return Mono.empty();
        }).then();
    }

    private Tags toTags(Map<String, String> tags) {
        return Optional.ofNullable(tags)
                .map(map -> map
                        .entrySet()
                        .stream()
                        .map(es -> Tags.from(es.getKey(), es.getValue()))
                        .reduce(Tags::merge)
                        .orElse(new Tags()))
                .orElse(new Tags());
    }
}
