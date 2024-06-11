package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;

/**
 * java openCV4.x 入门-Mat之创建、复制
 *
 * @Author: zhengja
 * @Date: 2024-06-07 10:24
 */
public class MatExample3SizeAndPoint {

    /**
     * Size对象:表示图像的宽度和高度
     * Size对象作用：调整图像大小、裁剪图像、计算图像的比例等。
     */
    @Test
    public void test_Mat_Size() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 创建一个5x5大小的Size对象。相当于创建一个从（0,0）坐标开始到（5,5）坐标结束的一个图像尺寸
        Size size = new Size(new Point(5, 5)); // Point是一个点对象，通过接受x，y坐标定义一个点

        System.out.println("size = " + size);
        System.out.println("size.width = " + size.width);
        System.out.println("size.height = " + size.height);
        System.out.println("size.area() = " + size.area()); // 返回Size对象的面积，即宽度乘以高度的结果
        System.out.println("size.empty() = " + size.empty()); // 验证Size是否为空。如果Size对象的宽度和高度都为0，则返回true；否则返回false。

        Size size2 = new Size(5, 5);
        System.out.println("size.equals(size2) = " + size.equals(size2));

        double[] vals = {5, 10}; // 第一个元素表示宽度，第二个元素表示高度
        size2.set(vals);
        System.out.println("size2.width = " + size2.width);
        System.out.println("size2.height = " + size2.height);
        System.out.println("size.equals(size2) = " + size.equals(size2));
    }

    /**
     * Point对象: Point类是一个表示二维坐标点的类。它包含了两个成员变量x和y，分别表示点的横坐标和纵坐标。
     * <p>
     * 表示：二维空间中的两个点A(x1, y1)和B(x2, y2)
     */
    @Test
    public void test_Mat_Point() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // dot(Point p) 点积 公式：dot = x1 * x2 + y1 * y2
        Point point1 = new Point(1, 2);
        Point point2 = new Point(2, 4);
        double dot = point1.dot(point2);
        System.out.println("dot = " + dot);

        // inside(Rect r)
        // 创建一个矩形区域
        Rect rect = new Rect(0, 0, 100, 100);
        // 创建一个点
        Point point3 = new Point(50, 50);
        Point point4 = new Point(150, 100);
        // 判断点是否在矩形区域内
        boolean inside1 = point3.inside(rect);
        boolean inside2 = point4.inside(rect);
        // 输出结果
        System.out.println("point3.inside(rect) = " + point3.inside(rect));
        System.out.println("point4.inside(rect) = " + point4.inside(rect));
    }

    /**
     * Mat之创建操作
     */
    @Test
    public void test_Mat_create() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // create(Size size, int type)
        Mat mat = new Mat();
        mat.create(new Size(3, 3), CvType.CV_8UC(3)); // Size大小(宽、高)、CvType类型
        System.out.println("mat = \n" + mat.dump());

        // ones 创建一个所有元素都为1的矩阵
        Mat ones = Mat.ones(3, 3, CvType.CV_8UC1); // 行、列、类型
        System.out.println("ones = \n" + ones.dump());

        // zeros 创建一个所有元素都为0的矩阵
        Mat zeros = Mat.zeros(3, 3, CvType.CV_8UC1);
        System.out.println("zeros = \n" + zeros.dump());

        // eye 创建一个对角线上值为1，其它位置为0的矩阵
        Mat eye = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("eye = \n" + eye.dump());

        // clone 复制当前矩阵，并返回一个新的矩阵。
        Mat clone = zeros.clone();
        System.out.println("zeros = " + zeros);
        System.out.println("clone = " + clone);
        System.out.println("zeros.dump() = \n" + zeros.dump());
        System.out.println("clone.dump() = \n " + clone.dump());
        // 根据结果看出，两个矩阵唯一的区别就是内存地址不一样
    }

    /**
     * Mat之复制转换操作
     */
    @Test
    public void test_Mat_copy() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat);

        // assignTo 将当前Mat对象的数据复制到指定的Mat对象中。也可以理解为覆盖替换操作
        Mat assignToMat = new Mat(4, 4, CvType.CV_8UC3);
        System.out.println("assignToMat = " + assignToMat);
        mat.assignTo(assignToMat);
        System.out.println("assignToMat = " + assignToMat);
        // 结果：改变目标矩阵的数据地址（dataAddr-当前矩阵地址赋值给目标矩阵地址）以及矩阵的大小和数据类型

        // copySize 将目标矩阵的大小复制给当前的Mat对象
        Mat copySizeMat = new Mat(4, 4, CvType.CV_8UC3);
        System.out.println("copySizeMat = " + copySizeMat);
        mat.copySize(copySizeMat);
        System.out.println("copySizeMat = " + copySizeMat);

        // copyTo 将当前的Mat对象复制到目标Mat对象中，与assignTo操作基本一样，唯一不同的是，此操作数据地址是一个全新的地址
        Mat copyToMat = new Mat(4, 4, CvType.CV_8UC2);
        System.out.println("copyToMat = " + copyToMat);
        mat.copyTo(copyToMat);
        System.out.println("copyToMat = " + copyToMat);

        // push_back(Mat m) 将另一个Mat对象添加到当前Mat对象的末尾，用于以垂直方形拼接图像的 （不属于复制的范畴）
        // 注：此方法只能操作相同列数的矩阵
        // 创建两个Mat对象
        Mat mat1 = Mat.eye(3, 3, CvType.CV_8UC1);
        Mat mat2 = Mat.ones(2, 3, CvType.CV_8UC1);
        System.out.println("mat1:");
        System.out.println(mat1.dump());
        System.out.println("mat2:");
        System.out.println(mat2.dump());
        // 将mat2添加到mat1的末尾
        mat1.push_back(mat2);
        System.out.println("push_back:");
        System.out.println(mat1.dump());
    }
}
