package io.zixingly.rpcclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class AccessHandler extends ChannelInboundHandlerAdapter {

    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        channel = ctx.channel();
        System.out.println("客户端远程通道激活！已连接服务提供商");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与远程服务端断开连接！");
        super.channelUnregistered(ctx);
    }

    public Channel getChannel() {
        return channel;
    }
}
