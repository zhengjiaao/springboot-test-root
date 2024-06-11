package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;

/**
 * java openCV4.x 入门-特殊的Mat类汇总（二）
 * <p>
 * 本节中列举的所有类的作用用于表示图像的轮廓点或矩形相关的数据信息
 * 学习地址：https://blog.csdn.net/qq_27185879/article/details/137454538?spm=1001.2014.3001.5501
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-06-07 14:01
 */
public class MatExample9MatClass2 {

    /**
     * MatOfDMatch用于存储特征点的匹配信息。包括匹配的特征点在两个图像中的索引和它们之间的距离。
     * 它是一个二维矩阵，每一行表示一个匹配对，每个匹配对由两个特征点的索引和它们之间的距离组成。
     */
    @Test
    public void test_Mat_MatOfDMatch() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 构造函数
        DMatch dMatch1 = new DMatch(0, 0, 0, 2f); // DMatch 类是用于表示两幅图像之间的匹配关系的类。
        DMatch dMatch2 = new DMatch(2, 2, 2, 4f);
        // 使用构造函数创建MatOfDMatch对象，并传入两个DMatch对象
        MatOfDMatch match = new MatOfDMatch(dMatch1, dMatch2);
        // 打印对象内容
        System.out.println("match.dump() :\n " + match.dump());


