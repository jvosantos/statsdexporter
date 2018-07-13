package co.hold.senders;

import co.hold.domain.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MetricsSender<T extends Event> {
    Mono<Void> send(Flux<T> events);
}
