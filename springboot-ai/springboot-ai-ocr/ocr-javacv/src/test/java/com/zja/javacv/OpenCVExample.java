package com.zja.javacv;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @Author: zhengja
 * @Date: 2024-06-06 9:36
 */
public class OpenCVExample {

    static {
        // 错误：no opencv_java470 in java.library.path
        // 1.先把 E:\App\opencv-4.7.0\build\java\x64\opencv_java470.dll 父目录添加到系统环境变量中。
        // 2.引入依赖：opencv
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 使用OpenCV进行图像处理
     */
    @Test
    public void test() {
        String inputFilePath = "D:\\temp\\opencv\\Images\\input.jpg";
        String outputFilePath = "D:\\temp\\opencv\\Images\\output.jpg";

        // 使用imread()函数读取输入图像文件，并将其存储在Mat对象中
        Mat image = imread(inputFilePath, IMREAD_COLOR);

        if (image.empty()) {
            // 改为抛出异常而不是简单打印错误信息
            throw new RuntimeException("Failed to read image: " + inputFilePath);
        }

        // 使用cvtColor()函数将彩色图像转换为灰度图像
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // 检查转换是否成功
        if (grayImage.empty()) {
            throw new RuntimeException("Image conversion to gray scale failed.");
        }

        // 使用imwrite()函数将处理后的图像保存到输出文件
        // 这里假设输出文件夹存在，否则应添加文件夹创建逻辑
        boolean written = Imgcodecs.imwrite(outputFilePath, grayImage);
        if (!written) {
            throw new RuntimeException("Failed to write image: " + outputFilePath);
        }

        // 显示处理后的图像，可选操作，用于验证
        HighGui.imshow("Gray Image", grayImage);
        HighGui.waitKey(0);

        // 明确释放资源
        image.release();
        grayImage.release();
    }
}
