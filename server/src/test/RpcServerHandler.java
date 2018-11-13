import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

import java.io.*;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(byteBuf.readableBytes());

        if (byteBuf.hasArray()){


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuf.array());
            try {
                ObjectInputStream os = new ObjectInputStream(byteArrayInputStream);

                TestP testP = (TestP) os.readObject();

                System.out.println(testP.getPname());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            byte[] bytes = new  byte[byteBuf.readableBytes()];

            byteBuf.readBytes(bytes);

            for (int i = 0; i <bytes.length ; i++) {
                System.out.print(bytes[i]);
            }

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            try {
//                byteBuf.readBytes(byteArrayOutputStream,byteBuf.readableBytes());
//                ObjectInputStream objectInputStream = new ObjectInputStream(bytes);

                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));


                TestP testP = (TestP) objectInputStream.readObject();

                System.out.println(testP.getPname());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            byteBuf.readBytes((InputStream)byteArrayInputStream,byteBuf.readableBytes());
        }

    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)

//        System.out.println(msg);

//        ByteBuf in = (ByteBuf) msg;
//
//        while (in.isReadable()) { // (1)
//            System.out.print((char) in.readByte());
//            System.out.flush();
//        }
//        ctx.close();

//        final ByteBuf time = ctx.alloc().buffer(4); // (2)
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

//        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        }); // (4)
        // Discard the received data silently.
//        ctx.write(msg); // (1)
//        ctx.flush(); // (2)
//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg); // (2)
//        }

//        System.out.println(msg);
//        ((ByteBuf) msg).release(); // (3)
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
//        // Close the connection when an exception is raised.
//        cause.printStackTrace();
//        ctx.close();
//    }

//    @Override
//    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("register");
//        ByteBuf payLoad = ctx.alloc().buffer(100);
//        final ChannelFuture f = ctx.writeAndFlush("register");
//        f.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                System.out.println("send the register msg succeed!");
//            }
//        });
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("handler 1");
//        this.channelReadComplete(ctx);


//        final ChannelPipeline pipeline = pipeline();

//        byte[] bytes = {1,2,3,4};
//
//        ByteBuf pl = ctx.alloc().buffer(32);
//
//        System.out.println("可写入字节数："+pl.writableBytes());
//
////        System.out.println("写入"+pl.writeInt(1));
//        System.out.println("写入"+pl.writeBytes(bytes));
//
//        System.out.println("可写入字节数："+pl.writableBytes());
//
//        final ChannelFuture f = ctx.writeAndFlush(pl);
//        f.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                System.out.println("send the active msg succeed!");
//            }
//        });

//        System.out.println("active!");
//        ctx.write("active").addListener();
        ctx.fireChannelActive();
    }
}
