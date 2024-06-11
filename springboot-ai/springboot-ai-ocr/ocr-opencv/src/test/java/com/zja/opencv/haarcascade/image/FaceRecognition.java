package com.zja.opencv.haarcascade.image;

import nu.pattern.OpenCV;
import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * 人脸识别: 图像
 *
 * @Author: zhengja
 * @Date: 2024-05-20 17:34
 */
public class FaceRecognition {

    @Test
    public void detectFace_v1() throws Exception {
        // 加载 OpenCV 库
        OpenCV.loadShared();

        // 读取图像
        String imagePath = "D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\Brush1.png";  // 要检测的图片路径
        Mat image = Imgcodecs.imread(imagePath);

        // 加载人脸分类器
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml";
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml";
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml";
        String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(haarFilePath);

        // 人脸检测
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        // 绘制人脸框
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // 输出图像
        String outFile = "D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\1-detectFace.png";
        // 存储
        Imgcodecs.imwrite(outFile, image);

        // 显示结果
        HighGui.imshow("Detected Face", image);
        HighGui.waitKey();

        // 释放资源
        image.release();
    }

}
