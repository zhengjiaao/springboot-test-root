package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * java openCV4.x 入门-特殊的Mat类汇总(一)
 * <p>
 * 本节中列举的所有类的作用都可以用于读取和写入图像的不同类型的数据。
 * <p>
 * 学习地址：https://blog.csdn.net/qq_27185879/article/details/137351789?spm=1001.2014.3001.5501
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-06-07 13:35
 */
public class MatExample9MatClass {

    /**
     * MatOfByte : 一个特殊的Mat类，用于存储图像的字节（byte）数据
     */
    @Test
    public void test_Mat_MatOfByte() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 可以通过这个构造函数，可以将一个字节数组的一部分数据拷贝到MatOfByte对象中，用于表示图像或矩阵的字节数据。
        // 创建一个MatOfByte对象，并初始化其数据。
        byte data[] = {1, 2, 3, 4, 5, 6};
        // 从data数组的第3个位置开始拷贝，拷贝3个
        MatOfByte matOfByte = new MatOfByte(2, 3, data);
        System.out.println("matOfByte.dump() :\n" + matOfByte.dump());


        // 将给定的Mat对象转换为MatOfByte对象
        Mat mat = new Mat(1, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3);
        // 将mat转换为MatOfByte对象
        MatOfByte matOfByte2 = new MatOfByte(mat);
        System.out.println("matOfByte2.dump() :\n" + matOfByte2.dump());

        // 方法介绍
        // 将给定的字节数组转化为MatOfByte对象
        MatOfByte matOfByte3 = new MatOfByte(mat);
        byte[] matOfByteArray = matOfByte.toArray();
        matOfByte3.fromArray(matOfByteArray);
        System.out.println("matOfByte3.dump() = \n" + matOfByte3.dump());

        // fromList 用于将 List 类型的数据转换为 MatOfByte 对象。
        // 创建一个空的matOfByte
        MatOfByte matOfByte4 = new MatOfByte();
        // 创建bl集合
        List<Byte> bl = new ArrayList<>();
        bl.add((byte) 1);
        bl.add((byte) 2);
        bl.add((byte) 3);
        matOfByte4.fromList(bl);
        System.out.println("matOfByte4.dump() :\n " + matOfByte4.dump());

        // toArray 将MatOfByte对象转换为byte数组
        byte[] array = matOfByte.toArray();
        System.out.println("byte[] array = " + array.toString());

        // toList  将MatOfByte对象转换为byte集合
        List<Byte> ofByteList = matOfByte.toList();
        System.out.println("ofByteList = " + ofByteList);
    }

    /**
     * MatOfDouble类，可以方便地进行双精度浮点数类型的矩阵数据的处理和操作
     * <p>
     * 用法参考：MatOfByte，一样
     * </p>
     */
    @Test
    public void test_Mat_MatOfDouble() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(1, 3, CvType.CV_64FC1);
        mat.put(0, 0, 1, 2, 3);
        System.out.println("mat.dump() :\n " + mat.dump());
        // 构造函数
        MatOfDouble matOfDouble = new MatOfDouble(mat); // mat数据类型需为CV_64FC1,且需要1行或1列
        System.out.println("matOfDouble.dump() :\n " + matOfDouble.dump());

    }

    /**
     * MatOfFloat类的主要作用是提供了一种方便的方式来表示和操作浮点数类型的矩阵数据
     * <p>
     * 用法参考：MatOfByte
     * </p>
     */
    @Test
    public void test_Mat_MatOfFloat() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(1, 3, CvType.CV_32FC1);
        mat.put(0, 0, 1, 2, 3);
        MatOfFloat matOfFloat = new MatOfFloat(mat); // mat数据类型需为 CV_32FC1,且需要1行或1列
        System.out.println("matOfFloat.dump() :\n " + matOfFloat.dump());
    }

    /**
     * MatOfFloat4类可以用来存储4个浮点数的矩阵
     * <p>
     * 用法参考：MatOfByte
     * </p>
     */
    @Test
    public void test_Mat_MatOfFloat4() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(1, 3, CvType.CV_32FC4);
        MatOfFloat4 matOfFloat = new MatOfFloat4(mat); // mat数据类型需为 CV_32FC4,且需要1行或1列
        System.out.println("matOfFloat4.dump() :\n " + matOfFloat.dump());

    }

    /**
     * MatOfFloat6类可以用来存储6个浮点数的矩阵
     * <p>
     * 用法参考：MatOfByte
     * </p>
     */
    @Test
    public void test_Mat_MatOfFloat6() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(3, 1, CvType.CV_32FC(6));
        MatOfFloat6 matOfFloat = new MatOfFloat6(mat); // mat数据类型需为 CV_32FC(6),且需要1行或1列
        System.out.println("matOfFloat6.dump() :\n " + matOfFloat.dump());

    }

    /**
     * MatOfInt类是用来表示一个整数类型的矩阵
     * <p>
     * 用法参考：MatOfByte
     * </p>
     */
    @Test
    public void test_Mat_MatOfInt() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(1, 3, CvType.CV_32SC1);
        MatOfInt matOfInt = new MatOfInt(mat); // mat数据类型需为 CV_32SC1,且需要1行或1列
        System.out.println("matOfInt.dump() :\n " + matOfInt.dump());

    }

    /**
     * MatOfInt4用来存储和操作四个整数类型的矩阵
     * <p>
     * 用法参考：MatOfByte
     * </p>
     */
    @Test
    public void test_Mat_MatOfInt4() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(3, 1, CvType.CV_32SC4);
        MatOfInt4 matOfInt = new MatOfInt4(mat); // mat数据类型需为 CV_32SC4,且需要1行或1列
        System.out.println("matOfInt4.dump() :\n " + matOfInt.dump());

    }

}
