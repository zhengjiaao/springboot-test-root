package com.dist.io.DataInputStream;

import org.junit.Test;

import java.io.*;

/**
 *过滤流：DataInputStream和DataOutputStream  (处理流)
 *
 * DataInputStream和DataOutputStream，使用已经存在的节点流来构造，提供了读写Java中的基本数据类型的功能
 */
public class TestDataOutputStream {

    @Test
    public void test(){

        testDataOutputStream();
        testDataInputStream();
    }

    public void testDataOutputStream(){

        // 节点流FileOutputStream直接以D:\aaa\bbb.txt作为数据源操作
        FileOutputStream fileOutputStream = null;
        DataOutputStream out =null;
        try {
            fileOutputStream = new FileOutputStream("D:\\aaa\\bbb.txt");
            // 过滤流BufferedOutputStream进一步装饰节点流，提供缓冲写
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            // 过滤流DataOutputStream进一步装饰过滤流，使其提供基本数据类型的写
            out = new DataOutputStream(bufferedOutputStream);
            out.writeInt(3);
            out.writeBoolean(true);
            out.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (out !=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void testDataInputStream(){
        // 此处输入节点流，过滤流正好跟上边输出对应，可举一反三
        DataInputStream in = null;
        try {
            in = new DataInputStream(new BufferedInputStream(
                    new FileInputStream("D:\\aaa\\bbb.txt")));
            System.out.println(in.readInt()); //3
            System.out.println(in.readBoolean()); //true
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
