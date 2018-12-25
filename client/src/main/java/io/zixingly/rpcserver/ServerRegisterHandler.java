package io.zixingly.rpcserver;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.zixingly.assis.Constant;
import io.zixingly.assis.RegisterMsg;

import static io.zixingly.assis.Constant.PORT;

public class ServerRegisterHandler extends ChannelDuplexHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("actived!");


        ChannelFuture f = ctx.channel().writeAndFlush(new RegisterMsg().setAddress(Constant.HOST).setPort(PORT));
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("flush");
            }
        });

//        super.channelActive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("when the writable changed!");
        super.channelWritabilityChanged(ctx);
    }
}
