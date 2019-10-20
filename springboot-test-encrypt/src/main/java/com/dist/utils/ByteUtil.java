package com.dist.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

/**
 * byte工具
 * @author yinxp@dist.com.cn
 * @date 2018/12/6
 */
public abstract class ByteUtil {

    /**
     * 将object对象转byte数组
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {
        if (obj == null) {
            return null;
        }
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将流转换成字节数组进行输出
     * @param in
     * @param size
     * @return
     */
    public static byte[] streamToByteArray(InputStream in,int size) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[size];
        int n = 0;
        try {
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
