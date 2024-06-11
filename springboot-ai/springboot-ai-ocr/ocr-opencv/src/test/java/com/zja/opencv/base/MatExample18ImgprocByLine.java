package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * java openCV4.x 入门-Imgproc之图形绘制（重要的：绘制线、矩形、圆、椭圆、多边形、文字）
 * <p>
 * 学习地址：https://blog.csdn.net/qq_27185879/article/details/137786814?spm=1001.2014.3001.5502
 *
 * @Author: zhengja
 * @Date: 2024-06-11 14:58
 */
public class MatExample18ImgprocByLine {

    /**
     * 绘制线：带箭直线绘制: 在给定的图像上，从第一个点到第二个点绘制一个箭头线段
     */
    @Test
    public void Imgproc_arrowedLine_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        // 创建一个图像
        Mat mat = new Mat(400, 400, CvType.CV_8UC3);
        // 设置颜色
        mat.setTo(new Scalar(0, 0, 0));
        // 定义线段的起始点和终点坐标
        Point pt1 = new Point(50, 50);
        Point pt2 = new Point(350, 350);

        // 定义线段的颜色
        Scalar color = new Scalar(0, 0, 255);

        int lineType = Imgproc.LINE_AA;
        Imgproc.arrowedLine(mat, pt1, pt2, color, 10, lineType, 0, 0.1); // 要绘制线段的图像、线段的起始点坐标、线段的终点坐标、线段的颜色、线段的粗细、箭头端点类型(LINE_*)、坐标点的小数位数（shift）、箭头端点大小（箭头尖端的长度与箭头长度之间的关系）
        HighGui.imshow("mat", mat);
        HighGui.waitKey();
    }


    /**
     * 绘制线：普通直线绘制
     */
    @Test
    public void Imgproc_line_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 普通直线绘制
        // line(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift) // 要绘制线段的图像、线段的起始点坐标、线段的终点坐标、线段的颜色、线段的粗细、线段的类型（LINE_*）、坐标点的小数位数（shift）
        // 创建矩阵
        Mat img = new Mat(200, 300, CvType.CV_8UC3, new Scalar(0, 0, 0));
        // 定义线段开始结束坐标pt1
        Point pt1 = new Point(20, 20);
        Point pt2 = new Point(100, 100);
        Point pt3 = new Point(150, 100);
        // 定义绘制的颜色
        Scalar scalar = new Scalar(0, 255, 0);
        // 定义线条粗细
        int thickness = 2;
        // 定义线条类型
        int lineType = Imgproc.LINE_AA;
        // 定义点坐标小数位数
        int shift = 0;
        Imgproc.line(img, pt1, pt2, scalar, thickness, lineType, shift);
        Imgproc.line(img, pt2, pt3, scalar, thickness, lineType, shift);

        // 显示
        HighGui.imshow("img", img);
        HighGui.waitKey();

    }

    /**
     * 绘制线：绘制多段线
     */
    @Test
    public void Imgproc_polylines_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // polylines(Mat img, java.util.List pts, boolean isClosed, Scalar color, int thickness, int lineType, int shift) // 要绘制线段的图像、线段的起始点坐标、线段的终点坐标、线段的颜色、线段的粗细、线段的类型（LINE_*）、坐标点的小数位数（shift）

        Mat mat = new Mat(500, 500, CvType.CV_8UC3);
        mat.setTo(new Scalar(255, 255, 255));

        List<MatOfPoint> pts = new ArrayList<>();
        MatOfPoint point = new MatOfPoint();
        point.fromArray(new Point(50, 50), new Point(150, 90), new Point(80, 180), new Point(250, 250));
        pts.add(point);
        Imgproc.polylines(mat, pts, false, new Scalar(0, 255, 0), 2, Imgproc.LINE_AA, 0);
        HighGui.imshow("mat", mat);
        HighGui.waitKey();

    }

    /**
     * 矩形绘制：
     */
    @Test
    public void Imgproc_rectangle_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift) // 要绘制线段的图像、矩形的一个角的坐标、与 pt1 相对的矩形角的坐标、矩形的颜色、构成矩形的线条的厚度，-1则表示填充矩形、线段的类型（LINE_*）、坐标点的小数位数（shift）

        Mat mat = new Mat(300, 300, CvType.CV_8UC3);
        mat.setTo(new Scalar(255, 255, 255));

        Imgproc.rectangle(mat, new Point(50, 50), new Point(250, 250), new Scalar(0, 250, 0), 4, Imgproc.LINE_AA, 0);

        HighGui.imshow("mat", mat);
        HighGui.waitKey();
    }

    /**
     * 圆绘制
     */
    @Test
    public void Imgproc_circle_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // circle(Mat img, Point center, int radius, Scalar color, int thickness, int lineType, int shift) // 要绘制线段的图像、圆心的坐标（x, y）、圆的半径、圆的颜色、圆的线条厚度（如果为正数，表示绘制圆的外轮廓；如果为负数，表示绘制填充圆)、圆的边界类型(LINE_*)、圆心和半径值的坐标的小数位数（shift）

        // 创建矩阵
        Mat img = new Mat(300, 420, CvType.CV_8UC3, new Scalar(255, 255, 255));

        // 定义坐标
        Point point = new Point(100, 100);
        // 定义半径
        int radius = 50;
        // 定义颜色
        Scalar scalar = new Scalar(255, 0, 0);
        // 绘制圆
        Imgproc.circle(img, point, radius, scalar, 4, Imgproc.LINE_AA, 0);// 蓝色圆

        //====在上方的基础上绘制一个五环===
        // 定义坐标
        Point point1 = new Point(210, 100);
        Point point2 = new Point(320, 100);
        Point point3 = new Point(150, 160);
        Point point4 = new Point(260, 160);
        // 定义颜色
        Scalar scalar1 = new Scalar(0, 0, 0);
        Scalar scalar2 = new Scalar(0, 0, 255);
        Scalar scalar3 = new Scalar(0, 215, 255);
        Scalar scalar4 = new Scalar(0, 215, 0);
        // 绘制圆

        Imgproc.circle(img, point1, radius, scalar1, 4, Imgproc.LINE_AA, 0);// 黑色圆
        Imgproc.circle(img, point2, radius, scalar2, 4, Imgproc.LINE_AA, 0);// 红色圆
        Imgproc.circle(img, point3, radius, scalar3, 4, Imgproc.LINE_AA, 0);// 黄色圆
        Imgproc.circle(img, point4, radius, scalar4, -1, Imgproc.LINE_AA, 0);// 绿色圆

        // 显示
        HighGui.imshow("img", img);
        HighGui.waitKey();
    }

    /**
     * 椭圆（椭圆弧）绘制
     */
    @Test
    public void Imgproc_ellipse_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness, int lineType, int shift) // 要绘制椭圆的图像、椭圆中心坐标、椭圆的旋转角度（以度为单位）、椭圆弧的起始角度（以度为单位）、椭圆弧的结束角度（以度为单位）、椭圆的颜色、椭圆的线条厚度（如果为正数，表示绘制圆的外轮廓；如果为负数，表示绘制填充圆）、椭圆的边界类型(LINE_*)、椭圆中心坐标的小数位数（shift）

        // 创建空白图像
        Mat mat = new Mat(500, 500, CvType.CV_8UC3, new Scalar(255, 255, 255));


        // 椭圆中心点
        Point point = new Point(250, 250);
        // 轴长
        Size axes = new Size(200, 150);
        // 旋转角度
        double angle = 0;
        // 起始角度
        double startAngle = 0;
        // 结束角度
        double endAngle = 360;
        Imgproc.ellipse(mat, point, axes, angle, startAngle, endAngle, new Scalar(0, 200, 0), 2, Imgproc.LINE_8, 0);

        // 绘制椭圆
        double vals[] = {200, 200, 100, 100, 0};
        RotatedRect box = new RotatedRect(vals);
        Imgproc.ellipse(mat, box, new Scalar(0, 0, 255), 2, Imgproc.LINE_8);
        Imgcodecs.imwrite("target/ellipse.jpg", mat);

        // 显示
        HighGui.imshow("ellipse.jpg", mat);
        HighGui.waitKey();
    }

    /**
     * 多边形填充-单个填充
     * <p>
     * fillConvexPoly 用于绘制填充的凸多边形它不仅可以填充凸多边形，还可以填充任何单调的非自相交多边形，即轮廓最多与每条水平线（扫描线）相交两次的多边形（尽管其顶部和/或底部边缘可能是水平的）
     */
    @Test
    public void Imgproc_fillConvexPoly_test1() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // fillConvexPoly(Mat img, MatOfPoint points, Scalar color, int lineType, int shift) // 要绘制多边形的图像、含多边形顶点坐标的 MatOfPoint 对象、多边形的颜色、填充多边形的线条类型（LINE_*）、坐标点的小数位数（shift）

        Mat mat = new Mat(500, 500, CvType.CV_8UC3, new Scalar(255, 255, 255));

        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromArray(new Point(100, 100), new Point(400, 100), new Point(400, 400), new Point(100, 400));

        // 填充多边形
        Imgproc.fillConvexPoly(mat, matOfPoint, new Scalar(0, 255, 0), Imgproc.LINE_8, 0);

        HighGui.imshow("fill", mat);
        HighGui.waitKey();
        mat.release();
        HighGui.destroyAllWindows();

    }

    /**
     * 多边形填充-多个填充
     * <p>
     * fillPoly 用于填充由多个多边形轮廓所包围的区域。该函数可以填充复杂的区域，例如包含空洞的区域、具有自交点（即某些部分相互交叉）的轮廓等等.
     */
    @Test
    public void Imgproc_fillConvexPoly_test2() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // fillPoly(Mat img, java.util.List pts, Scalar color, int lineType, int shift, Point offset) // 要绘制多边形的图像、包含多边形顶点坐标的 java.util.List 对象、填充多边形时使用的颜色、边界类型（LINE_*）、填充多边形时的偏移量，是一个 Point 对象

        Mat mat = new Mat(500, 500, CvType.CV_8UC3, new Scalar(255, 255, 255));
        MatOfPoint matOfPoint = new MatOfPoint();
        MatOfPoint matOfPoint1 = new MatOfPoint();
        matOfPoint.fromArray(new Point(100, 100), new Point(400, 100), new Point(400, 400), new Point(100, 400));
        matOfPoint1.fromArray(new Point(50, 50), new Point(200, 50), new Point(200, 200), new Point(50, 200));
        List<MatOfPoint> pts = new ArrayList<>();
        pts.add(matOfPoint);
        pts.add(matOfPoint1);
        // 填充多边形
        Imgproc.fillPoly(mat, pts, new Scalar(0, 255, 0), Imgproc.LINE_8, 0);

        HighGui.imshow("fill", mat);
        HighGui.waitKey();
        mat.release();
        HighGui.destroyAllWindows();
    }

    /**
     * 标记绘制
     */
    @Test
    public void Imgproc_drawMarker_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 在图像中预定义的位置绘制标记
        // drawMarker(Mat img, Point position, Scalar color, int markerType, int markerSize, int thickness, int line_type) // 要绘制标记的图像、标记的位置、标记的颜色、标记的类型（MARKER_*）、标记轴的长度(标记的大小) [默认 = 20 像素]、标记的线条厚度[线条粗细]、标记的边界类型（线条类型 LINE_*）

        // 创建一个空白图像
        Mat img = Mat.zeros(500, 500, 16); // 16表示CV_8UC3，即3通道的8位无符号整数图像

        // 定义标记的位置、颜色和其他参数
        Point position = new Point(250, 250); // 在图像中心
        Scalar color = new Scalar(0, 255, 0); // 绿色
        int markerType = Imgproc.MARKER_CROSS;
        int markerSize = 20;
        int thickness = 2;
        int lineType = Imgproc.LINE_8;

        // 在图像上绘制标记
        Imgproc.drawMarker(img, position, color, markerType, markerSize, thickness, lineType);

        // 显示图像
        HighGui.imshow("DrawMarkerExample", img);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

    /**
     * 文本绘制
     */
    @Test
    public void Imgproc_putText_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 绘制一个文本字符串
        // putText(Mat img, java.lang.String text, Point org, int fontFace, double fontScale, Scalar color, int thickness, int lineType, boolean bottomLeftOrigin) // 要绘制的图像、要绘制文本的字符串、指定文本字符串的起始位置（左下角或右下角，取决于bottomLeftOrigin参数）、字体类型（FONT_*）、指定字体大小(它表示字体相对于原始大小的缩放因子)、字体颜色、字体粗细、文本线条线条粗细（LINE_*）、线条类型、是否从左下角开始绘制文本（true，则文本从左下角开始绘制，false，则从图像的右下角开始绘制）
        // 创建一个空白图像
        Mat img = Mat.zeros(200, 400, CvType.CV_8UC3);

        // 绘制文本
        Imgproc.putText(img, "hello OpenCV", new Point(100, 100), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);

        // 显示图像
        HighGui.imshow("img", img);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();

    }

    /**
     * 相关扩展方法-线段裁定
     */
    @Test
    public void Imgproc_clipLine_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // clipLine用于检查线段是否完全位于给定的图像边界内，并进行裁剪。如果线段与图像边界相交或超出边界，它将被裁剪到边界内。函数返回一个布尔值，指示线段是否被裁剪。
        // clipLine(Rect imgRect, Point pt1, Point pt2) // 要裁剪的图像的矩形区域、线段的两个端点

        // 创建一个图像矩形范围
        Rect imgRect = new Rect(0, 0, 100, 100);
        // 创建线段的起点和终点
        Point pt1 = new Point(50, 50);
        Point pt2 = new Point(60, 150);

        // 剪裁线段，使其位于图像矩形范围内
        boolean clipped = Imgproc.clipLine(imgRect, pt1, pt2);

        if (clipped) {
            System.out.println("线段被剪裁后的起点：" + pt1);
            System.out.println("线段被剪裁后的终点：" + pt2);
        } else {
            System.out.println("线段完全位于图像矩形范围之外");
        }

    }


    /**
     * 相关扩展方法-椭圆弧转换为多段线
     */
    @Test
    public void Imgproc_ellipse2Poly_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // ellipse2Poly用于将椭圆弧转换为多段线 如果arcStart的值大于arcEnd，那么这两个值会被交换。
        // ellipse2Poly(Point center, Size axes, int angle, int arcStart, int arcEnd, int delta, MatOfPoint pts) // 椭圆的中心点坐标 (Point 类型)、椭圆的半长轴和半短轴的长度 (Size 类型)、椭圆的旋转角度、椭圆弧的起始角度、椭圆弧的结束角度、弧段的步长(角度增量，用于计算椭圆弧上的点。增加这个值会增加多段线的精度)、输出参数(包含生成的椭圆边界点的 MatOfPoint 对象）

        // 创建空白图像
        Mat mat = new Mat(500, 500, CvType.CV_8UC3, new Scalar(255, 255, 255));

        // 椭圆中心点
        Point point = new Point(250, 250);
        // 轴长
        Size axes = new Size(200, 150);
        // 旋转角度
        int angle = 0;
        // 起始角度
        int arcStart = 0;
        // 结束角度
        int arcEnd = 360;

        // 转换成线段
        MatOfPoint matOfPoint = new MatOfPoint();
        Imgproc.ellipse2Poly(point, axes, angle, arcStart, arcEnd, 10, matOfPoint);

        System.out.println("matOfPoint.dump() = " + matOfPoint.dump());

        // 绘制线段
        List<MatOfPoint> pts = new ArrayList<>();
        pts.add(matOfPoint);
        Imgproc.polylines(mat, pts, true, new Scalar(255, 0, 0), 2);

        // 输出图像
        Imgcodecs.imwrite("target/ellipse_2_poly.jpg", mat);

        // 显示
        HighGui.imshow("ellipse_2_poly.jpg", mat);
        HighGui.waitKey();
    }
    
}
