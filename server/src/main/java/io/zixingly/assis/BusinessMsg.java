package io.zixingly.assis;

import java.io.Serializable;

public class BusinessMsg implements Serializable {
    String type;
    String msg;

    public String getType() {
        return type;
    }

    public BusinessMsg setType(String type) {
        this.type = type;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BusinessMsg setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
