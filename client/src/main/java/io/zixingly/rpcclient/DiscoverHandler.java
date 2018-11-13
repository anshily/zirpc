package io.zixingly.rpcclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import io.zixingly.assis.ServeMsg;

public class DiscoverHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){


        System.out.println("connect!");

        if (msg instanceof ServeMsg){
            System.out.println("收到注册信息！");

            System.out.println(((ServeMsg) msg).getMsg());

            final int Port = ((ServeMsg) msg).getRemotePort();
            final String Host = ((ServeMsg) msg).getRemoteHost();

            new Thread(){
                @Override
                public void run(){

                    EventLoopGroup group = new NioEventLoopGroup();
                    try {
                        Bootstrap b = new Bootstrap();
                        b.group(group)
                                .channel(NioSocketChannel.class)
                                .handler(new ChannelInitializer<SocketChannel>() {
                                    @Override
                                    public void initChannel(SocketChannel ch) throws Exception {
                                        ChannelPipeline p = ch.pipeline();

                                        p.addLast(
                                                new ObjectEncoder(),
                                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                                new AccessHandler());
                                    }
                                });

                        // Start the connection attempt.
                        b.connect(Host, Port).sync().channel().closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        group.shutdownGracefully();
                    }
                }
            }.start();
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("注册服务!");

        ctx.writeAndFlush(new ServeMsg().setRemotePort(1234));
//        ctx.writeAndFlush(firstMessage);
    }
}
