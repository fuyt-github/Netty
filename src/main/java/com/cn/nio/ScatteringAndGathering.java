package com.cn.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Scattering:将数据写入buffer时，可是使用Buffer数组，依次写入
 * Gathering:从buffer读取数据时，可hi采用Buffer数组，一次读
 */
public class ScatteringAndGathering {
    public static void main(String[] args) throws  Exception{
        //使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定socket端口，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        int byteWrite = 0;
        //循环的读取
        while(true){
            int byteRead = 0;
            while(byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;//累计读取的字节数
                System.out.println("byteRead=" + byteRead);
                //使用流打印，看看当前的这个Buffer的postion和limit
                Arrays.asList(byteBuffers).stream().map(buffer ->"postion=" +buffer.position()+","+"limit="+buffer.limit()).forEach(System.out::println);
            }
            //将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
            //将所有的数据读出来显示在客户端

            while(byteWrite < messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }
            //将所有Buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead:=" +byteRead +"byteWrite:="+byteWrite+"messageLength:+" +messageLength);
        }



    }
}
