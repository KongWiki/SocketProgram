package com.wkk.chatnio;

import java.io.File;

/**
 * @Time: 2020/5/19下午8:55
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */

public class FileCopyDemo {
    private static final int ROUNDS = 5;
    private static final File sourceBig = new File("/home/kongweikun/Downloads/test/ccc.txt");
    private static final File sourceMid = new File("/home/kongweikun/Downloads/test/bbb.txt");
    private static final File sourceSmal = new File("/home/kongweikun/Downloads/test/aaa.txt");
    private static final File target = new File("/home/kongweikun/Downloads/test/ddd.txt");

    public static void benchMark(FileCopyRunner test, File source, File target, String name) {
        long elapsed = 0L;
        for (int i = 0; i < ROUNDS; i++) {
            long start = System.currentTimeMillis();
            test.copyFile(source, target);
            elapsed = System.currentTimeMillis() - start;
            // 删除copy之后的文件
            target.delete();
        }
        System.out.println(test + name + " : " + elapsed / ROUNDS + "毫秒");
    }

    public static void main(String[] args) {
        FileCopyRunner noBufferStreamCopy = new NoBufferStreamCopy();
        FileCopyRunner bufferedStreamCopy = new BufferStreamCopy();
        FileCopyRunner nioBufferCopy = new NioBufferCopy();
        FileCopyRunner nioTransferCopy = new NioTransferCopy();

        // 测试 1
        System.out.println("-----------------------------------------");
        System.out.println("无Buffer的BIO");
        benchMark(noBufferStreamCopy, sourceBig, target, "大文件");
        benchMark(noBufferStreamCopy, sourceMid, target, "中等文件");
        benchMark(noBufferStreamCopy, sourceSmal, target, "小文件");
        System.out.println("-----------------------------------------");
        System.out.println("有Buffer的BIO");
        benchMark(bufferedStreamCopy, sourceBig, target, "大文件");
        benchMark(bufferedStreamCopy, sourceMid, target, "中等文件");
        benchMark(bufferedStreamCopy, sourceSmal, target, "小文件");
        //
        System.out.println("-----------------------------------------");
        System.out.println("NIO基础");
        benchMark(nioBufferCopy, sourceBig, target, "大文件");
        benchMark(nioBufferCopy, sourceMid, target, "中等文件");
        benchMark(noBufferStreamCopy, sourceSmal, target, "小文件");
        System.out.println("-----------------------------------------");
        System.out.println("NIO的TransferTo");
        benchMark(nioTransferCopy, sourceBig, target, "大文件");
        benchMark(nioTransferCopy, sourceMid, target, "中等文件");
        benchMark(nioTransferCopy, sourceSmal, target, "小文件");
    }
}
