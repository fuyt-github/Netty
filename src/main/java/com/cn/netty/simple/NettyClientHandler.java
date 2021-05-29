package com.cn.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.channels.Channel;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发该事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client cte :" +ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Server 喵", CharsetUtil.UTF_8));
    }

    //当通道有读取事件发生时会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器回复的消息是：" +byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址是" +ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
