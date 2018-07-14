package co.hold.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatfulConfiguration {
    @JsonProperty
    private Long flushInterval;
    @JsonProperty
    private Integer flushSize;
    @JsonProperty
    private Boolean dryRun;
    @JsonProperty
    private String host;
    @JsonProperty
    private Integer port;
    @JsonProperty
    private String app;
    @JsonProperty
    private String namespace;
    @JsonProperty
    private String environment;

    public Long getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(Long flushInterval) {
        this.flushInterval = flushInterval;
    }

    public Integer getFlushSize() {
        return flushSize;
    }

    public void setFlushSize(Integer flushSize) {
        this.flushSize = flushSize;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

}
