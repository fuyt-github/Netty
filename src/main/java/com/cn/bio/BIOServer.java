package com.cn.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws Exception{
        //思路
        //1：创建线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
       // Socket
        ServerSocket serverSocket = new ServerSocket(666);
       System.out.println("服务器启动了");
       //BIO 没有客户端进行连接的话会阻塞
        System.out.println("线程等待....");
       while (true){
          final Socket socket = serverSocket.accept();
          System.out.println("连接到了一个客户端");
          newCachedThreadPool.execute(new Runnable() {
              public void run() {
                  handler(socket);
              }
          });
       }

    }

    public  static void handler(Socket socket){
        try {
            System.out.println("线程信息：id="+Thread.currentThread().getId());
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环读取客户端发送的数据
            while (true){
                //BIO 客户端没有发送数据时会阻塞
                System.out.println("read....");
                int i = inputStream.read(bytes);
                if(i != -1){
                    System.out.println("线程信息：id="+Thread.currentThread().getId());
                    System.out.println(new String(bytes,0,i));
                }else{
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
}
