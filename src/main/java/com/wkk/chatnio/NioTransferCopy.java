package com.wkk.chatnio;

import java.io.*;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * @Time: 2020/5/19下午9:26
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class NioTransferCopy implements FileCopyRunner {
    @Override
    public void copyFile(File source, File target) {
        try (
                FileChannel inputChannel = new FileInputStream(source).getChannel();
                FileChannel ouputChannel = new FileOutputStream(target).getChannel();
        ) {
            for (long count = inputChannel.size(); count > 0;) {
                long transferTo = inputChannel.transferTo(inputChannel.position(), count, ouputChannel);
                count -= transferTo;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "NioTransferCopy";
    }
}
