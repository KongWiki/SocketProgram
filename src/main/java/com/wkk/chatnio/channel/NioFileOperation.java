package com.wkk.chatnio.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Time: 2020/5/21下午1:33
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class NioFileOperation {
    public static void read() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("aaa.txt");
        FileChannel inputChannel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        inputChannel.read(buffer);
        // 切换为读模式
        buffer.flip();
        while (buffer.hasRemaining()){
            byte b = buffer.get();
            System.out.println("Character: " + (char)b);
        }
        buffer.clear();
        fileInputStream.close();
    }

    public static void readAndWrite() throws IOException {
        FileInputStream input = new FileInputStream("aaa.txt");
        FileOutputStream output = new FileOutputStream("bbb.txt");
        FileChannel ipc = input.getChannel();
        FileChannel opc = output.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(512);
        while (ipc.read(buffer) != -1){
            byte[] bytes = "加油 --- 林俊杰".getBytes();
            buffer.put(bytes);
            buffer.flip();
            while (buffer.hasRemaining()){
                opc.write(buffer);
            }
            buffer.clear();
        }

        input.close();
        output.close();

    }

    public static void write() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("bbb.txt");
        FileChannel outChannel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] msg = "hello i'm wkk".getBytes();
        buffer.put(msg);
        buffer.flip();
        outChannel.write(buffer);

        fileOutputStream.close();


    }

    public static void main(String[] args) throws IOException {
//        write();
        readAndWrite();
    }
}
