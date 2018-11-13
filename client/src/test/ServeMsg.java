import java.io.Serializable;

public class ServeMsg implements Serializable {
    String name;
    String remoteHost;
    int remotePort;
    String msg;

    public String getMsg() {
        return msg;
    }

    public ServeMsg setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServeMsg setName(String name) {
        this.name = name;
        return this;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public ServeMsg setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
        return this;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public ServeMsg setRemotePort(int remotePort) {
        this.remotePort = remotePort;
        return this;
    }
}
