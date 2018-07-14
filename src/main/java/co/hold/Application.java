package co.hold;

import co.hold.config.properties.StatsdStatfulExporterProperties;
import co.hold.exceptions.MissingConfigurationException;
import co.hold.mapper.DefaultMetricMapper;
import co.hold.receivers.tcp.TcpServer;
import co.hold.receivers.udp.UdpServer;
import co.hold.senders.statful.StatfulSender;
import com.statful.client.domain.api.StatfulClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.util.Logger;
import reactor.util.Loggers;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER = Loggers.getLogger(Application.class);

    @Bean
    public CommandLineRunner start(StatfulClient statfulClient,
                                   StatsdStatfulExporterProperties statsdStatfulExporterProperties) {
        return (args) -> {
            LOGGER.info("Configuration Loaded: " + statsdStatfulExporterProperties.toString());

            final DefaultMetricMapper metricMapper = new DefaultMetricMapper();
            final StatfulSender statfulSender = new StatfulSender(statfulClient);

            Thread tcpServerThread = new Thread(() -> TcpServer.start(statsdStatfulExporterProperties.getTcp(), metricMapper, statfulSender));
            Thread udpServerThread = new Thread(() -> UdpServer.start(statsdStatfulExporterProperties.getUdp(), metricMapper, statfulSender));

            if (isNull(statsdStatfulExporterProperties.getTcp()) && isNull(statsdStatfulExporterProperties.getUdp())) {
                throw new MissingConfigurationException("Enable at least one server to sink metrics, udp or http.");
            }

            if (nonNull(statsdStatfulExporterProperties.getTcp())) {
                tcpServerThread.start();
            }

            if (nonNull(statsdStatfulExporterProperties.getUdp())) {
                udpServerThread.start();
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
