package com.wkk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Time: 2020/5/17下午2:53
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class Server {
    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        final String QUIET = "quiet";
        ServerSocket server = null;
        try {
            server = new ServerSocket(DEFAULT_PORT);
            System.out.println("[启动服务器， 监听端口 " + DEFAULT_PORT
                    + "]");
            while (true) {
                // 等待链接
                Socket client = server.accept();
                System.out.println("[客户端 " +
                        client.getPort() + " 已经链接]");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream()));

                // 读取客户端数据
                String msg = reader.readLine();
                if (!msg.equalsIgnoreCase(QUIET)) {
                    System.out.println("[客户端 " + client.getPort() + "发送数据 " + msg + "]");
                    // 返回个客户端数据
                    writer.write("echo " + msg + "\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
