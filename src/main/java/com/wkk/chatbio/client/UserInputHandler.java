package com.wkk.chatbio.client;

import com.wkk.chatbio.Constant;

import java.io.*;

/**
 * @Time: 2020/5/17下午8:58
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class UserInputHandler implements Runnable, Constant {
    ChatClient chatClient;

    public UserInputHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @Override
    public void run() {
        try {
            // 等待用户输入
            BufferedReader consoleReader = new BufferedReader(
                    new InputStreamReader(System.in));

            while (true) {
                // 获取用户输入数据
                String input = consoleReader.readLine();

                // 向服务器发送输入数据
                chatClient.send(input);

                if (chatClient.readyQuit(input)) {
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
