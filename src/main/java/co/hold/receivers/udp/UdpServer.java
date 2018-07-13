package co.hold.receivers.udp;

import co.hold.domain.Event;
import co.hold.mapper.MetricMapper;
import co.hold.senders.MetricsSender;
import co.hold.util.Constants;
import org.json.JSONObject;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Arrays;
import java.util.Objects;

import static co.hold.util.Constants.PORT;

public class UdpServer {

    private static final Logger LOGGER = Loggers.getLogger(UdpServer.class);

    public static <T extends Event> void start(final JSONObject config,
                                               final MetricMapper<T> metricMapper,
                                               final MetricsSender<T> metricsSender) {
        Objects.requireNonNull(config, "Configuration object must not be null");

        /*
           SO_RCVBUF	The size of the socket receive buffer
         */

        reactor.ipc.netty.udp.UdpServer udpServer = reactor.ipc.netty.udp.UdpServer
                .create(config.getString(Constants.HOST), config.getInt(PORT));

        udpServer.startAndAwait((udpInbound, udpOutbound) -> udpInbound
                .receive()
                .asByteArray()
                .map(String::new)
                .map(data -> metricMapper.map(Arrays.asList(data.split("\n"))))
                .flatMap(metricsSender::send)
                .flatMap(d -> udpOutbound.then())
                .doOnError(e -> LOGGER.error("Error processing metrics", e)));
    }

}
