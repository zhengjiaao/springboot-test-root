package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * java openCV4.x 入门-Imgproc之点集拟合
 *
 * @Author: zhengja
 * @Date: 2024-06-11 15:53
 */
public class MatExample19ImgprocByFitLine {

    /**
     * 拟合直线-拟合给定点集的直线
     */
    @Test
    public void Imgproc_fitLine_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // fitLine(Mat points, Mat line, int distType, double param, double reps, double aeps) // 输入的2D或3D点向量、输出的直线向量、距离类型(距离度量方式，用于M估计。参见DIST_*)、参数(可选参数，对于某些距离类型，它表示数值参数C。如果为0，则选择最优值)、足够精度下的半径（坐标原点到直线的距离）、最小精度(对于角度的足够精度。对于reps和aeps，0.01是一个不错的默认值
        // 创建矩阵
        Mat mat = new Mat(300, 300, CvType.CV_8UC3);
        mat.setTo(new Scalar(255, 255, 255));

        // 创建点集
        MatOfPoint points = new MatOfPoint();
        points.fromArray(new Point(30, 30), new Point(55, 30), new Point(110, 88), new Point(150, 200), new Point(100, 250));

        Mat line = new Mat();
        // 拟合直线
        Imgproc.fitLine(points, line, Imgproc.DIST_L1, 0, 0.01, 0.01);

        // 获取拟合结果
        double vx = line.get(0, 0)[0];
        double vy = line.get(1, 0)[0];
        double x = line.get(2, 0)[0];
        double y = line.get(3, 0)[0];

        System.out.println("line.dump() = \n" + line.dump());
        // 绘制点集
        for (Point point : points.toArray()) {
            Imgproc.circle(mat, point, 2, new Scalar(0, 0, 255), -1, Imgproc.LINE_AA);
        }

        // 根据拟合结果绘制直线
        Point p1 = new Point(x, y);
        Point p2 = new Point(x + 200 * vx, y + 200 * vy);
        Imgproc.line(mat, p1, p2, new Scalar(255, 0, 0), 2);

        HighGui.imshow("mat", mat);
        HighGui.waitKey();

    }

    /**
     * 拟合椭圆-最佳拟合 : 该函数的作用是在给定的2D点集上找到一个最佳拟合的旋转椭圆，并返回表示该椭圆的 RotatedRect 对象
     */
    @Test
    public void Imgproc_fitEllipse_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // fitEllipse(MatOfPoint2f points) // 一个包含2D点的 MatOfPoint2f 对象

        // 创建一个二维点集
        MatOfPoint2f points = new MatOfPoint2f(new Point(100, 50), new Point(150, 100), new Point(200, 50), new Point(150, 0), new Point(100, 50));

        // 调用fitEllipse方法拟合椭圆
        RotatedRect rotatedRect = Imgproc.fitEllipse(points);

        // 打印拟合的椭圆信息
        System.out.println("Center: " + rotatedRect.center);
        System.out.println("Size: " + rotatedRect.size);
        System.out.println("Angle: " + rotatedRect.angle);

    }

    /**
     * 拟合椭圆-最佳拟合 : 该函数的作用是在给定的2D点集上找到一个最佳拟合的旋转椭圆，并返回表示该椭圆的 RotatedRect 对象
     */
    @Test
    public void Imgproc_fitEllipseDirect_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 使用了由 Fitzgibbon1999 提出的直接最小二乘法（Direct）算法

        // 假设有一组点集合，这里只是举例，实际应用中需要替换为真实数据
        Point[] pointsArray = {new Point(100, 50), new Point(150, 100), new Point(200, 50), new Point(150, 0), new Point(100, 50)};
        MatOfPoint points = new MatOfPoint(pointsArray);

        // 调用 fitEllipseAMS 方法拟合椭圆
        RotatedRect rotatedRect = Imgproc.fitEllipseDirect(points);

        // 输出椭圆的信息
        System.out.println("Center: " + rotatedRect.center);
        System.out.println("Size: " + rotatedRect.size);
        System.out.println("Angle: " + rotatedRect.angle);

        // 绘制
        Mat mat = new Mat(300, 300, CvType.CV_8UC3, new Scalar(255, 255, 255));
        List<MatOfPoint> pts = new ArrayList<>();
        pts.add(points);
        Imgproc.polylines(mat, pts, true, new Scalar(255, 0, 0));
        Imgproc.ellipse(mat, rotatedRect, new Scalar(0, 255, 0));

        // 显示
        HighGui.imshow("mat", mat);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

    /**
     * 拟合椭圆-最小面积拟合
     */
    @Test
    public void Imgproc_fitEllipseAMS_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // fitEllipseAMS根据输入的点集拟合出一个最小面积的椭圆，采用 AMS (Algebraic Method) 算法. 该函数支持更多类型的points
        // fitEllipse(MatOfPoint2f points) // 输入的点坐标集合

        // 假设有一组点集合，这里只是举例，实际应用中需要替换为真实数据
        Point[] pointsArray = {
                new Point(100, 50),
                new Point(150, 100),
                new Point(200, 50),
                new Point(150, 0),
                new Point(100, 50)
        };
        MatOfPoint points = new MatOfPoint(pointsArray);

        // 调用 fitEllipseAMS 方法拟合椭圆
        RotatedRect rotatedRect = Imgproc.fitEllipseAMS(points);

        // 输出椭圆的信息
        System.out.println("Center: " + rotatedRect.center);
        System.out.println("Size: " + rotatedRect.size);
        System.out.println("Angle: " + rotatedRect.angle);

        // 绘制
        Mat mat = new Mat(300, 300, CvType.CV_8UC3, new Scalar(255, 255, 255));
        List<MatOfPoint> pts = new ArrayList<>();
        pts.add(points);
        Imgproc.polylines(mat, pts, true, new Scalar(255, 0, 0));
        Imgproc.ellipse(mat, rotatedRect, new Scalar(0, 255, 0));

        // 显示
        HighGui.imshow("mat", mat);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

}
