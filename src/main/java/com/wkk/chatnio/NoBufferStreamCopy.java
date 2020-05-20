package com.wkk.chatnio;

import java.io.*;

/**
 * @Time: 2020/5/19下午9:25
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class NoBufferStreamCopy implements FileCopyRunner {
    @Override
    public void copyFile(File source, File target) {
        try (
                InputStream input = new FileInputStream(source);
                OutputStream output = new FileOutputStream(target);
        ) {
            int read;
            while ((read = input.read())!= -1){
                output.write(read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "NoBufferStreamCopy";
    }
}
