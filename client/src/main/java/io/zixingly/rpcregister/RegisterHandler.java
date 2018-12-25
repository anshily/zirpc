package io.zixingly.rpcregister;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.zixingly.assis.RegisterMsg;
import io.zixingly.assis.ServeMsg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


@ChannelHandler.Sharable
public class RegisterHandler extends ChannelDuplexHandler {

    static List<RegisterMsg> list = new ArrayList<RegisterMsg>();

    static Map<String,RegisterMsg> providerMap = new Hashtable<String, RegisterMsg>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("连接数加一！");
        super.channelActive(ctx);
    }


    //    static int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // Echo back the received object to the client.
        System.out.println(msg.getClass());
        if (msg instanceof RegisterMsg){

            System.out.println(((RegisterMsg) msg).getAddress()+"已注册");
            System.out.println(((RegisterMsg) msg).getPort()+"已监听");
            System.out.println("ctx.channel().id().asShortText():"+ctx.channel().id().asShortText());

            providerMap.put(ctx.channel().id().asShortText(),(RegisterMsg)msg);

//            list.add((RegisterMsg) msg);

        }

        if (msg instanceof ServeMsg){

            System.out.println("服务提供者个数：" + providerMap.size());
            if (!providerMap.isEmpty()){
                int seed = (int)(Math.random()*providerMap.size());

                RegisterMsg[] pMapToArr = providerMap.values().toArray(new RegisterMsg[0]);
//                System.out.println(count);
                System.out.println("当前服务种子"+seed);
                RegisterMsg registerMsg = pMapToArr[seed];    //list.get(seed);
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

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx){

        System.out.println(ctx.channel().id().asShortText()+"服务停机，已移除服务序列！");
        providerMap.remove(ctx.channel().id().asShortText());

        System.out.println("ctx.channel().remoteAddress().toString():"+ctx.channel().remoteAddress().toString());
        System.out.println("ctx.channel().id().asShortText()"+ctx.channel().id().asShortText());
        System.out.println("剩余服务个数："+providerMap.size());
//        super.channelUnregistered(ctx);
    }
}
