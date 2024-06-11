package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * java openCV4.x 入门- Imgproc之色彩映射及颜色空间转换
 *
 * @Author: zhengja
 * @Date: 2024-06-11 14:30
 */
public class MatExample17ImgprocByColor {

    String input_image = "input/image.jpg";


    /**
     * Imgproc.applyColorMap 色彩映射
     * <p>
     * 色彩映射原理：颜色将根据灰度图像中的像素值进行映射(灰度图像中的像素值直接用作索引来选择相应的颜色)。这种方式相当于线性映射的一种形式
     */
    @Test
    public void Imgproc_applyColorMap_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 1.系统预设的colormap
        // applyColorMap(Mat src, Mat dst, int colormap)
        // 读取图像
        Mat mat = Imgcodecs.imread(input_image);
        // 创建输出彩色图像
        Mat dst = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC3);
        // 应用调色板
        Imgproc.applyColorMap(mat, dst, Imgproc.COLORMAP_HOT); // 输入图像、输出图像、调色板(要应用的colormap，COLORMAP_*)
        HighGui.imshow("dst", dst);
        HighGui.waitKey();

        // 2.自定义colormap
        // applyColorMap(Mat src, Mat dst, Mat userColor)
        Mat src = Imgcodecs.imread(input_image);
        // 创建自定义色板
        Mat userColor = new Mat(256, 1, CvType.CV_8UC3);
        // 将每个像素值映射为对应的蓝色，不设置，则随机
        for (int i = 0; i < 256; i++) {
            userColor.put(i, 0, new double[]{(double) i, 0, 0});
           /* userColor.put(i, 0, new double[]{(double) 0, i,0});
            userColor.put(i, 0, new double[]{(double) 0, 0,i});*/
        }
        Imgproc.applyColorMap(src, dst, userColor);
        HighGui.imshow("userColor", userColor);
        HighGui.waitKey();
    }

    /**
     * 颜色空间转换: 将图像从一种颜色空间转换为另一种颜色空间.
     */
    @Test
    public void Imgproc_cvtColor_test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //cvtColor(Mat src, Mat dst, int code)
        //读取图像
        Mat mat = Imgcodecs.imread(input_image);

        Mat dst = new Mat();
        Imgproc.cvtColor(mat,dst,Imgproc.COLOR_BGR2GRAY); // 输入图像、输出图像、颜色空间转换(COLOR_*)

        HighGui.imshow("dst",dst);
        HighGui.waitKey();
    }

}
