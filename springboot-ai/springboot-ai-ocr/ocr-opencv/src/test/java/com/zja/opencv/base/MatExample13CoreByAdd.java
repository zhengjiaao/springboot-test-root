package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Random;

/**
 * java openCV4.x 入门-Core之图像融合
 *
 * @Author: zhengja
 * @Date: 2024-06-07 16:04
 */
public class MatExample13CoreByAdd {

    /**
     * 逐元素和 ： add函数用于将两个矩阵或者矩阵与常量之间进行逐像素相加，并生成一个新的矩阵，其中每个像素表示对应位置上两者像素的和。
     */
    @Test
    public void test_Core_add_1() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 1. 计算两个数组(矩阵)的每个元素的和

        Mat src1 = new Mat(2, 2, CvType.CV_8UC1);
        src1.put(0, 0, 1, 2, 3, 4);
        // 克隆
        Mat src2 = src1.clone();
        src2.t();

        System.out.println("src1.dump() = \n" + src1.dump());
        System.out.println("src2.dump() = \n" + src2.dump());

        // add(Mat src1, Mat src2, Mat dst, Mat mask, int dtype) 第一个数组(矩阵)、第二个数组(矩阵)、结果数组(矩阵)、掩模、数据类型
        Mat dst = new Mat();
        Core.add(src1, src2, dst, new Mat(), -1);

        System.out.println("dst.dump() = \n" + dst.dump());
    }

    @Test
    public void test_Core_add_2() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 2.计算数组与标量之间的每个元素的和值.
        Mat src1 = new Mat(2, 1, CvType.CV_8UC2);
        src1.put(0, 0, 1, 2, 3, 4);

        System.out.println("src1.dump() = \n" + src1.dump());

        // add(Mat src1, Scalar src2, Mat dst, Mat mask, int dtype)
        Mat dst = new Mat();
        Core.add(src1, new Scalar(0, -1), dst, new Mat(), -1);

        System.out.println("dst.dump() = \n" + dst.dump());

    }


    // add 扩展示例：图像融合
    // 可以用它实现图像融合的效果。例如实现图像叠加、混合或透明度调整、亮度调整等功能。
    @Test
    public void test_Core_add_image() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat src1 = Imgcodecs.imread("input/Brush1.png");
        Mat src2 = Imgcodecs.imread("input/Brush2.png");
        Mat dst = new Mat();
        Core.add(src1, src2, dst);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }

    // 添加噪点
    @Test
    public void test_Core_add_image_2() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat src1 = Imgcodecs.imread("input/image2.jpg");
        // 创建随机矩阵，为了效果明显，像素值设置到最大
        Mat src2 = new Mat(src1.size(), src1.type());
        int rows = src2.rows();
        int cols = src2.cols();
        Random random = new Random(10);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (random.nextInt() < 6) {
                    src2.put(r, c, 255, 255, 255);
                }
            }
        }
        Mat dst = new Mat();
        Mat mask = new Mat();
        Core.add(src1, src2, dst, mask, -1);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();

    }

    /**
     * 缩放和: 计算一个缩放数组与另一个数组的和
     */
    @Test
    public void test_Core_scaleAdd() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 计算一个缩放数组与另一个数组的和
        // scaleAdd(Mat src1, double alpha, Mat src2, Mat dst)
        // 计算公式为: dst(I)=scale⋅src1(I)+src2(I)
        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Mat clone = mat.clone();
        clone.put(0, 0, 1, 0, 2);

        Mat dst = new Mat();
        Core.scaleAdd(mat, 10, clone, dst); // 10*src1+src2

        System.out.println("mat.dump() = \n" + mat.dump());
        System.out.println("clone.dump() = \n" + clone.dump());
        System.out.println("dst.dump() = \n" + dst.dump());
    }


    /**
     * 加权和: addWeighted用于将两个图像按照指定的权重进行融合。
     */
    @Test
    public void test_Core_addWeighted() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 计算两个数组的加权和
        // addWeighted(Mat src1, double alpha, Mat src2, double beta, double gamma, Mat dst, int dtype)
        // 计算公式为: dst(I)=saturate(src1(I)∗alpha+src2(I)∗beta+gamma) 其中I是一个多维数组元素的索引。在多通道数组的情况下，每个通道独立处理
        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Mat clone = mat.clone();
        clone.put(0, 0, 2, 2, 2);

        Mat dst = new Mat();
        Core.addWeighted(mat, 2, clone, 3, -1, dst); // 2*src1+3*src2-1

        System.out.println("mat.dump() = \n" + mat.dump());
        System.out.println("clone.dump() = \n" + clone.dump());
        System.out.println("dst.dump() = \n" + dst.dump());
    }
}
