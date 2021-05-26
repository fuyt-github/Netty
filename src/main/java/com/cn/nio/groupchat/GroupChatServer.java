package com.cn.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private static final  int PORT = 6667;
    private Selector selector;
    private ServerSocketChannel listenChannel;
    public GroupChatServer(){
        try{
            //得到选择器
            selector = Selector.open();
            //得到ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口号
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置为非阻塞模式
            listenChannel.configureBlocking(false);
            //将listenChannel注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void listen(){
        //循环处理
        while(true){
            try{
                int count = selector.select();
                if(count > 0){//有事件发生
                    //得到SelectorKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        //去除selectorKey
                        SelectionKey key = iterator.next();
                        //监听Accept
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            //设置为非阻塞
                            socketChannel.configureBlocking(false);
                            //注册到Selector
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            //
                            System.out.println(socketChannel.getRemoteAddress() + "上线！！！");
                        }
                        if(key.isReadable()){
                            //处理读
                            readDate(key);
                        }
                        //删除当前的key，避免重复处理
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待连接。。。。。。。。。。。。。");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //发生异常处理
            }

        }
    }
    //处理读
    public void readDate(SelectionKey selectionKey){
        //获取到关联的Channel
        SocketChannel socketChannel = null;
        try{
            socketChannel = (SocketChannel)selectionKey.channel();
            //创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(buffer);
            //根据count值处理
            if(read > 0){
                //把缓冲区放进buffer
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" +msg);
                //向其他客户端转发消息
                sendInfoToOther(msg,socketChannel);
            }
        }catch (Exception e){
            try{
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                socketChannel.close();
            }catch (IOException e1){
                e1.printStackTrace();
            }

        }
    }

    //向其他客户端转发消息
    public void sendInfoToOther(String msg,SocketChannel socketChannel) throws  Exception{
        System.out.println("服务器转发消息中");
        //遍历，所有的注册Selector上的SocketChannel，排除自身
        for(SelectionKey key:selector.keys()){
            //通过key，取出对应的SocketChannel
            SocketChannel channel = (SocketChannel)key.channel();
            if(channel != socketChannel){
                //将msg存储到Buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer写入通道
                channel.read(byteBuffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
