package com.wkk.chatnio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Time: 2020/5/19下午9:26
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class NioBufferCopy implements FileCopyRunner {
    @Override
    public void copyFile(File source, File target) {
        try (
                FileChannel input = new FileInputStream(source).getChannel();
                FileChannel output = new FileOutputStream(target).getChannel();
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (input.read(buffer) != -1){
                buffer.flip();
                while (buffer.hasRemaining()) {
                    output.write(buffer);
                }
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "NioBufferCopy";
    }
}
