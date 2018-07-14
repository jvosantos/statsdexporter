package co.hold.config;

import co.hold.config.properties.*;
import com.statful.client.core.udp.StatfulFactory;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Transport;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.hold.util.Constants.ENVIRONMENT;

@Configuration
@EnableConfigurationProperties({TcpProperties.class, UdpProperties.class, StatfulProperties.class, MappingsProperties.class})
public class StatsdStatfulExporterConfiguration {

    @Bean
    public StatfulClient getStatfulClient(StatsdStatfulExporterProperties statsdStatfulExporterProperties) {
        StatfulProperties statfulProperties = statsdStatfulExporterProperties.getStatful();
        return StatfulFactory
                .buildUDPClient() //see if I can replace this with a nonBlocking udp client. (udp client will only block when socket buffer is full
                .with()
                .isDryRun(statfulProperties.getDryRun())
                .host(statfulProperties.getHost())
                .port(statfulProperties.getPort())
                .namespace(statfulProperties.getNamespace())
                .app(statfulProperties.getApp())
                .flushInterval(statfulProperties.getFlushInterval())
                .flushSize(statfulProperties.getFlushSize())
                .tag(ENVIRONMENT, statfulProperties.getEnvironment())
                .transport(Transport.UDP)
                .build();
    }
}
