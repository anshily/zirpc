package io.zixingly.rpcserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.zixingly.assis.Constant;

import static io.zixingly.assis.Constant.PORT;

public class Server {

    public ServerRegisterHandler serverRegisterHandler;

    public void run(){

        serverRegisterHandler = new ServerRegisterHandler();

        EventLoopGroup cGroup = new NioEventLoopGroup();

        Bootstrap bc = new Bootstrap(); // (1)
        bc.group(cGroup); // (2)
        bc.channel(NioSocketChannel.class); // (3)
        bc.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        bc.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        serverRegisterHandler
                );
            }
        });

        // Start the client register to RC.
        ChannelFuture f = null; // (5)
        try {
            f = bc.connect(Constant.DISCOVER_HOST, Constant.DISCOVER_PORT).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Wait until the connection is closed.
        try {
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        f.syncUninterruptibly();
    }

    class ServerDo implements Runnable{
        public void run() {
//            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//            EventLoopGroup workerGroup = new NioEventLoopGroup();


            try{
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline p = ch.pipeline();
                                    p.addLast(
                                            new ObjectEncoder(),
                                            new ObjectDecoder(ClassResolvers.cacheDisabled(null))
                                            );
//                            p.addLast(new ObjectEchoServerHandler());
                                }
                            });

                    b.bind(PORT).sync().channel().closeFuture().sync();

                    // Bind and start to accept incoming connections.
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }

//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(bossGroup,workGroup)

        }finally {

            }
        }
    }
}
