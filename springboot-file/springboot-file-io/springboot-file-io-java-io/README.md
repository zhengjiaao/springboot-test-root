# Java IO 和 NIO

在 Java 中，`IO` 和 `NIO` 是两种处理输入输出的方式，各自有不同的特点和适用场景。以下是对它们的详细介绍：

## 1. 基本概念

- **Java IO**：
    - 基于阻塞式流的模型，主要通过字符流和字节流来处理输入输出。
    - 操作是顺序的，线程在执行 IO 操作时会被阻塞。
- **Java NIO**：
    - 基于缓冲区和通道的模型，支持非阻塞 IO。
    - 允许在单个线程中管理多个通道，适合高并发场景。

## 2. 主要特性

| 特性            | Java IO                       | Java NIO                              |
| :-------------- | :---------------------------- | :------------------------------------ |
| **阻塞/非阻塞** | 阻塞式                        | 非阻塞式                              |
| **模型**        | 面向流（Input/Output Stream） | 面向缓冲区（Buffer）和通道（Channel） |
| **多路复用**    | 不支持多路复用                | 支持多路复用（Selector）              |
| **性能**        | 在小规模应用中表现良好        | 在高并发和大数据量应用中表现更佳      |
| **复杂性**      | 简单易用，适合基本应用        | 复杂度较高，适合高性能应用            |

1、Java IO (Input/Output)特点：

- **阻塞式**：Java IO 是基于流的，操作是阻塞的，即在执行 IO 操作时，线程会被阻塞，直到操作完成。
- **简单易用**：Java IO 提供了一组简单的类和接口，如 `InputStream` 和 `OutputStream`，适合处理文件、网络等基本的 IO 操作。
- **面向流**：IO 操作是基于字节流和字符流的，分别用于处理二进制数据和文本数据。

2、Java NIO (New Input/Output)特点：

- **非阻塞式**：NIO 支持非阻塞 IO，允许线程在等待 IO 操作时继续执行其他任务。
- **基于通道和缓冲区**：NIO 使用 `Channel` 和 `Buffer` 的概念，通道可以读写数据，缓冲区则用于存储数据。
- **选择器**：NIO 提供了 `Selector` 类，可以监听多个通道的状态变化，适用于高并发场景。

## 3. 主要组件比较

- **Java IO 组件**：
    - `InputStream` / `OutputStream` 用于读取和写入文件的字节流
    - `FileInputStream` / `FileOutputStream`用于读取和写入文件的字节流
    - `BufferedInputStream` 和 `BufferedOutputStream`用于对字节流进行缓冲，提高读取和写入效率
    - `BufferedReader` / `BufferedWriter` 用于读取和写入文件的字符流
    - `FileReader` 和 `FileWriter`用于读取和写入文件的字符流
    - `PrintWriter`用于以文本方式写入文件，提供了方便的打印功能
- **Java NIO 组件**：
    - `Buffer`（如 `ByteBuffer`用于存储字节数据的缓冲区）
    - `Channel`（如 `FileChannel`用于读写文件的通道, `SocketChannel`用于网络通信）
    - `Selector`用于多路复用，可以同时处理多个通道。
    - `ServerSocketChannel` / `SocketChannel`用于网络通信

## 4. 适用场景

- **Java IO**：
    - 适合简单的文件操作、网络通信等场景。
    - 当并发连接较少时表现良好，易于实现。
- **Java NIO**：
    - 适合高并发的网络应用，如网络服务器、聊天应用等。
    - 适合处理大文件或需要同时处理多个连接的场景。

## 5. 示例代码对比

### Java IO 示例

```java
import java.io.*;

public class JavaIOExample {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader("input.txt");
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

```java
import java.io.*;

public class JavaIOExample {
  public static void main(String[] args) {
    try (FileInputStream fis = new FileInputStream("file.txt")) {
      int data;
      while ((data = fis.read()) != -1) {
        System.out.print((char) data);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
```

### Java NIO 示例

``` java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class JavaNIOExample {
    public static void main(String[] args) {
        try {
            Path path = Paths.get("input.txt");
            Files.lines(path, StandardCharsets.UTF_8)
                 .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

```java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class JavaNIOExample {
  public static void main(String[] args) {
      try (
              FileChannel fileChannel = FileChannel.open(Paths.get("file.txt"), StandardOpenOption.READ)) {
          ByteBuffer buffer = ByteBuffer.allocate(1024);
          int bytesRead = fileChannel.read(buffer);
          while (bytesRead != -1) {
              buffer.flip();
              while (buffer.hasRemaining()) {
                  System.out.print((char) buffer.get());
              }
              buffer.clear();
              bytesRead = fileChannel.read(buffer);
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}
```

## 总结

- **Java IO** 适合简单的文件和网络操作，易于使用，但在高并发场景下效率较低。
- **Java IO** 适合简单、阻塞的输入输出操作，易于使用，但在高并发场景下性能不足。
- **Java NIO** 适合高性能和高并发的应用，支持非阻塞 IO 和多路复用，但相对复杂。
- **Java NIO** 适合处理高并发和大数据量的场景，通过非阻塞 IO 和选择器提高了性能，但相对复杂。