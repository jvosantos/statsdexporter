package co.hold.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class StatfulProperties {
    private Long flushInterval = 5000L;
    private Integer flushSize = 10;
    private Boolean dryRun = false;
    private String host = "localhost";
    private Integer port = 2013;
    private String app = "statsdexporter";
    private String namespace = "statsdexporter";
    private String environment = "local";

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

    @Override
    public String toString() {
        return "StatfulProperties{" +
                "flushInterval=" + flushInterval +
                ", flushSize=" + flushSize +
                ", dryRun=" + dryRun +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", app='" + app + '\'' +
                ", namespace='" + namespace + '\'' +
                "environment='" + environment + '\'' +
                '}';
    }
}
