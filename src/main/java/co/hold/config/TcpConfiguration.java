package co.hold.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TcpConfiguration {
    @JsonProperty
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
