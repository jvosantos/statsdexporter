package co.hold;

import co.hold.config.ConfigReader;
import co.hold.exceptions.MissingConfigurationException;
import co.hold.mapper.DefaultMetricMapper;
import co.hold.receivers.tcp.TcpServer;
import co.hold.receivers.udp.UdpServer;
import co.hold.senders.statful.StatfulSender;
import com.statful.client.core.udp.StatfulFactory;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Transport;
import org.json.JSONObject;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static co.hold.util.Constants.*;

public class Main {

    private static final Logger LOGGER = Loggers.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        final String configPath = System.getProperty("configurationPath");
        final String environment = Optional.ofNullable(System.getProperty("environment")).orElse("default");

        byte[] configData = ConfigReader.read(configPath).block();

        final JSONObject config = new JSONObject(new String(configData, StandardCharsets.UTF_8));
        final JSONObject tcpConfig = config.getJSONObject(TCP);
        final JSONObject udpConfig = config.getJSONObject(UDP);
        final JSONObject statfulConfig = config.getJSONObject(STATFUL);

        final StatfulClient statfulClient = StatfulFactory
                .buildUDPClient() //see if I can replace this with a nonBlocking udp client. (udp client will only block when socket buffer is full
                .with()
                .isDryRun(statfulConfig.getBoolean(DRY_RUN))
                .host(statfulConfig.getString(HOST))
                .port(statfulConfig.getInt(PORT))
                .namespace(statfulConfig.getString(NAMESPACE))
                .app(statfulConfig.getString(APP))
                .flushInterval(statfulConfig.getLong(FLUSH_INTERVAL))
                .flushSize(statfulConfig.getInt(FLUSH_SIZE))
                .tag(ENVIRONMENT, environment)
                .transport(Transport.UDP)
                .build();

        final DefaultMetricMapper metricMapper = new DefaultMetricMapper();
        final StatfulSender statfulSender = new StatfulSender(statfulClient);

        Thread tcp = new Thread(() -> TcpServer.start(tcpConfig, metricMapper, statfulSender));
        Thread udp = new Thread(() -> UdpServer.start(udpConfig, metricMapper, statfulSender));

        if (!tcpConfig.getBoolean(ACTIVE) && !udpConfig.getBoolean(ACTIVE)) {
            throw new MissingConfigurationException("Enable at least one server to sink metrics, udp or http.");
        }

        if (tcpConfig.getBoolean(ACTIVE)) {
            tcp.start();
        }

        if (udpConfig.getBoolean(ACTIVE)) {
            udp.start();
        }
    }

}
