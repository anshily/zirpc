import java.io.Serializable;

public class RegisterMsg implements Serializable {
    String address;
    int port;

    public String getAddress() {
        return address;
    }

    public RegisterMsg setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RegisterMsg setPort(int port) {
        this.port = port;
        return this;
    }
}
