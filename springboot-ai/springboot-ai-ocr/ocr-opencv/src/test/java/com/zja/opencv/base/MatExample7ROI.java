package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author: zhengja
 * @Date: 2024-06-07 9:16
 */
public class MatExample7ROI {

    static {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Mat_ROI adjustROI 加、减
     */
    @Test
    public void test_Mat_ROI() {
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(5, 5, CvType.CV_8UC1);
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

        // adjustROI  上下+1，左右-1
        Mat mat1 = mat.adjustROI(1, 1, -1, -1);
        System.out.println("mat1.dump()=\n" + mat1.dump());
    }

    // Mat_ROI locateROI 定位：定位从左上角开始
    @Test
    public void test_Mat_ROI_locateRoI() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat(5, 5, CvType.CV_8UC1);
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

        // locateRoI(Size wholeSize, Point ofs) 定位：定位从左上角开始
        Mat submat = mat.submat(1, 4, 1, 4);
        System.out.println("submat.dump()=\n" + submat.dump());
        Point ofs = new Point();
        submat.locateROI(mat.size(), ofs);
        System.out.println("ofs =" + ofs);
    }

    // 绘制
    // Mat_ROI 定位：定位从左上角开始
    @Test
    public void test_Mat_ROI_2() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String imageInputPath = "D:\\temp\\opencv\\Images\\bus.jpg"; // input/image.jpg
        Mat imread = Imgcodecs.imread(imageInputPath);

        Mat submat = imread.submat(100, 300, 100, 300);
        Point point = new Point();
        submat.locateROI(imread.size(), point);
        System.out.println("point =" + point);
        // 绘制
        Imgproc.rectangle(imread, new Rect((int) point.x, (int) point.y, submat.width(), submat.height()), new Scalar(255, 8, 0));

        HighGui.imshow("submat", submat); // 截取图(矩形)
        HighGui.imshow("imread", imread); // 原图(在原图上绘制的矩形)
        HighGui.waitKey(0); // 等待按键后关闭窗口
    }
}
