package io.zixingly.rpcclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.zixingly.assis.BusinessMsg;


import java.util.Scanner;

import static io.zixingly.assis.Constant.DISCOVER_HOST;
import static io.zixingly.assis.Constant.DISCOVER_PORT;

public class Client {

//    private DiscoverHandler discoverHandler = new DiscoverHandler();

    private static IOProvider ioProvider = new IOProvider();


    public static void main(String[] args) throws Exception {

//        ioProvider.start();
//        IOProvider ioProvider = new IOProvider();
//
//        Channel channel = ioProvider.getDiscoverHandler().getHandler().getChannel();
//        System.out.println(channel.remoteAddress());

        new Thread(){
            @Override
            public void run() {


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
                                            new DiscoverHandler(ioProvider));
                                }
                            });

                    // Start the connection attempt.
                    Channel channel = null;
                    try {
                        channel = b.connect(DISCOVER_HOST, DISCOVER_PORT).sync().channel();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//            channel.writeAndFlush(new BusinessMsg().setMsg("测试主程序"));

                    try {
                        channel.closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("bootstrap started!");
                } finally {
                    group.shutdownGracefully();
//            group.next();
                }

//                super.run();
            }
        }.run();

        Scanner scanner = new Scanner(System.in);
        System.out.println("please input to test send:");

        if (scanner.hasNext()){

            String str = scanner.next();
            ioProvider.send(new BusinessMsg().setMsg(str));
        }



//        ioProvider.send(new BusinessMsg().setMsg("测试ioprovider"));

//        System.identityHashCode();
    }


//    public void run(){
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        public void initChannel(SocketChannel ch) throws Exception {
//                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(
//                                    new ObjectEncoder(),
//                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
//                                    discoverHandler);
//                        }
//                    });
//
//            // Start the connection attempt.
//            Channel channel = b.connect(DISCOVER_HOST, DISCOVER_PORT).sync().channel();
//
//            channel.closeFuture().sync();
//
//            System.out.println("bootstrap started!");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            group.shutdownGracefully();
////            group.next();
//        }
//    }
}
