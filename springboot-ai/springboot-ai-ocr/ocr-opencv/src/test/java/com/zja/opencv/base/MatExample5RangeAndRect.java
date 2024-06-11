package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;

/**
 * java openCV4.x 入门-Mat之局部区域读写及Range和Rect介绍
 *
 * @Author: zhengja
 * @Date: 2024-06-07 9:50
 */
public class MatExample5RangeAndRect {
    static {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Mat(矩阵) Range(范围)
    @Test
    public void test_Mat_Range() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // set(double[] vals)
        Range range = new Range();
        double[] vals = {10, 20};
        range.set(vals);
        System.out.println("range =" + range);

        // aLL
        Range all = Range.all();
        System.out.println("all =" + all);

        // empty()判断对象是否为空
        Range range1 = new Range();
        System.out.println("range1=" + range1);
        System.out.println("range1.empty()=" + range1.empty());

        // intersection(Range r1)计算交集
        Range range_1 = new Range(2, 10);
        Range range_2 = new Range(4, 12); //[4,10) 左边按最大的，右边按最小的，交集是包含左边，不包含右边
        // Range range_2 = new Range(11, 12); //[11,11)  两个一样的话，没有交集
        Range intersection = range_1.intersection(range_2);
        System.out.println("intersection =" + intersection); //[4,10)

        // shift(int delta)
        System.out.println("range_1.shift(-1)=" + range_1.shift(-1)); //[1, 9)
        System.out.println("range_2.shift(1)=" + range_2.shift(1)); //[5, 13)
        // size()
        System.out.println("range.size()=" + range.size());// 10
    }

    // Mat(矩阵) Rect()
    @Test
    public void test_Mat_Rect() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Rect rect = new Rect();

        // set(double[] vals)
        double[] vals = {0, 0, 10, 10};
        rect.set(vals);
        System.out.println("rect =" + rect); // rect ={0, 0, 10x10}

        // area()
        System.out.println("rect.area()=" + rect.area()); // ect.area()=100.0

        // br()
        System.out.println("rect.br()=" + rect.br()); // rect.br()={10.0, 10.0}

        // tl()
        System.out.println("rect.tl()=" + rect.tl());

        // contains(Point p) 判断点是否在矩形内
        System.out.println("rect.contains(new Point(5,5))=" + rect.contains(new Point(5, 5)));
        System.out.println("rect.contains(new Point(100,100))=" + rect.contains(new Point(100, 100)));

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

        // submab(int rowStart, int rowEnd, int colStart, int colEnd)
        Mat submat1 = mat.submat(0, 2, 1, 3); // row=[0,2),col=[1,3) 不包含最后一行/列
        System.out.println("submat1.dump()=\n" + submat1.dump());

        // submab(Rect roi) 返回当前矩阵的子矩阵
        Mat submat2 = mat.submat(new Rect(0, 0, 2, 2));
        System.out.println("submat2.dump()=\n" + submat2.dump());
    }

    @Test
    public void test_() {
        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        // Mat mat = new Mat(3, 3, CvType.CV_8UC3);
        // Mat mat = new Mat(3, 3, CvType.CV_16FC(2));

        // elemsize() 每个像素点上的通道数
        System.out.println("mat.elemsize()=" + mat.elemSize());
        // elemSize1() 每个通道里面的每个元素值的大小(字节)
        System.out.println("mat.elemsize1()=" + mat.elemSize1());

        // size(int i) 获取 Mat 尺寸大小：x/y轴意思
        System.out.println("mat.size(1)=" + mat.size(1));
        System.out.println("mat.size(-1)=" + mat.size(-1));

        System.out.println("mat.rows() = " + mat.rows()); // 返回矩阵的行数（高度）
        System.out.println("mat.cols() = " + mat.cols()); // 返回矩阵的列数（宽度）

        System.out.println("mat.height() = " + mat.height()); // 获取矩阵的高度（行数）
        System.out.println("mat.width() = " + mat.width()); // 获取矩阵的宽度（列数）

        System.out.println("mat.total() = " + mat.total()); // 返回矩阵的总元素数量

        System.out.println("mat.type() = " + mat.type()); // 获取图像的类型值。它表示图像的通道数和数据类型
    }
}
