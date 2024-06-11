package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Random;

/**
 * @Author: zhengja
 * @Date: 2024-06-07 10:24
 */
public class MatExample1CreateAndCopy {

    // 创建 Mat 矩阵
    @Test
    public void test_Mat() {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 创建空矩阵
        Mat mat = new Mat();
        System.out.println("mat.dump()=\n" + mat.dump());

        // 创建3x2的单通道8位无符号数数类型矩阵
        Mat mat1 = new Mat(3, 2, CvType.CV_8UC1);
        System.out.println("mat1.dump()=\n" + mat1.dump());

        // 创建3x3的单通道8位无符号整数类型矩阵
        Mat mat2 = new Mat(new Size(4, 4), CvType.CV_8UC1); // CvType.CV_8U1 == CvType.CV_8UC(1)
        System.out.println("mat2.dump()=n" + mat2.dump());

        // 创建3X3的单通道的8位无符号整效类型矩阵
        int[] size = {2, 3}; // rows, cols
        // CvType.makeType(CvType.CV_8U,1)== CvType.CV_8U1 == CvType.CV_8UC(1)
        Mat mat3 = new Mat(size, CvType.CV_8UC1);
        System.out.println("mat3.dump()=\n" + mat3.dump());

        // 创建带颜色的矩阵 BGR
        Mat mat4 = new Mat(3, 3, CvType.CV_8UC3, new Scalar(100, 200, 255));
        System.out.println("mat4.dump()=\n" + mat4.dump());

        // 显示矩阵
        Mat mat5 = new Mat(new Size(200, 200), CvType.CV_8UC3, new Scalar(255, 0, 0));
        System.out.println("mat5.dump()=\n" + mat5.dump());

        HighGui.imshow("mat5", mat5);
        HighGui.waitKey();
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

    // 在屏幕上看到一个随机生成的灰度图像
    @Test
    public void test_99() {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 定义图像尺寸
        int width = 512;
        int height = 512;

        // 创建一个新的Mat对象，类型为8位单通道（灰度图像）
        Mat image = new Mat(height, width, CvType.CV_8UC1);

        // 生成随机数
        Random random = new Random();

        // 填充Mat对象
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 生成0-255之间的随机灰度值
                int grayValue = random.nextInt(256);
                image.put(y, x, new float[]{grayValue});
            }
        }

        // 显示图像
        HighGui.imshow("Random Gray Image", image);
        HighGui.waitKey(0);

        // 保存图像
        String outputPath = "target/output/image_random_gray.png";
        Imgcodecs.imwrite(outputPath, image);

        // 释放资源
        image.release();

        System.out.println("Process completed. Check the output image.");
    }

    /**
     * Mat
     * <p>
     * 加载图像：从指定路径读取一张图片。
     * 图像缩放：将读取的图像缩小一半并显示。
     * 转换为灰度图像：将缩放后的图像转换为灰度图像。
     * 阈值处理：对灰度图像应用二值化处理，设置阈值为127。
     * 显示图像：分别显示原始图像、缩放后的图像和阈值处理后的图像。
     * 保存图像：将阈值处理后的图像保存到指定路径。
     * 资源释放：使用release()方法释放不再需要的Mat对象占用的内存。
     */
    @Test
    public void test_100() {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 读取图像
        String imagePath = "D:\\temp\\opencv\\Images\\image.jpg";
        Mat srcImage = Imgcodecs.imread(imagePath);

        if (srcImage.empty()) {
            System.out.println("Error loading image");
            return;
        }

        // 图像缩放
        Mat resizedImage = new Mat();
        Size size = new Size(srcImage.width() / 2, srcImage.height() / 2);
        Imgproc.resize(srcImage, resizedImage, size);

        // 显示原始图像和缩放后的图像
        HighGui.imshow("Original Image", srcImage);
        HighGui.imshow("Resized Image", resizedImage);
        HighGui.waitKey(0);

        // 图像阈值处理
        Mat grayImage = new Mat();
        Imgproc.cvtColor(resizedImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat thresholdedImage = new Mat();
        Imgproc.threshold(grayImage, thresholdedImage, 127, 255, Imgproc.THRESH_BINARY);

        // 显示阈值处理后的图像
        HighGui.imshow("Thresholded Image", thresholdedImage);
        HighGui.waitKey(0);

        // 保存处理后的图像
        String outputImagePath = "target/image_thresholded.jpg";
        Imgcodecs.imwrite(outputImagePath, thresholdedImage);

        // 释放资源
        srcImage.release();
        resizedImage.release();
        grayImage.release();
        thresholdedImage.release();

        System.out.println("Process completed. Check the output image.");
    }
}
