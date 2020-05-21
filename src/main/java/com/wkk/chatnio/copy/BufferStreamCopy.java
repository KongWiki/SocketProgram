package com.wkk.chatnio.copy;

import java.io.*;

/**
 * @Time: 2020/5/19下午9:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class BufferStreamCopy implements FileCopyRunner {
    @Override
    public void copyFile(File source, File target) {
        try (
                InputStream input = new FileInputStream(source);
                OutputStream output = new FileOutputStream(target);
        ) {
            byte[] buffer = new byte[1024];
            int leng;
            while ((leng = input.read(buffer)) != -1){
                output.write(buffer, 0, leng);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "BufferStreamCopy";
    }
}
