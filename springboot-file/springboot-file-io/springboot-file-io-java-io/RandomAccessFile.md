# Java IO RandomAccessFile

`RandomAccessFile` 是 Java IO 中的一个类，用于以随机访问的方式读取和写入文件。它允许你在文件的任意位置进行读写操作，而不是从头到尾顺序访问。

### 主要特点

1. **随机访问**：可以在文件的任意位置读写数据，适合处理大文件或需要频繁更新的文件。
2. **读写模式**：支持不同的文件访问模式，例如只读、只写和读写。
3. **文件指针**：内部维护一个文件指针，指示当前读写位置，可以通过 `seek()` 方法改变指针位置。

### 构造方法

```java
RandomAccessFile(String name, String mode)
```

- `name`：文件名。
- `mode`：访问模式，如 `"r"`（只读）、`"rw"`（读写）等。

### 常用方法

- `seek(long pos)`：将文件指针移动到指定位置。
- `read()`：读取下一个字节。
- `read(byte[] b)`：读取一定数量的字节到数组中。
- `write(byte[] b)`：写入字节数组。
- `writeInt(int v)`：写入一个整数。
- `readInt()`：读取一个整数。
- `getFilePointer()`：返回当前文件指针的位置。

### 示例代码

以下是一个使用 `RandomAccessFile` 的示例，展示如何在文件中随机读写数据。

```java
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileExample {
    public static void main(String[] args) {
        String filePath = "example.dat";

        // 写入数据
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.writeInt(123);         // 写入整数 123
            raf.writeUTF("Hello");     // 写入字符串 "Hello"
            raf.writeDouble(45.67);    // 写入双精度数 45.67
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取数据
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            raf.seek(0);  // 移动到文件开头
            int num = raf.readInt();              // 读取整数
            String str = raf.readUTF();            // 读取字符串
            double d = raf.readDouble();           // 读取双精度数

            System.out.println("Integer: " + num);
            System.out.println("String: " + str);
            System.out.println("Double: " + d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 代码说明

1. **写入数据**：
    - 创建一个 `RandomAccessFile` 对象并以读写模式打开文件。
    - 使用 `writeInt()`、`writeUTF()` 和 `writeDouble()` 方法写入不同类型的数据。
2. **读取数据**：
    - 重新打开文件并设置为只读模式。
    - 使用 `seek(0)` 方法将文件指针移到开头。
    - 读取之前写入的数据并打印。

### 适用场景

- 适合需要随机读写的文件操作，如数据库文件、配置文件等。
- 能够在不加载整个文件的情况下高效地处理大文件。

