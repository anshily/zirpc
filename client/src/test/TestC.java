public class TestC extends TestP {
    public String cname;

    public TestC(String pname) {
        super(pname);
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
