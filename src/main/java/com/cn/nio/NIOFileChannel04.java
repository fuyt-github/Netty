package com.cn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用transferFrom实现copy。将资源channel的数据copy到目标channel
 * transferFrom(arg1,arg2,arg3)
 * arg1:资源信道
 * arg2:开始位置
 * arg3:结束位置
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("d:\\test\\dog.jpeg");
        FileChannel channel01 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\test\\dog_copy.jpeg");
        FileChannel channel02 = fileOutputStream.getChannel();
        channel02.transferFrom(channel01,0,channel01.size());
        fileInputStream.close();
        fileOutputStream.close();
    }
}
