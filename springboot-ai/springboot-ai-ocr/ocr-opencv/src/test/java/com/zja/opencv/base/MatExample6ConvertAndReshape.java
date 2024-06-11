package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * java openCV4.x 入门-Mat之转换、重塑与计算
 *
 * @Author: zhengja
 * @Date: 2024-06-07 10:10
 */
public class MatExample6ConvertAndReshape {

    // Mat 转换
    @Test
    public void test_convertTo() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // convertTo(Mat m, int rtype, double alpha, double beta)
        Mat m = new Mat();
        mat.convertTo(m, CvType.CV_16F, 0.5, 10);
        System.out.println("mat.dump()=\n" + mat.dump());
        System.out.println("m.dump()=\n" + m.dump());
        System.out.println("cvType.typeTostring(m.type())=" + CvType.typeToString(m.type()));
    }

    @Test
    public void test_convertTo_t() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // t() 对象转换（转置矩阵）：行列互换，转置矩阵的行数等于原矩阵的列数，列数等于原矩阵的行数。
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Mat t = mat.t();
        System.out.println("mat.dump() = \n" + mat.dump());
        System.out.println("t.dump() = \n" + t.dump());
    }

    @Test
    public void test_convertTo_image() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat imread = Imgcodecs.imread("input/image.jpg");
        Mat dst = new Mat();

        // convertTo 应用示例：把图片变亮，达到一个曝光的效果
        imread.convertTo(dst, imread.type(), 3); // 转为 3 通道
        HighGui.imshow("imread", imread); // 原图
        HighGui.imshow("dst", dst); // 转换后 曝光图

        // t() 应用示例：把图片旋转
        Mat t2 = dst.t();
        HighGui.imshow("t2", t2); // 转置 旋转图
        System.out.println("t2.channels()=" + t2.channels());

        HighGui.waitKey();
    }

    // Mat 重塑
    @Test
    public void test_Reshape() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(3, 3, CvType.CV_8UC2);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println("mat.() = " + mat);
        System.out.println("mat.dump() = \n" + mat.dump());

        // reshape(int cn, int[] newshape)
        int[] newshap = {2, 3};
        Mat reshape = mat.reshape(3, newshap); // 通道数、一个整数数组，指定重塑后的矩阵的行数和列数
        System.out.println("reshape = " + reshape);
        System.out.println("reshape.dump() = \n" + reshape.dump());

        // clone()
        Mat clone = mat.clone();
        System.out.println("mat.mul(clone) = \n" + mat.mul(clone).dump());
    }

    @Test
    public void test_Reshape_image() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 应用示例：把图片三通道变成单通道，并转为灰度图
        Mat imread = Imgcodecs.imread("input/image.jpg");
        Mat dst = new Mat();
        imread.convertTo(dst, imread.type(), 3, 0); // 3 通道

        Mat t2 = dst.t();
        Mat reshape2 = t2.reshape(1); // 转为单通道，单通道就是一个灰度图
        HighGui.imshow("r2", reshape2);

        // 指定高度
        Mat reshape3 = t2.reshape(1, 10);
        HighGui.imshow("r3", reshape3);

        HighGui.waitKey();
    }


    // Mat 计算
    @Test
    public void test_Calculate() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Calculate 乘积

        // matMul(Mat m) 矩阵的乘积：计算两个矩阵的乘积
        // 不演示

        // mul 逐元素乘积：执行逐元素的乘法运算
        // 创建3X3矩阵
        Mat mat1 = new Mat(new Size(3, 3), CvType.CV_32FC1);
        mat1.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Mat mat2 = new Mat();
        // 赋值矩阵
        mat1.copyTo(mat2);
        System.out.println("mat1.dump() ：\n" + mat1.dump());
        System.out.println("mat2.dump() ：\n " + mat2.dump());
        Mat mat = mat1.mul(mat2, 2);// 另一个Mat对象，表示要与当前矩阵进行乘法运算的矩阵; scale乘法因子,可选的缩放因子，用于在乘法运算后缩放结果矩阵。默认值为 1.0，表示不进行缩放
        System.out.println("mat.dump() ：\n " + mat.dump());


        // dot(Mat m) 点积(点乘运算):计算两个矩阵的点积（内积），返回结果为一个标量值
        // 创建两个矩阵
        Mat mat3 = new Mat(3, 3, CvType.CV_32F);
        mat3.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Mat mat4 = new Mat(3, 3, CvType.CV_32F);
        mat4.put(0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1);

        // 计算两个矩阵的点积
        double result = mat3.dot(mat4);
        // 打印结果
        System.out.println("mat3.dump() :\n " + mat3.dump());
        System.out.println("mat4.dump() :\n" + mat4.dump());
        System.out.println("Dot product: " + result);


        // inv(int method) 逆矩阵（求逆运算）:设A是一个n阶矩阵，若存在另一个n阶矩阵B，使得： AB=BA=E ，则称方阵A可逆，并称方阵B是A的逆矩阵。
        // 创建矩阵
        Mat mat5 = new Mat(3, 3, CvType.CV_32FC1);
        // 矩阵赋值
        mat5.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        // 计算矩阵的逆矩阵
        Mat inv = mat5.inv(Core.DECOMP_SVD);
        System.out.println("mat5.dump() :\n " + mat5.dump());
        System.out.println("inv.dump() :\n " + inv.dump());
    }

}
