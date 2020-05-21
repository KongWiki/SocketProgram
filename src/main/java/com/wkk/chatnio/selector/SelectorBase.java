package com.wkk.chatnio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * @Time: 2020/5/21下午6:21
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class SelectorBase {
    public static void main(String[] args) throws IOException {
        int[] ports = new int[5];
        int base = 9000;
        for (int i = 0; i < 5; i++) {
            ports[i] = base++;
        }
        Selector selector = Selector.open();

        for (int port : ports) {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            ServerSocket socket = server.socket();

            socket.bind(new InetSocketAddress(port));
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口: " + port);
        }

        while (true) {
            int num = selector.select();
            System.out.println("numbers: " + num);
            // 触发事件集
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("触发事件集有: " + selectionKeys.size());
            for (SelectionKey selectionKey : selectionKeys) {
                System.out.println("触发事件集类型有: " + selectionKey);
            }
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    iterator.remove();
                    System.out.println("获得客户端的链接" + client);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.clear();
                    while (channel.read(buffer) > 0);
                    buffer.flip();
                    channel.write(buffer);
                    System.out.println("读取: " + buffer + ", 来自于" + channel);
                    iterator.remove();
                }

            }

        }


    }

}
