package io.zixingly.echodemo;

import io.zixingly.annotation.ZiRpcClient;

public class CallTest {

    @ZiRpcClient
    public String call(){
        return "I am in LY";
    }
}
