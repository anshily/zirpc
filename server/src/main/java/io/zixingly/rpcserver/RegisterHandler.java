package io.zixingly.rpcserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.zixingly.assis.Constant;
import io.zixingly.assis.RegisterMsg;

import static io.zixingly.assis.Constant.PORT;

public class RegisterHandler extends ChannelDuplexHandler {


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("register! 发送注册信息。。。");

//        new Thread(){
//            @Override
//            public void run() {
//                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//                EventLoopGroup workerGroup = new NioEventLoopGroup();
//                try {
//                    ServerBootstrap b = new ServerBootstrap();
//                    b.group(bossGroup, workerGroup)
//                            .channel(NioServerSocketChannel.class)
//                            .handler(new LoggingHandler(LogLevel.INFO))
//                            .childHandler(new ChannelInitializer<SocketChannel>() {
//                                @Override
//                                public void initChannel(SocketChannel ch) throws Exception {
//                                    ChannelPipeline p = ch.pipeline();
//                                    p.addLast(
//                                            new ObjectEncoder(),
//                                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
//                                            new TestIOproviderHandler());
////                            p.addLast(new ObjectEchoServerHandler());
//                                }
//                            });
//
//                    // Bind and start to accept incoming connections.
//                    try {
//                        b.bind(PORT).sync().channel().closeFuture().sync();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                } finally {
//                    bossGroup.shutdownGracefully();
//                    workerGroup.shutdownGracefully();
//                }
//            }
//        }.start();
        ctx.fireChannelRegistered();
    }

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
