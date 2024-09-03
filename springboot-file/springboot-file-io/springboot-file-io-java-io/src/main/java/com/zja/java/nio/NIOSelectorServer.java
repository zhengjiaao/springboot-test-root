package com.zja.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO 服务器端示例
 * @Author: zhengja
 * @Date: 2024-09-03 9:12
 * 下面是使用 Selector 的示例代码，展示如何在 Java NIO 中实现一个简单的多路复用服务器，能够处理多个客户端连接。
 */
public class NIOSelectorServer {

    public static void main(String[] args) {
        // supportClients();

        supportMultipleClients();
    }

    /**
     * 服务器端：
     * 使用 Selector 和 ServerSocketChannel 创建一个多路复用服务器，能够处理多个客户端连接。
     * 当有客户端连接时，接受连接并注册到选择器。
     * 读取客户端发送的消息并回显。
     */
    public static void supportClients() {
        try {
            // 创建选择器
            Selector selector = Selector.open();

            // 创建 ServerSocketChannel
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(8080));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started on port 8080");

            while (true) {
                // 选择准备好的通道
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();

                    if (key.isAcceptable()) {
                        // 接受新连接
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Client connected: " + socketChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        // 读取数据
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        int bytesRead = socketChannel.read(buffer);

                        if (bytesRead == -1) {
                            // 关闭连接
                            socketChannel.close();
                            System.out.println("Client disconnected: " + socketChannel.getRemoteAddress());
                        } else {
                            buffer.flip();
                            String message = new String(buffer.array(), 0, bytesRead);
                            System.out.println("Received: " + message);
                            // Echo back the received message
                            socketChannel.write(buffer);
                        }
                    }
                    selectedKeys.remove(); // 移除当前的 key
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 实现一个能够处理多个客户端发送数据的 NIO 服务器
     * <p>
     * 处理多个客户端同时发送数据的情况可以通过 Java NIO 中的 Selector 和 SocketChannel 来实现。具体步骤如下：
     * 1.使用 Selector: 利用 Selector 监听多个通道（客户端连接），以便在有事件发生时（如连接、读、写）进行处理。
     * 2.非阻塞模式: 将 SocketChannel 设置为非阻塞模式，这样可以在不阻塞线程的情况下处理多个客户端。
     * 3.轮询事件: 在主循环中轮询已注册的事件，根据事件类型（如连接、可读、可写）调用相应的处理逻辑。
     * </p>
     *
     * <p>
     * 代码说明:
     * 1.ServerSocketChannel: 用于监听客户端连接。
     * 2.Selector: 用于多路复用，监听多个通道的事件。
     * 3.非阻塞模式: 通过 configureBlocking(false) 将通道设置为非阻塞模式。
     * 4.事件处理:
     * OP_ACCEPT: 当有客户端连接时，接受连接并注册为可读状态。
     * OP_READ: 当有数据可读时，读取数据并处理（如回显）。
     * </p>
     *
     * 运行步骤：
     * 启动服务器端代码。
     * 启动多个客户端实例，连接到服务器并发送消息。
     * <p>
     * 注意事项：
     * 线程安全: 如果需要在多个线程中处理，确保对共享数据的访问是线程安全的。
     * 资源管理: 确保在适当的时候关闭通道和选择器，以免资源泄漏。
     * </p>
     */
    public static void supportMultipleClients() {
        try {
            // 创建选择器
            Selector selector = Selector.open();

            // 创建 ServerSocketChannel
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(8080));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started on port 8080");

            while (true) {
                // 选择准备好的通道
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();

                    if (key.isAcceptable()) {
                        // 接受新连接
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Client connected: " + socketChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        // 读取数据
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        int bytesRead = socketChannel.read(buffer);

                        if (bytesRead == -1) {
                            // 关闭连接
                            socketChannel.close();
                            System.out.println("Client disconnected: " + socketChannel.getRemoteAddress());
                        } else {
                            buffer.flip();
                            String message = new String(buffer.array(), 0, bytesRead);
                            System.out.println("Received: " + message);
                            // Echo back the received message
                            socketChannel.write(buffer);
                        }
                    }
                    selectedKeys.remove(); // 移除当前的 key
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
