package com.zja.javacv.rtsp;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * 根据 rtsp 视频流实现车辆实施检测
 *
 * @Author: zhengja
 * @Date: 2024-06-05 13:56
 */
public class RtspCarDetection {

    String rtspUrl = "rtsp://your_rtsp_stream_url";

    @Test
    public void test() throws FrameGrabber.Exception {
        Loader.load(opencv_core.class);
        Loader.load(opencv_imgproc.class);

        // todo 此示例是可行，但opencv官网未提供haarcascade_car.xml 车辆检测Haar特征分类器。
        // 不推荐这种方式，最好使用如YOLO、SSD或Faster R-CNN等。这些模型通常比传统的Haar特征分类器更准确。
        File cascadeFile = new File("D:\\temp\\opencv\\haarcascade\\haarcascade_car.xml");
        CascadeClassifier faceDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());

        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspUrl);
        grabber.start();

        while (true) {
            // Frame frame = grabber.grab();
            Frame frame = grabber.grabImage();
            if (frame == null) {
                System.out.println("No frame grabbed, exiting...");
                break;
            }
            Mat matImage = converter.convert(frame);

            // 转换为灰度图像
            Mat grayImage = new Mat();
            opencv_imgproc.cvtColor(matImage, grayImage, opencv_imgproc.COLOR_BGR2GRAY);

            RectVector faces = new RectVector();
            faceDetector.detectMultiScale(grayImage, faces);

            // 车辆检测
            for (int i = 0; i < faces.size(); i++) {
                org.bytedeco.opencv.opencv_core.Rect faceRect = faces.get(i);
                opencv_imgproc.rectangle(matImage, faceRect.tl(), faceRect.br(), new Scalar(0, 255, 0, 128));
            }

            // 定义输出的文件路径
            String outputPath = "target/" + System.currentTimeMillis() + "_snapshot.png";

            // 将 matImage 写入到本地PNG文件
            boolean isSuccess = opencv_imgcodecs.imwrite(outputPath, matImage);

            if (isSuccess) {
                System.out.println("Image saved successfully to " + outputPath);
            } else {
                System.err.println("Failed to save the image.");
            }

            // 为了防止程序过快，可以添加一些延时
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        grabber.stop();
        faceDetector.close();
    }

}
