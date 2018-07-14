package co.hold.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class TcpProperties {
    private Integer port = 8080;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "TcpProperties{" +
                "port=" + port +
                '}';
    }
}
