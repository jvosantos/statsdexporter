package co.hold.mapper;

import co.hold.domain.Event;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface MetricMapper<T extends Event> {
    Flux<T> map(Collection<String> metricLines);
}
