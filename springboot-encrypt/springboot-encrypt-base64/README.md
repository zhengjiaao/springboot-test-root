# Java 的三种 Base64

**推荐使用 jdk util 包下的 Base64，效率高**

## Base64 编码步骤: 
- 第一步，将每三个字节作为一组，一共是24个二进制位   
- 第二步，将这24个二进制位分为四组，每个组有6个二进制位 (因为 6 位 2 进制最大数为 63)   
- 第三步，在每组前面加两个00，扩展成32个二进制位，即四个字节 
- 第四步，根据序号表(0-63)，得到扩展后的每个字节的对应符号就是Base64的编码值 

## sun 包下的 BASE64Encoder

>不推荐，效率不高

```java
    @Test
    public void test() throws IOException {
        final BASE64Encoder encoder = new BASE64Encoder();
        final BASE64Decoder decoder = new BASE64Decoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encode(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decodeBuffer(encodedText), "UTF-8"));
    }
```


## apache 包下的 Base64

> 不推荐，需要额外依赖，比 sun 包效率高一些，但是没有jdk提供的效率高

```java
    @Test
    public void test() throws UnsupportedEncodingException {
        final Base64 base64 = new Base64();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = base64.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(base64.decode(encodedText), "UTF-8"));
    }
```

## jdk util 包下的 Base64

> 推荐，效率高。
> Java 8 提供的 Base64 效率最高. 实际测试编码与解码速度, Java 8 的 Base64 要比 sun包下的要快大约 11 倍，比 Apache 的快大约 3 倍.

```java
    @Test
    public void test() throws IOException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
    }
```
