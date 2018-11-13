import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


@ChannelHandler.Sharable
public class RegisterHandler extends ChannelInboundHandlerAdapter {

    private int count = 1;


    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)

        System.out.println("当前活跃数："+count);
        count++;
//        final ByteBuf time = ctx.alloc().buffer(4); // (2)
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
//        f.addListener(new ChannelFutureListener() {
//            //            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        }); // (4)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;

        byte[] con = new byte[in.readableBytes()];

        if (in.isReadable()){
            in.readBytes(con);
        }

        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            in.readBytes(outputStream,in.readableBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] testarr = ((ByteArrayOutputStream) outputStream).toByteArray();

        for (int i = 0; i < testarr.length; i++) {
            System.out.println(testarr[i]);
        }

//        byte[] con = in.array();
//        System.out.println(con);


//        try {
//            OutputStream outputStream = new ByteArrayOutputStream();
//
//            System.out.println("可读取字节数："+in.readableBytes());
//            while (in.isReadable()){
////                System.out.println("内容："+in.readByte());
//                in.readBytes(con);
//            }
//
//System.out.println(((ByteArrayOutputStream) outputStream).toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
