package com.wkk.chatnio.client;

import com.wkk.chatnio.NioConstant;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @Time: 2020/5/20下午8:39
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class ChatClient implements NioConstant {
    private String host;
    private int port;
    private SocketChannel client;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    public ChatClient(){
        this(DEFAULT_HOST , DEFAULT_PORT);
    }

    public ChatClient(String host , int port){
        this.host = host;
        this.port = port;
    }

    // 检查用户是否准备退出
    public boolean readyQuit(String msg){
        return QUIT.equalsIgnoreCase(msg);
    }

    public void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        try {
            client = SocketChannel.open();
            // 设置非阻塞
            client.configureBlocking(false);

            selector = Selector.open();
            client.register(selector , SelectionKey.OP_CONNECT);
            client.connect(new InetSocketAddress(host , port));

            while(true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for(SelectionKey key : selectionKeys){
                    handles(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClosedSelectorException e){
            // 用户正常退出，产生的异常
        } finally {
            close(selector);
        }
    }

    private void handles(SelectionKey key) throws IOException {
        // CONNECT事件 - 连接就绪事件
        if(key.isConnectable()){
            SocketChannel client = (SocketChannel) key.channel();
            if(client.isConnectionPending()){
                client.finishConnect();
                // 处理用户的输入
                new Thread(new UserInputHandler(this)).start();
            }
            client.register(selector , SelectionKey.OP_READ);
        }
        // READ事件 - 服务器转发消息
        else if(key.isReadable()){
            SocketChannel client = (SocketChannel) key.channel();
            String msg = receive(client);
            if(msg.isEmpty()){
                // 服务器异常
                close(selector);
            }
            else{
                System.out.println(msg);
            }
        }
    }

    private String receive(SocketChannel client) throws IOException {
        // 写模式
        rBuffer.clear();
        while(client.read(rBuffer) > 0);
        // 写模式
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
    }

    public void send(String msg) throws IOException {
        if(msg.isEmpty()){
            return ;
        }

        // 写模式
        wBuffer.clear();
        wBuffer.put(charset.encode(msg));
        wBuffer.flip();
        while(wBuffer.hasRemaining()){
            client.write(wBuffer);
        }

        // 检查用户是否准备退出
        if(readyQuit(msg)){
            close(selector);
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}
