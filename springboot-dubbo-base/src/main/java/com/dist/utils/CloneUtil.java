package com.dist.utils;

import java.io.*;

/**
 * 对象克隆工具
 */
public abstract class CloneUtil {

    public static final <T> T deepClone(T t) {
        Object rst = null;
        ByteArrayInputStream bio = null;
        ByteArrayOutputStream bao = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 对象写入流
            bao = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bao);
            oos.writeObject(t);

            // 从流中取出数据
            bio = new ByteArrayInputStream(bao.toByteArray());
            ois = new ObjectInputStream(bio);
            rst = ois.readObject();
        } catch (Exception e) {
                e.printStackTrace();
        }finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bio != null) {
                try {
                    bio.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bao != null) {
                try {
                    bao.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return (T) rst;
    }

}
