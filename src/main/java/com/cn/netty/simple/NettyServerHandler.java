package com.cn.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 1:我们自定义一个Handler需要继承一个netty，规定好的某个HandlerAdapter(规范)
 * 2：这是我们自定义的一个handler，才能是一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx:上下文对象，可以获取管道pipeline，通道channel，地址
     * @param msg：即使客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程："+Thread.currentThread().getName());
        System.out.println("server ctx = " +ctx);
        //将msg转换为ByteBuf(此处的ByteBuf为Netty的，并非NIO的ByteBuffer,它更加强大)
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的数据是===" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址是====" + ctx.channel().remoteAddress());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲，并刷新
        //我们需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("HELLO 客户端",CharsetUtil.UTF_8));
    }

    //处理异常，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
