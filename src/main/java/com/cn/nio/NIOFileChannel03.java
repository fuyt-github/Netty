package com.cn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用buffer来完成文件的读取和写入
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws  Exception{
        //创建输入流
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        //创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        //创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while(true){
            /* 将buffer还原，
            public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }
            */
            byteBuffer.clear();
            int read = inputStreamChannel.read(byteBuffer);
            if(read == -1){//读取完成
                break;
            }
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();


    }
}
