package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * java openCV4.x 入门-Core之广义矩阵乘法运算
 * <p>
 * 广义矩阵乘法：
 * gemm其名称来源于 “General Matrix Multiply”，即广义矩阵乘法。此方法与matMul()方法有一样的作用，不过此方法支持第三个矩阵。
 * <p>
 * <p>
 * 学习地址(算法规则)：https://blog.csdn.net/qq_27185879/article/details/137588727?spm=1001.2014.3001.5501
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-06-07 16:25
 */
public class MatExample14CoreByGemm {

    /**
     * 实数矩阵（单通道）
     * <p>
     * 1.计算 ( C i j ) (C_{ij})(Cij) 的值，即将第一个矩阵 A的第 i ii 行与第二个矩阵 B 的第 j jj 列对应元素相乘后相加得到。
     * 2.将计算结果赋值给结果矩阵C的对应位置。
     * </p>
     */
    @Test
    public void test_Core_gemm() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat A = new Mat(2, 3, CvType.CV_32FC1);
        A.put(0, 0, 1, 2, 3, 4, 5, 6);

        Mat B = new Mat(3, 2, CvType.CV_32FC1);
        B.put(0, 0, 3, 4, 5, 6, 7, 8);

        Mat C = new Mat();
        Core.gemm(A, B, 1, new Mat(), 1, C);
        System.out.println("A.dump() = \n" + A.dump());
        System.out.println("B.dump() = \n" + B.dump());
        System.out.println("C.dump() = \n" + C.dump());

    }

    @Test
    public void test_Core_gemm_2() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat src1 = new Mat(2, 3, CvType.CV_32FC1);
        src1.put(0, 0, 1, 2, 3, 4, 5, 6);
        Mat src2 = new Mat(3, 2, CvType.CV_32FC1);
        src2.put(0, 0, 3, 4, 5, 6, 7, 8);
        Mat dst = new Mat();
        Mat src3 = new Mat(3, 3, CvType.CV_32FC1);
        src3.put(0, 0, 1, 2, 3, 4);
        Core.gemm(src1, src2, 2, src3, 1, dst, Core.GEMM_1_T + Core.GEMM_2_T);
        System.out.println("src1.dump() = \n" + src1.dump());
        System.out.println("src2.dump() = \n" + src2.dump());
        System.out.println("dst.dump() = \n" + dst.dump());
    }

}
