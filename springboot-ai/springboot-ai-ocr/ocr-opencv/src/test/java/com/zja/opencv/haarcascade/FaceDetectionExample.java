package com.zja.opencv.haarcascade;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * 人脸检测：图片、视频
 * <p>
 * 输出时间帧
 *
 * @Author: zhengja
 * @Date: 2024-05-21 13:03
 */
@Deprecated
public class FaceDetectionExample {

    public static void main(String[] args) {
        Loader.load(org.bytedeco.opencv.presets.opencv_core.class);
        Loader.load(org.bytedeco.opencv.presets.opencv_objdetect.class);
        Loader.load(org.bytedeco.opencv.presets.opencv_videoio.class);

        // 要检测的图片路径
        String imagePath = "D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\Brush1.png";
        // 要检测的视频路径
        String videoPath = "D:\\temp\\opencv\\Video\\1.mp4";
        // 输出结果文件路径
        String outputFilePath = "D:\\temp\\opencv\\output.txt";

        // 加载人脸检测器
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml";
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml";
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml";
        String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml";
        CascadeClassifier faceDetector = new CascadeClassifier();
        faceDetector.load(haarFilePath);

        // 图片人脸检测
        detectFacesInImage(imagePath, faceDetector, outputFilePath);

        // 视频人脸检测
        //detectFacesInVideo(videoPath, faceDetector, outputFilePath);
    }

    private static void detectFacesInImage(String imagePath, CascadeClassifier faceDetector, String outputFilePath) {
        // 读取图片
        Mat image = org.bytedeco.opencv.global.opencv_imgcodecs.imread(imagePath);

        // 灰度化处理
        Mat grayImage = new Mat();
        org.bytedeco.opencv.global.opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);

        // 人脸检测
        org.bytedeco.opencv.opencv_core.RectVector faceDetections = new org.bytedeco.opencv.opencv_core.RectVector();
        faceDetector.detectMultiScale(grayImage, faceDetections);

        // 输出结果到文件
        writeDetectionResultsToFile(faceDetections, outputFilePath);
    }

    private static void detectFacesInVideo(String videoPath, CascadeClassifier faceDetector, String outputFilePath) {
        // 打开视频文件
        VideoCapture videoCapture = new VideoCapture(videoPath);

        // 创建用于存储视频帧的Mat对象
        Mat frame = new Mat();

        // 打开输出文件
        try (PrintWriter writer = new PrintWriter(outputFilePath)) {
            // 逐帧读取视频并进行人脸检测
            while (videoCapture.read(frame)) {
                // 灰度化处理
                Mat grayFrame = new Mat();
                org.bytedeco.opencv.global.opencv_imgproc.cvtColor(frame, grayFrame, opencv_imgproc.COLOR_BGR2GRAY);

                // 人脸检测
                org.bytedeco.opencv.opencv_core.RectVector faceDetections = new org.bytedeco.opencv.opencv_core.RectVector();
                faceDetector.detectMultiScale(grayFrame, faceDetections);

                // 输出结果到文件
                writer.println("Frame: " + videoCapture.get(Videoio.CAP_PROP_POS_FRAMES));
                writer.println("Number of faces detected: " + faceDetections.size());
                for (int i = 0; i < faceDetections.size(); i++) {
                    Rect rect = faceDetections.get(i);
                    writer.println("Face " + (i + 1) + ": " + rect.x() + ", " + rect.y() + ", " + rect.width() + ", " + rect.height());
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeDetectionResultsToFile(org.bytedeco.opencv.opencv_core.RectVector faceDetections, String outputFilePath) {
        try (PrintWriter writer = new PrintWriter(outputFilePath)) {
            writer.println("Number of faces detected: " + faceDetections.size());
            for (int i = 0; i < faceDetections.size(); i++) {
                Rect rect = faceDetections.get(i);
                writer.println("Face " + (i + 1) + ": " + rect.x() + ", " + rect.y() + ", " + rect.width() + ", " + rect.height());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
