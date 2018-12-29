package io.zixingly.rpcclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.zixingly.assis.BusinessMsg;

import java.net.InetSocketAddress;

public class IOProvider {


    private IOHandler ioHandler = new IOHandler();

    private Bootstrap bootstrap;

    private ChannelFuture future;

    private boolean init = false;

    private boolean isClosed = false;

    public void start(int port) {
        if(init) {
            throw new RuntimeException("client is already started");
        }
        //thread model: one worker thread pool,contains selector thread and workers‘.
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);//1 is OK
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class) //create SocketChannel transport
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    ioHandler);
                        }
                    });
            //keep the connection with server，and blocking until closed!
            future = bootstrap.connect(new InetSocketAddress("127.0.0.1", port)).sync();
            init = true;
        } catch (Exception e) {
            isClosed = true;
        } finally {
            if(isClosed) {
                workerGroup.shutdownGracefully();
            }
        }
    }

    public void close() {
        if(isClosed) {
            return;
        }
        try {
            future.channel().close();
        } finally {
            bootstrap.group().shutdownGracefully();
        }
        isClosed = true;
    }

    /**
     * 发送消息
     * @param message
     * @return
     * @throws Exception
     */
    public BusinessMsg send(BusinessMsg message) throws Exception {
        if(isClosed || !init) {
            throw new RuntimeException("client has been closed!");
        }
        //send a request call,and blocking until recevie a response from server.
        return ioHandler.call(message,future.channel());
    }

}
