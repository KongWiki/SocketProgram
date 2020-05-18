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
    private ChatServer server;
    private Socket socket;

    public ChatHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 存储新上线用户
            server.add(socket);

            // 读取用户发送的消息
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            String msg = null;
            while ((msg = reader.readLine()) != null) {
                String fwdMsg = "客户端[" + socket.getPort() + "]: " + msg + "\n";
                System.out.print(fwdMsg);

                // 将消息转发给聊天室里在线的其他用户
                server.forwardMSG(socket, fwdMsg);

                // 检查用户是否准备退出
                if (server.readQuit(msg)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 从服务器移除退出的用户
                server.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
