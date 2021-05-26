package com.cn.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT  = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;
    public  GroupChatClient() throws Exception{
         selector = Selector.open();
         //连接服务器
        socketChannel = socketChannel.open(new InetSocketAddress("127.0.0.1",6667));
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        //将SocketChannel注册到Selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        userName = socketChannel.getRemoteAddress().toString().substring(1);
        System.out.println(userName + "is ok...............");
    }
    //向服务器发送消息
    public void sendInfo(String info){

        info = userName + "说，你好。。。。。。。" + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    //从服务器读取回复的数据
    public void readInfo(){
        try{
            int select = selector.select();
            if(select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        //得到对应的channel
                        SocketChannel channel = (SocketChannel)key.channel();
                        //得到一个Buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        //把读到缓冲区的数组转换为字符串
                        String s = new String(byteBuffer.array());
                        System.out.println(s.trim());
                    }
                }
                iterator.remove();
            }else{
                //System.out.println("没有可用的通道");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws  Exception{
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(){
            public void run(){
                while(true){
                    groupChatClient.readInfo();
                    try{
                        Thread.currentThread().sleep(2000);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }.start();
        //发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String nextLine = scanner.nextLine();
            groupChatClient.sendInfo(nextLine);
        }
    }
}
