package com.wkk.chatnio.buffer;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @Time: 2020/5/21上午10:16
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class NioBase {
    public static void main(String[] args) {
        // 初始化一个Buffer
        IntBuffer buffer = IntBuffer.allocate(1024);

        // 向Buffer中随机写入10个数字
        for (int i = 0; i < 10; i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        // 切换为读模式
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.print(buffer.get() + " ");
        }

    }
}
