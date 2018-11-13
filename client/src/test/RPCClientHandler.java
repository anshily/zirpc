import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.*;
import java.util.Date;

public class RPCClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {

        System.out.println("recive");
        System.out.println(msg);

        ByteBuf bf = (ByteBuf) msg;

//        byte[] bytes =new  byte[10];


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024*10);

//        ByteBufInputStream bbis = new ByteBufInputStream()

        ObjectOutputStream ops = null;
        try {
            ops = new ObjectOutputStream(byteArrayOutputStream);
            ops.writeObject(new TestP("ppp!"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            bf.writeBytes(byteArrayInputStream,1024*10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ChannelFuture f = ctx.writeAndFlush(bf);

        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        }); // (4)


//        bf.writeBytes()

//        ByteBuf m = (ByteBuf) msg; // (1)
//        try {
//
//            System.out.println(m.readUnsignedInt());
////            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
////            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        } finally {
//            m.release();
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {

//        ctx.read();

        System.out.println(ctx.alloc().buffer().readerIndex(0));
        System.out.println(ctx.name());
        System.out.println(ctx.toString());

        System.out.println("client register!");


        ByteBuf bf = ctx.alloc().buffer(63);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(63);

//        ByteBufInputStream bbis = new ByteBufInputStream()

        ObjectOutputStream ops = null;
        try {
            ops = new ObjectOutputStream(byteArrayOutputStream);
            ops.writeObject(new TestP("ppp!"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = byteArrayOutputStream.toByteArray();

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i]);
        }

        System.out.println();
        System.out.println(bf.readableBytes());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            bf.writeBytes(byteArrayInputStream,bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ByteBuf bfp = Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes());
//
//        bf.writeBytes(bfp);

//        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
//
//
//        TestP testP = (TestP) objectInputStream.readObject();
//
//        System.out.println(testP.getPname());

//        System.out.println(bf.readableBytes());
//        for (int i = 0; i < bf.readableBytes(); i++) {
//            System.out.print(bf.getByte(i));
//        }

//        ctx.writeAndFlush(bf);

        final ChannelFuture f = ctx.writeAndFlush(bf);

        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
//                assert f == future;
                if (future.isSuccess()){
                    System.out.println("future Success");
                }

//                ctx.close();
//                ctx.fireChannelReadComplete();
            }
        }); // (4)


//        ctx.fireChannelRegistered();
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

        System.out.println("channelUnregistered");
//        ctx.fireChannelUnregistered();
    }
}
