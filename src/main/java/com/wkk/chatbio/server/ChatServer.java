package com.wkk.chatbio.server;

import com.wkk.chatbio.Constant;

import javax.naming.ldap.SortKey;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**1
 * @Time: 2020/5/17下午8:58
 * @Author: kongwiki
 * @Email: kongwiki@163.com
 */
public class ChatServer implements Constant {
    private ServerSocket server = null;
    private ExecutorService executor;

    private Map<Integer, Writer> connectionClients;

    public ChatServer() {
        this.connectionClients = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(10);
    }

    // 新增用户添加到map
    public synchronized void add(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            connectionClients.put(port, writer);
            System.out.println("客户端 [" + port + "] 已经连接服务器");
        }
    }

    // 下线客户移除
    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectionClients.containsKey(port)) {
                connectionClients.get(port).close();
            }
            connectionClients.remove(port);
            System.out.println("客户端 [" + port + "]已经断开连接");
        }
    }

    // 转发消息
    public synchronized  void forwardMSG(Socket socket, String msg) throws IOException {
        // 转发给除去发送者的其他在线用户
        for (Integer integer : connectionClients.keySet()) {
            if (!integer.equals(socket.getPort())) {
                Writer writer = connectionClients.get(integer);
                writer.write(msg);
                writer.flush();
            }
        }
    }

    // 全部转发
    public synchronized void forwardAllMSG(String msg) throws IOException {
        for (Integer integer : connectionClients.keySet()) {
            connectionClients.get(integer).write(msg);
            connectionClients.get(integer).flush();

        }
    }

    public synchronized void close(){
        if(server != null){
            try {
                server.close();
                System.out.println("关闭服务器");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 绑定监听端口
            server = new ServerSocket(DEFAULT_PORT);
            System.out.println("[启动服务器， 监听端口 " + DEFAULT_PORT
                    + "]");
            while (true) {
                // 等待客户端连接
                Socket socket = server.accept();
                // 创建新的线程, 用于处理客户端数据
//                new Thread(new ChatHandler(socket, this)).start();
                executor.execute(new ChatHandler(socket, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
