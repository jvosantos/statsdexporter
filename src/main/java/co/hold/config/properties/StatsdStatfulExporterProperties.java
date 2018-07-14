package co.hold.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class StatsdStatfulExporterProperties {
    private TcpProperties tcp;
    private UdpProperties udp;
    private StatfulProperties statful;

    public TcpProperties getTcp() {
        return tcp;
    }

    public void setTcp(TcpProperties tcp) {
        this.tcp = tcp;
    }

    public UdpProperties getUdp() {
        return udp;
    }

    public void setUdp(UdpProperties udp) {
        this.udp = udp;
    }

    public StatfulProperties getStatful() {
        return statful;
    }

    public void setStatful(StatfulProperties statful) {
        this.statful = statful;
    }

    @Override
    public String toString() {
        return "StatsdStatfulExporterProperties{" +
                "tcp=" + tcp +
                ", udp=" + udp +
                ", statful=" + statful +
                '}';
    }
}
