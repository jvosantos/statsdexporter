package co.hold.receivers.tcp;

import co.hold.domain.Event;
import co.hold.mapper.MetricMapper;
import co.hold.senders.MetricsSender;
import org.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Arrays;
import java.util.Objects;

import static co.hold.util.Constants.PORT;

public class TcpServer {

    private static final Logger LOGGER = Loggers.getLogger(TcpServer.class);

    public static <T extends Event> void start(final JSONObject config,
                                               final MetricMapper<T> metricMapper,
                                               final MetricsSender<T> metricsSender) {
        Objects.requireNonNull(config, "Configuration object must not be null");

        reactor.ipc.netty.tcp.TcpServer server = reactor.ipc.netty.tcp.TcpServer.create(config.getInt(PORT));

        server.startAndAwait((in, out) -> in.receive()
                .asString()
                .map(data -> metricMapper.map(Arrays.asList(data.split("\n"))))
                .map(metricsSender::send)
                .flatMap(d -> out.sendString(Mono.just("RECEIVED")))
                .doOnError(e -> LOGGER.error("Error processing metrics", e)));
    }
}
