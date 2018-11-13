/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;


/**
 * Handler implementation for the object echo client.  It initiates the
 * ping-pong traffic between the object echo client and server by sending the
 * first message to the server.
 */
public class ObjectEchoClientHandler extends ChannelInboundHandlerAdapter {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", Constant.DISCOVER_HOST);
    static final int PORT = Integer.parseInt(System.getProperty("port", Constant.DISCOVER_PORT+""));

//    private final List<Integer> firstMessage;
//
//    /**
//     * Creates a client-side handler.
//     */
//    public ObjectEchoClientHandler() {
//        firstMessage = new ArrayList<Integer>(ObjectEchoClient.SIZE);
//        for (int i = 0; i < ObjectEchoClient.SIZE; i ++) {
//            firstMessage.add(Integer.valueOf(i));
//        }
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Send the first message if this handler is a client-side handler.
        System.out.println("active!");

        ctx.writeAndFlush(new ServeMsg().setRemotePort(1234));
//        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws SSLException {

        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        System.out.println("connect!");

        if (msg instanceof ServeMsg){
            System.out.println("收到注册信息！");
            System.out.println((((ServeMsg) msg).getRemoteHost()));
            System.out.println((((ServeMsg) msg).getRemotePort()));
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
                                        if (sslCtx != null) {
                                            p.addLast(sslCtx.newHandler(ch.alloc(), Host, Port));
                                        }
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
        // Echo back the received object to the server.
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}