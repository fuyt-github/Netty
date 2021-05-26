package com.cn.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * selector使用
 */
public class NIOServer {
    public static void main(String[] args) throws  Exception{
        //1:创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2:得到一个Selector
        Selector selector = Selector.open();
        //3:绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //4:设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //5:把serverSocketChannel注册到Selector关心的的事件为OP_ACCEPT；
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //注册的全部事件
        System.out.println("SelectorKey的长度===="+selector.keys().size());
        //6:循环等待客户端连接
        while(true){
            //7:等待一秒，如果一秒内没有时间则返回
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了一秒，无连接");
                continue;
            }
            //如果大于0,则获取到相关的SelectionKey集合
            //如果大于0，表示已经获取到相关的事件(有事件发生的通道)
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //8:遍历Set<SelectionKey>,使用迭代便利
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件，做出相应的处理
                if(key.isAcceptable()){
                    //则该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功生成一个socketChannel"+socketChannel.hashCode());
                    //将SocketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到Selector，关注事件为OP_READ,同时给SocketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("SelectorKey的长度===="+selector.keys().size());
                }
                if(key.isReadable()){//发生OP_READ
                    //通过key反向获取对应的Channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //获取channel关联的Bufffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("from 客户端"+new String(byteBuffer.array()));
                }
                keyIterator.remove();
            }

        }
    }
}
