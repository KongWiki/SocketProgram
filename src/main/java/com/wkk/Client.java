package com.wkk;

import java.io.*;
import java.net.Socket;

/**
 * @Time: 2020/5/17下午2:53
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class Client {

    public static void main(String[] args) {
        final String DEFAULT_SERVER_HOST = "localhost";
        final String QUIET = "quiet";
        final int DEFAULT_SERVER_PORT = 8888;
        try (
                Socket client = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
                // 创建IO流
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream()));

                // 等待用户输入信息
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        ) {

            // 获取输入信息
            String input = consoleReader.readLine();

            // 发送给服务器
            writer.write(input + "\n");
            writer.flush();
            // 获取服务器数据
            String accept = reader.readLine();
            System.out.println("[服务器 发送数据 " + accept + "]");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
