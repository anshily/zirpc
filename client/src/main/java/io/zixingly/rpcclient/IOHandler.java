package io.zixingly.rpcclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.zixingly.assis.BusinessMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IOHandler extends SimpleChannelInboundHandler<BusinessMsg> {
    private Map<String,BusinessMsg> response = new ConcurrentHashMap<String, BusinessMsg>();

    //key is sequence ID，value is request thread.
    private final Map<String,Thread> waiters = new ConcurrentHashMap<String, Thread>();

    private final AtomicInteger sequence = new AtomicInteger();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当channel就绪后。
        System.out.println("client channel is ready!");
        //ctx.writeAndFlush("started");//阻塞知道发送完毕
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BusinessMsg message) throws Exception {
//        JSONObject json = JSONObject.fromObject(message);
//        Integer id = json.getInt("id");
//        response.put(id,json.getString("md5Hex"));
        String id = message.getType();

        response.put(id,message);

        System.out.println(id);

        System.out.println(message.getMsg());

        Thread thread = waiters.remove(id);//读取到response后，从waiters中移除并唤醒线程。
        synchronized (thread) {
            thread.notifyAll();
        }
    }


    public BusinessMsg call(BusinessMsg message, Channel channel) throws Exception {
        String id =
                String.valueOf(sequence.incrementAndGet());//产生一个ID，并与当前request绑定
        Thread current = Thread.currentThread();
        waiters.put(id,current);
        message.setType(id);

        System.out.println(id);

//        JSONObject json = new JSONObject();
//        json.put("id",id);
//        json.put("source",message);
        channel.writeAndFlush(message);
        while (!response.containsKey(id)) {
            synchronized (current) {
                current.wait();//阻塞请求调用者线程，直到收到响应响应
            }
        }
        waiters.remove(id);
        return response.remove(id);

    }
}
