package io.zixingly.rpcserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.zixingly.assis.Constant;

public class Server {
    public static void main(String[] args) throws Exception {

        System.out.println("服务器启动");


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
                        new RegisterHandler()
                );
            }
        });

        // Start the client register to RC.
        ChannelFuture f = bc.connect(Constant.DISCOVER_HOST, Constant.DISCOVER_PORT).sync(); // (5)

        // Wait until the connection is closed.
        f.channel().closeFuture().sync();


    }
}