        // 这个方法通常用于排序或查找操作，例如在匹配结果中进行筛选或查找最佳匹配
        // 创建两个 DMatch 对象
        DMatch match1 = new DMatch(0, 0, 0, 2.0f);
        DMatch match2 = new DMatch(0, 0, 0, 1.0f);
        // 比较两个 DMatch 对象
        if (match1.lessThan(match2)) {
            System.out.println("match1 小于 match2");
        } else {
            System.out.println("match1 不小于 match2");
        }

    }

    /**
     * MatOfKeyPoint用于存储关键点的信息。
     * <p>
     * MatOfKeyPoint类的作用包括:
     * 1.存储关键点的位置和尺度信息：每个关键点由其在图像中的坐标和尺度大小表示。
     * 2.存储关键点的特征描述符：关键点通常与其周围的图像区域相关联，可以通过计算该区域的特征描述符来表示关键点的特征。
     * 3.支持关键点的检测和提取：可以使用MatOfKeyPoint类中的方法来检测图像中的关键点，并提取关键点的特征描述符。
     * 4.支持关键点的匹配和跟踪：可以使用MatOfKeyPoint类中的方法来进行关键点的匹配和跟踪，用于目标检测、图像配准等任务。
     * </p>
     */
    @Test
    public void test_Mat_MatOfKeyPoint() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 构造函数
        // 将给定的Mat对象转换为MatOfKeyPoint对象
        Mat mat = new Mat(2, 1, CvType.CV_32FC(7));
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 14);
        MatOfKeyPoint keyPoint = new MatOfKeyPoint(mat);  // mat数据类型需为 CV_32FC(7),且需要1行多列或1列多行（cols=7,rows>0,CvType.CV_32FC1也可,请自行验证）
        System.out.println("keyPoint.dump() =  \n" + keyPoint.dump());

    }

    /**
     * MatOfPoint 用于表示图像中的轮廓点的类(表示二维空间中的轮廓点)
     * <p>
     * MatOfPoint类的主要作用包括:
     * 1.存储轮廓点集：MatOfPoint类可以用来存储图像中的轮廓点集。轮廓是图像中的连续边界，由一组点表示。MatOfPoint类可以存储这些点，并提供对这些点的访问和操作方法。
     * 2.访问轮廓点：MatOfPoint类提供了一些方法来访问轮廓中的点。可以通过索引访问单个点，也可以通过迭代器遍历所有的点。 。
     * 3.转换为其他数据结构：MatOfPoint类可以将轮廓点集转换为其他数据结构，如数组或列表。这样可以更方便地使用这些点进行进一步的分析和处理。
     * 4.计算轮廓的面积和周长：MatOfPoint类提供了计算轮廓的面积和周长的方法。这些信息对于图像分析和处理非常有用。
     * </p>
     */
    @Test
    public void test_Mat_MatOfPoint() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(1, 2, CvType.CV_32SC2);
        MatOfPoint matOfPoint = new MatOfPoint(mat);
        System.out.println("matOfPoint.dump() :\n " + matOfPoint.dump());
    }

    /**
     * MatOfPoint2f 与MatOfPoint一样用于表示二维点集的类，它的坐标类型为浮点型。便于更加精确的操作点集。
     */
    @Test
    public void test_Mat_MatOfPoint2f() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }

    /**
     * MatOfPoint3 用于表示图像中的轮廓点的类(表示三维空间中的轮廓点)
     */
    @Test
    public void test_Mat_MatOfPoint3() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(4, 1, CvType.CV_32SC3);
        MatOfPoint3 matOfPoint = new MatOfPoint3(mat);
        System.out.println("matOfPoint.dump() :\n " + matOfPoint.dump());

        // 计算两个三维向量的叉乘
        Point3 p3_1 = new Point3(1, 2, 3);
        Point3 p3_2 = new Point3(2, 3, 4);
        Point3 cross = p3_1.cross(p3_2);
        System.out.println("cross.toString() = " + cross.toString());

        // 计算当前点与另一个点p的点积（dot product）
        Point3 point1 = new Point3(1, 2, 0);
        Point3 point2 = new Point3(2, 4, 0);
        double dot = point1.dot(point2);
        System.out.println("dot = " + dot);
    }

    /**
     * MatOfPoint3f 与MatOfPoint一样用于表示三维点集的类，它的坐标类型为浮点型。便于更加精确的操作点集。
     */
    @Test
    public void test_Mat_MatOfPoint3f() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }

    /**
     * MatOfRect类的主要作用是在图像或视频中检测和定位矩形对象。
     * 它通常用于目标检测、人脸识别、车牌识别等计算机视觉任务中。
     * <p>
     * MatOfRect类具有以下主要功能和方法：
     * 1.存储矩形对象： MatOfRect类可以存储一组矩形对象，每个矩形对象由四个整数表示，分别表示矩形的左上角坐标和宽度、高度。
     * 2.访问矩形对象： 可以通过索引访问MatOfRect对象中的矩形对象，获取矩形的位置和大小信息。
     * 3.操作矩形对象： 可以对MatOfRect对象中的矩形对象进行操作，如添加、删除、修改等。
     * 4.矩形对象的转换： 可以将MatOfRect对象转换为其他数据结构，如矩形数组、列表等。
     * </P>
     */
    @Test
    public void test_Mat_MatOfRect() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(4, 1, CvType.CV_32SC4);
        MatOfRect matOfRect = new MatOfRect(mat); // mat数据类型需为 CV_32SC4,且需要1行多列或1列多行（CV_32SC1,rows>0,cols=4也可）
        System.out.println("matOfRect.dump() ：\n " + matOfRect.dump());
    }

    // MatOfRect2d 与MatOfRect一样,但是使用double类型的坐标表示
    @Test
    public void test_Mat_MatOfRect2d() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }

    /**
     * MatOfRotatedRect 类是一个特殊的Mat类，用于存储旋转矩形对象的数据。
     * 旋转矩形是一个带有角度信息的矩形，通常用于表示图像中的物体或区域。
     */
    @Test
    public void test_Mat_MatOfRotatedRect() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(3, 1, CvType.CV_32FC(5));
        MatOfRotatedRect matOfRotatedRect = new MatOfRotatedRect(mat);
        System.out.println("matOfRotatedRect.dump() :\n " + matOfRotatedRect.dump());

        // RotatedRect类 用于表示旋转矩形的类，它可以具有任意旋转角度。
        double vals[] = {4, 4, 5, 5, -10};
        RotatedRect rotatedRect = new RotatedRect(vals); // vals 一个包含5个元素的double数组，依次表示旋转矩形的中心点坐标(x, y)，宽度width，高度height，以及旋转角度angle
        System.out.println("rotatedRect.toString() = " + rotatedRect.toString());

        // boundingRect 计算RotatedRect对象的最小外接矩形（即包围矩形）
        Point point = new Point(4, 4);
        Size size = new Size(2, 2);
        RotatedRect rotatedRect2 = new RotatedRect(point, size, 0);
        System.out.println("rotatedRect2.toString() = " + rotatedRect2.toString());
        Rect rect = rotatedRect2.boundingRect();
        System.out.println("rect.toString() = " + rect.toString());


        // points(Point[] pt) 获取旋转矩形的四个顶点坐标。
        // 创建一个旋转矩形对象
        RotatedRect rotatedRect3 = new RotatedRect(new Point(100, 100), new Size(20, 20), 0);

        // 创建一个Point数组用于存储顶点坐标
        Point[] points = new Point[4];

        // 获取旋转矩形的顶点坐标
        rotatedRect3.points(points);
        for (Point point2 : points) {
            System.out.println("point2.toString() = " + point2.toString());
        }
    }
}
