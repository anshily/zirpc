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

import static io.zixingly.assis.Constant.DISCOVER_HOST;
import static io.zixingly.assis.Constant.DISCOVER_PORT;

public class IOProvider {

    private Channel channel;
    private DiscoverHandler discoverHandler = new DiscoverHandler();


    public IOProvider() {
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
                                    discoverHandler);
                        }
                    });

            // Start the connection attempt.
            Channel channel = b.connect(DISCOVER_HOST, DISCOVER_PORT).sync().channel();

            channel.closeFuture().sync();

            System.out.println("bootstrap started!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
//            group.next();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public DiscoverHandler getDiscoverHandler() {
        return discoverHandler;
    }
}
