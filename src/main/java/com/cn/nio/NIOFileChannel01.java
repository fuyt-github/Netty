package com.cn.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 写入文件
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {

        String s = "hello world";
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\test\\file01.txt");
        //通过流创建一个FileChannel,File为一个抽象类，实际上是FileChannelImpl对象
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将字符串写入Buffer
        byteBuffer.put(s.getBytes());
        //读写反转
        byteBuffer.flip();
        //将缓冲区中的数据写入channel
        channel.write(byteBuffer);
        //关闭流
        fileOutputStream.close();
        System.out.println("成功==============");
    }
}
