package com.wkk.chatnio.client;

import com.wkk.chatbio.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Time: 2020/5/20下午8:40
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

                if(chatClient.readyQuit(input)){
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
