package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author: zhengja
 * @Date: 2024-06-06 15:35
 */
public class MatReadWriteExample {

    @Test
    public void test_Read() {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 图像文件路径
        String inputImagePath = "input/image.jpg";

        // 读取图像
        Mat image = Imgcodecs.imread(inputImagePath);

        if (image.empty()) {
            System.out.println("Error loading image: " + inputImagePath);
            return;
        }

        // 显示图像
        HighGui.imshow("Original Image", image);
        HighGui.waitKey(0); // 等待按键后关闭窗口
    }

    @Test
    public void test_Write() {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 输入和输出图像路径
        String inputImagePath = "input/image.jpg";
        String outputImagePath = "target/image_gray.jpg";

        // 读取图像
        Mat image = Imgcodecs.imread(inputImagePath);

        if (image.empty()) {
            System.out.println("Error loading image: " + inputImagePath);
            return;
        }

        // 转换为灰度图像
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // 显示灰度图像
        HighGui.imshow("Grayscale Image", grayImage);
        HighGui.waitKey(0); // 等待按键后关闭窗口

        // 写入灰度图像到文件
        boolean success = Imgcodecs.imwrite(outputImagePath, grayImage);

        if (success) {
            System.out.println("Image saved successfully: " + outputImagePath);
        } else {
            System.out.println("Failed to save image: " + outputImagePath);
        }
    }
}
