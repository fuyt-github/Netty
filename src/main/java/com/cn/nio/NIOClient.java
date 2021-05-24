package com.cn.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {
        //得到一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        //提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                //没有服务器需要连接
                System.out.println("因为连接需要事件，客户端不会阻塞，可以做其他工作");
            }
        }
        //如果连接成功
        String str = "HELLO 您好";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将数据写入Channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
