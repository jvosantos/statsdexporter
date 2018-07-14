package co.hold.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.statful.client.core.udp.StatfulFactory;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Transport;

import java.io.IOException;
import java.util.List;

import static co.hold.util.Constants.ENVIRONMENT;

public class StatsdStatfulExporterConfiguration {
    @JsonProperty
    private TcpConfiguration tcpConfiguration;
    @JsonProperty
    private UdpConfiguration udpConfiguration;
    @JsonProperty
    private List<Mappings> mappingsList;
    @JsonProperty
    private StatfulConfiguration statfulConfiguration;
    @JsonProperty
    private String environment;

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    public static StatsdStatfulExporterConfiguration loadConfiguration() throws IOException {
        return YAML_MAPPER.readValue(System.getProperty("configurationPath"), StatsdStatfulExporterConfiguration.class);
    }

    public StatfulClient getStatfulClient() {
        return StatfulFactory
                .buildUDPClient() //see if I can replace this with a nonBlocking udp client. (udp client will only block when socket buffer is full
                .with()
                .isDryRun(statfulConfiguration.getDryRun())
                .host(statfulConfiguration.getHost())
                .port(statfulConfiguration.getPort())
                .namespace(statfulConfiguration.getNamespace())
                .app(statfulConfiguration.getApp())
                .flushInterval(statfulConfiguration.getFlushInterval())
                .flushSize(statfulConfiguration.getFlushSize())
                .tag(ENVIRONMENT, environment)
                .transport(Transport.UDP)
                .build();
    }

    public TcpConfiguration getTcpConfiguration() {
        return tcpConfiguration;
    }

    public void setTcpConfiguration(TcpConfiguration tcpConfiguration) {
        this.tcpConfiguration = tcpConfiguration;
    }

    public UdpConfiguration getUdpConfiguration() {
        return udpConfiguration;
    }

    public void setUdpConfiguration(UdpConfiguration udpConfiguration) {
        this.udpConfiguration = udpConfiguration;
    }

    public List<Mappings> getMappingsList() {
        return mappingsList;
    }

    public void setMappingsList(List<Mappings> mappingsList) {
        this.mappingsList = mappingsList;
    }

    public StatfulConfiguration getStatfulConfiguration() {
        return statfulConfiguration;
    }

    public void setStatfulConfiguration(StatfulConfiguration statfulConfiguration) {
        this.statfulConfiguration = statfulConfiguration;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

}
