package com.wkk.chatbio.client;

import com.wkk.chatbio.Constant;

import java.io.*;
import java.net.Socket;

/**
 * @Time: 2020/5/17下午8:58
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class ChatClient implements Constant {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    // 发送给服务器数据
    public void send(String msg) throws IOException {
        if (!socket.isOutputShutdown()) {
            writer.write(msg + "\n");
            writer.flush();
        }

    }

    // 接受服务器转发的信息
    public String receive() throws IOException {
        String msg = null;
        if (!socket.isInputShutdown()) {
            msg = reader.readLine();
        }
        return msg;
    }


    // 关闭
    public void close() {
        if (writer != null) {
            try {
                System.out.println("客户端" + socket.getLocalPort() + "关闭");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 准备退出
    public boolean readyQuit(String s) {
        return s.equalsIgnoreCase(QUIT);
    }

    // 开始
    public void start() {
        try {
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 等待用户输入信息
            new Thread(new UserInputHandler(this)).start();

            // 读取服务器数据
            String msg = null;
            while ((msg = receive()) != null) {
                System.out.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}
