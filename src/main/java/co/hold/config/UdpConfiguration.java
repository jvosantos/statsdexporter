package co.hold.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UdpConfiguration {
    @JsonProperty
    private Integer port;
    @JsonProperty
    private String host;

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
}
