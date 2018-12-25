package io.zixingly;

import io.zixingly.assis.MSG;
import io.zixingly.rpcclient.Client;
import io.zixingly.rpcserver.Server;

import java.util.Queue;

public class Transporter {
    private Queue<MSG> msgs;
    private Server server;
    private Client client;

    public boolean isServerInited(){
        return false;
    }

    public boolean isClientInited(){
        return false;
    }

    public void init(){

    }

}
