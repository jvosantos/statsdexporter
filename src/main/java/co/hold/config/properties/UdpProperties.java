package co.hold.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UdpProperties {
    private Integer port = 52167;
    private String host = "127.0.0.1";

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "UdpProperties{" +
                "port=" + port +
                ", host='" + host + '\'' +
                '}';
    }
}
