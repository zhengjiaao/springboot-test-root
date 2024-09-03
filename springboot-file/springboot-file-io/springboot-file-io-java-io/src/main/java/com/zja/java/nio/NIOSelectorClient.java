package com.zja.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO 客户端示例
 * @Author: zhengja
 * @Date: 2024-09-03 9:14
 */
public class NIOSelectorClient {
    /**
     * 客户端：
     * 创建一个 SocketChannel，连接到服务器。
     * 发送一条消息并读取服务器的回显。
     * <p>
     * 运行步骤：
     * 首先，运行服务器端代码，让它在端口 8080 上监听。
     * 然后，运行客户端代码，它将连接到服务器并发送消息。
     * </p>
     */
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8080));

            String message = "Hello, NIO Server!";
            ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer.put(message.getBytes());
            buffer.flip();
            socketChannel.write(buffer); // Send message to server

            // Read response
            buffer.clear();
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                System.out.println("Received from server: " + new String(buffer.array(), 0, bytesRead));
            }

            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
