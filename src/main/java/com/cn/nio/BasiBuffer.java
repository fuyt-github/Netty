package com.cn.nio;

import java.nio.IntBuffer;

/**
 * NIO三大组件，Buffer基础
 */
public class BasiBuffer {
    public static void main(String[] args) {
        //创建一个容量为5个int的5的IntBuffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer里面添加数据
        //第一种使用put方法
       /* intBuffer.put(2);
        intBuffer.put(5);
        intBuffer.put(7);
        intBuffer.put(9);
        intBuffer.put(12);*/
        //第二种，使用for循环，intBuffer.capacity()获取容量
        for(int i = 0 ; i < intBuffer.capacity(); i++){
            intBuffer.put(i * 2);
        }
        //读取切换
        intBuffer.flip();
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get() );
        }
    }
}
