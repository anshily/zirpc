import java.io.Serializable;

public class TestP implements Serializable {

    public String pname;

    public TestP(String pname) {
        this.pname = pname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
