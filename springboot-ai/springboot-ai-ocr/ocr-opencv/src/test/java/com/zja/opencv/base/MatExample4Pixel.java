package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;

/**
 * java openCV4.x 入门-Mat之像素读写
 *
 * @Author: zhengja
 * @Date: 2024-06-07 10:12
 */
public class MatExample4Pixel {

    // Mat(矩阵) 像素值读写
    @Test
    public void test_Mat_PixelValue_Read_Write() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 写像素值

        // 定一个空白的矩阵，单通道、整数
        // put(int[] idx, byte[] data, int offset, int length)
        Mat zeros = Mat.zeros(new Size(5, 5), CvType.CV_8UC1);

        System.out.println("赋值前 =  \n" + zeros.dump());

        // 定义坐标
        int[] idx = {1, 1};
        // 定义值
        byte[] data = {1, 2, 3, 4, 5, 6, 7};
        // 定义偏移量 默认 0 , 表示从左开始赋值，-1 表示从右开始赋值
        // int offset = 0;
        int offset = -1;
        // 定义赋值的个数 默认 data.length
        int length = data.length;
        // int length = 5;

        zeros.put(idx, data, offset, length);

        System.out.println("赋值后 =  \n" + zeros.dump());

        System.out.println("=========偏移量========");


        // 定一个都是1的矩阵，单通道、整数，方便观看偏移量
        // put(int[] idx, byte[] data, int offset, int length)
        Mat ones = Mat.ones(new Size(5, 5), CvType.CV_8UC1);

        System.out.println("赋值前 =  \n" + ones.dump());

        // 定义坐标
        int[] idx2 = {1, 1};
        // 定义值
        byte[] data2 = {1, 2, 3, 4, 5, 6, 7};
        // 定义偏移量 默认 0 , 表示从左开始赋值，-1 表示从右偏移1个
        // int offset2 = 0;
        int offset2 = -1;
        // 定义赋值的个数 默认 data.length
        int length2 = data.length;
        // int length2 = 5;

        ones.put(idx2, data2, offset2, length2);

        System.out.println("赋值后 = \n " + ones.dump());

        // put(int row, int col, double... data)
        Mat zeros3 = Mat.zeros(new Size(3, 3), CvType.CV_8UC3);
        zeros3.put(1, 1, 1, 2, 3, 4, 5, 6);
        System.out.println("zeros3.dump()=\n" + zeros3.dump());

        // put(int row,int col, float[] data)
        Mat eye = Mat.eye(3, 3, CvType.CV_32FC1);
        float[] floatData = {11.1f, 22.2f};
        eye.put(0, 1, floatData);
        System.out.println("eye.dump()=\n" + eye.dump());

        // setTo(Scalar valve, Mat mask) 将矩阵的指定区域设置为给定的值。
        // Mat mask（掩码） 覆盖一层，修改覆盖的地方，未覆盖的不修改
        // 单通道测试
        Mat ones1 = Mat.ones(5, 5, CvType.CV_8UC1);
        // 创建掩码
        Mat mask1 = Mat.zeros(5, 5, CvType.CV_8UC1);
        // mask1.put(0, 0, 255);
        mask1.put(0, 0, 255, 1);
        System.out.println("mask1.dump()=\n" + mask1.dump());
        ones1.setTo(new Scalar(30)); // 不用掩码，设置所有的值
        // ones1.setTo(new Scalar(30), mask1);
        System.out.println("ones1.dump()=\n" + ones1.dump());

        // 多通道测试 3通道
        Mat ones2 = Mat.ones(5, 5, CvType.CV_8UC3);
        // 创建掩码
        Mat mask2 = Mat.zeros(5, 5, CvType.CV_8UC1);
        // mask2.put(0, 0, 255);
        mask2.put(0, 0, 255);
        System.out.println("mask2.dump()=\n" + mask2.dump());
        ones2.setTo(new Scalar(30, 2), mask2);
        System.out.println("ones2.dump()=\n" + ones2.dump());

        System.out.println("=========读取像素值-start========");

        // 读像素值
        System.out.println("eye.dump() = \n" + eye.dump());
        double[] doubles = eye.get(0, 1);  // 取值：第0行，第1列
        System.out.println("doubles[0]=" + doubles[0]);

        // get(int row,int col, float[] data)
        float[] fdata = new float[1];
        eye.get(1, 1, fdata);
        System.out.println("fdata[0]=" + fdata[0]);
    }

    // Mat(矩阵) col、row像素值Mat(矩阵)
    @Test
    public void test_Mat_PixelValue_Mat() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        int index = 0;
        // 获取行数
        int rows = mat.rows();
        // 获取列数
        int cols = mat.cols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mat.put(r, c, index);
                index++;
            }
        }

        System.out.println("mat.dump()= \n" + mat.dump());

        // col(int x)获取第x列的像素值，并以矩阵的形式返回 x-列的索引值
        Mat col = mat.col(0);
        System.out.println("col.dump()= \n" + col.dump());

        // row(int y)获取第y列的像素值，并以矩阵的形式返回 y-列的索引值
        Mat row = mat.row(1);
        System.out.println("row.dump()= \n" + row.dump());

        // colRange(int startcol,int endcol)[)
        Mat colRange = mat.colRange(0, 2);
        System.out.println("colRange.dump()=\n" + colRange.dump());

        // rowRange(int startrow，int endrow)获取指定行范围矩阵[)
        Mat rowRange = mat.rowRange(1, 3);
        System.out.println("rowRange.dump()=\n" + rowRange.dump());
        Mat rowRange_1 = mat.rowRange(new Range(1, 3));
        System.out.println("rowRange_1.dump()=\n" + rowRange_1.dump());

        // diag()
        Mat diag = mat.diag();
        System.out.println("diag.dump()=\n" + diag.dump());

        // diag(int d)+表示所在列的对角线上 -所在行的对角线上
        Mat diag_1 = mat.diag(1);
        Mat diag_2 = mat.diag(-2);
        System.out.println("diag_1.dump()=\n" + diag_1.dump());
        System.out.println("diag_2.dump()=\n" + diag_2.dump());

        // diag(Mat d)d 为对角矩阵，通过对焦矩阵生成矩阵。其他的点都是0
        Mat diagMat = Mat.diag(diag);
        System.out.println("diagMat.dump()=\n" + diagMat.dump());

        // submat(Range[] ranges) 相交的矩阵
        Range row_Range = new Range(0, 2);
        Range col_Range = new Range(1, 3);
        Mat submat = mat.submat(row_Range, col_Range);
        System.out.println("submat.dump()=\n" + submat.dump());

        // submab(int rowStart, int rowEnd, int colStart, int colEnd)
        Mat submat1 = mat.submat(0, 2, 1, 3);
        System.out.println("submat1.dump()=\n" + submat1.dump());
    }
}
