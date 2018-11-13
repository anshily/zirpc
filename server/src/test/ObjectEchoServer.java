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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;


public final class ObjectEchoServer {



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
//                    ch.pipeline().addLast(new RPCClientHandler());
                ch.pipeline().addLast(
                        new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
//                        new StringEncoder(),
//                        new StringDecoder(),
                        new RegisterHandler()
                );
            }
        });

        // Start the client register to RC.
        ChannelFuture f = bc.connect(Constant.DISCOVER_HOST, Constant.DISCOVER_PORT).sync(); // (5)

        System.out.println(f);

        // Wait until the connection is closed.
        f.channel().closeFuture().sync();


    }
}