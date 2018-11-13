package io.zixingly.register;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.zixingly.assis.RegisterMsg;
import io.zixingly.assis.ServeMsg;

import java.util.ArrayList;
import java.util.List;

public class RegisterHandler extends ChannelInboundHandlerAdapter {

    static List<RegisterMsg> list = new ArrayList<RegisterMsg>();

    static int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // Echo back the received object to the client.
        System.out.println(msg.getClass());
        if (msg instanceof RegisterMsg){

            System.out.println(((RegisterMsg) msg).getAddress()+"已注册");
            System.out.println(((RegisterMsg) msg).getPort()+"已监听");
            System.out.println(++count);

            list.add((RegisterMsg) msg);

        }

        if (msg instanceof ServeMsg){
//            if (!map.isEmpty()){
//            }
            System.out.println("服务提供者个数："+list.size());
            if (!list.isEmpty()){
                int seed = (int)(Math.random()*list.size());
                System.out.println(count);
                System.out.println("当前服务种子"+seed);
                RegisterMsg registerMsg = list.get(seed);
                ctx.channel().writeAndFlush(new ServeMsg()
                        .setRemoteHost(registerMsg.getAddress())
                        .setRemotePort(registerMsg.getPort()));
            }else {
                System.out.println("暂无服务");
                ctx.channel().writeAndFlush(new ServeMsg().setMsg("暂无服务"));
            }
        }
//        ctx.write(msg);


    }
}
