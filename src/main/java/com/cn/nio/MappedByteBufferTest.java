package com.cn.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：使用的读写模式
         * 参数2：可以直接修改的起始位置
         * 参数3：是映射到内存的大小（并不是索引的大小），即将1.text的多少个字节映射都内存，可以直接修改的范围是0-5
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0,5);
        map.put(1,(byte)'H');
        map.put(3,(byte)'D');
       // map.put(5,(byte)'l');//IndexOutOfBoundsException
        System.out.println("修改成功");
    }
}
