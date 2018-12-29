package io.zixingly.rpcserver;

import io.netty.channel.*;
import io.zixingly.assis.BusinessMsg;

import java.util.concurrent.Future;


public class RpcServerHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("来自远程的调用！");
        if (msg instanceof BusinessMsg){
            System.out.println(((BusinessMsg) msg).getMsg());

            ChannelFuture future = ctx.channel().writeAndFlush(new BusinessMsg().setMsg("服务器处理中").setType(((BusinessMsg) msg).getType()));
            future.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("flush");
                }
            });
        }
//        ctx.fireChannelActive();
    }
}
