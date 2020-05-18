package com.wkk.chatbio.server;

import com.wkk.chatbio.Constant;

import java.io.*;
import java.net.Socket;

/**
 * @Time: 2020/5/17下午8:58
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class ChatHandler implements Runnable, Constant {
    Socket client;
    ChatServer chatServer;

    public ChatHandler(Socket socket, ChatServer chatServer){
        this.client = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        try {
            // 存储新上线用户
            chatServer.add(client);
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String msg = null;
            while ((msg = reader.readLine()) != null){
                // 读入客户端的消息
                String fwdMSG = "客户端 [" + client.getPort() + "] 消息为 " + msg + "\n";
                System.out.print(fwdMSG);
                // 读出转发给其他的客户端
                chatServer.forwardMSG(client, fwdMSG);
                // 检测是否退出
                if(QUIT.equalsIgnoreCase(msg)){
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                chatServer.removeClient(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
