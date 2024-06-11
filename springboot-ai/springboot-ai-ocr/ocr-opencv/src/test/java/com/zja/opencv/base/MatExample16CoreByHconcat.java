package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * java openCV4.x 入门-Core之图像连接
 *
 * @Author: zhengja
 * @Date: 2024-06-07 17:43
 */
public class MatExample16CoreByHconcat {


    /**
     * Core.hconcat(src, dst) 水平连接(横向拼接): 将多个矩阵水平地连接在一起，图像的行数必须相同
     */
    @Test
    public void test_Core_hconcat() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat img1 = Imgcodecs.imread("input/image.jpg");

        List<Mat> list = new ArrayList<>();
        list.add(img1);
        list.add(img1);

        Mat dst = new Mat();
        Core.hconcat(list, dst); // 水平连接(横向拼接): 将多个矩阵水平地连接在一起，图像的行数必须相同
        HighGui.imshow("dst", dst);
        HighGui.waitKey();

    }

    /**
     * 垂直连接： 将多个矩阵垂直地连接在一起。图像的列数必须相同
     */
    @Test
    public void test_Core_vconcat() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img1 = Imgcodecs.imread("input/image.jpg");

        // 一个包含要连接的 Mat 对象的列表，行数必须相同
        List<Mat> list = new ArrayList<>();
        list.add(img1);
        list.add(img1);

        Mat dst = new Mat();
        Core.vconcat(list, dst); // 垂直连接： 将多个矩阵垂直地连接在一起。图像的列数必须相同
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }

}
