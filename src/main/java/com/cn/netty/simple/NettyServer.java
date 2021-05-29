package com.cn.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty实现简单的服务端
 */
public class NettyServer {
    public static void main(String[] args) throws  Exception{
        //创建BossGroup和WorkerGroup
        //1:创建两个线程组bossGroup和workerGroup
        //2:bossGroup仅仅处理连接请求，真正的处理客户端业务，交给workerGroup
        //bossGroup和workerGroup含有子线程(NIOEventLoop)的个数默认为cpu核数*2(NettyRuntime.availableProcessors() * 2)
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try{
            //创建服务器端启动的对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行配置
            bootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道
                    .option(ChannelOption.SO_BACKLOG,128)//设置设置线程队列的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }//创建一个通道测试对象（匿名）
                        //给pipeline设置处理器

                    });//给我们的workerGroup的EvenLoop对应的管道设置处理器
            System.out.println("................服务器 is ready");
            //绑定一个端口，并且同步。生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
