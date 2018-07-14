package co.hold;

import co.hold.config.StatsdStatfulExporterConfiguration;
import co.hold.exceptions.MissingConfigurationException;
import co.hold.mapper.DefaultMetricMapper;
import co.hold.receivers.tcp.TcpServer;
import co.hold.receivers.udp.UdpServer;
import co.hold.senders.statful.StatfulSender;
import com.statful.client.domain.api.StatfulClient;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private static final Logger LOGGER = Loggers.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        StatsdStatfulExporterConfiguration appConfig = StatsdStatfulExporterConfiguration.loadConfiguration();

        appConfig.setEnvironment(Optional.ofNullable(System.getProperty("environment")).orElse(appConfig.getEnvironment()));

        final StatfulClient statfulClient = appConfig.getStatfulClient();

        final DefaultMetricMapper metricMapper = new DefaultMetricMapper();
        final StatfulSender statfulSender = new StatfulSender(statfulClient);

        Thread tcp = new Thread(() -> TcpServer.start(appConfig.getTcpConfiguration(), metricMapper, statfulSender));
        Thread udp = new Thread(() -> UdpServer.start(appConfig.getUdpConfiguration(), metricMapper, statfulSender));

        if (isNull(appConfig.getTcpConfiguration()) && isNull(appConfig.getUdpConfiguration())) {
            throw new MissingConfigurationException("Enable at least one server to sink metrics, udp or http.");
        }

        if (nonNull(appConfig.getTcpConfiguration())) {
            tcp.start();
        }

        if (nonNull(appConfig.getUdpConfiguration())) {
            udp.start();
        }
    }

}
