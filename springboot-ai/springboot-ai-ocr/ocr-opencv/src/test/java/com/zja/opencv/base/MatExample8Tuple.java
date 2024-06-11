package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * java openCV4.x 入门-Mat之多维元组(Tuple)
 *
 * @Author: zhengja
 * @Date: 2024-06-07 13:27
 */
public class MatExample8Tuple {

    /**
     * Mat Tuple 访问元组
     * Mat.Tuple ：有Tuple2、Tuple3、Tuple4 这三个类，分别用来表示一个具有两个元素、三个元素、四个元素的元组。它用于表示图像中的像素值。
     * Mat Atable 元素操作 : 接口允许你以各种组合形式获取和设置矩阵元素
     */
    @Test
    public void test_Mat_Tuple() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(1, 2, CvType.CV_32FC4);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println("mat.dump() = \n" + mat.dump());

        // Mat.Atable at(java.lang.Class clazz, int[] idx) // clazz	指定获取值的类型，可以是Byte.class、Short.class、Integer.class、Float.class或Double.class ; idx 指定像素的位置，以数组形式表示
        Mat.Atable<Float> at = mat.at(Float.class, 0, 0);
        // 获取像素值
        Mat.Tuple4<Float> v4c = at.getV4c();
        System.out.println("B通道值 = " + v4c.get_0()); // 获取元素
        System.out.println("G通道值 = " + v4c.get_1());
        System.out.println("R通道值 = " + v4c.get_2());
        System.out.println("透明度  = " + v4c.get_3());
        // 设置值
        Mat.Tuple4<Float> tuple4 = new Mat.Tuple4<>(11f, 22f, 33f, 44f);
        at.setV4c(tuple4);
        System.out.println("mat.dump() = \n" + mat.dump());
    }

}
