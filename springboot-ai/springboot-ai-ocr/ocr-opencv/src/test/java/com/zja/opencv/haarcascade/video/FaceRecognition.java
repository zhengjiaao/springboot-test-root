package com.zja.opencv.haarcascade.video;

import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

/**
 * 人脸检测：图片、视频、摄像头
 *
 * @Author: zhengja
 * @Date: 2024-05-21 10:45
 */
public class FaceRecognition {

    @Test
    public void test() {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 加载人脸级联分类器
        //CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml"); // 尽可能检测更多人脸，存在不正确检查情况
        //CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml"); // 存在检测不全情况
        //CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml"); // 检测效果较好
        CascadeClassifier faceCascade = new CascadeClassifier("D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml"); //检测效果最好

        // 图片人脸检测
        //detectFacesInImage("D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\Brush1.png", faceCascade);

        // 视频人脸检测
        detectFacesInVideo("D:\\temp\\opencv\\Video\\1.mp4", faceCascade);

        // 摄像头人脸检测 todo 待测试，没条件
        //detectFacesFromCamera(faceCascade);
    }

    private static void detectFacesInImage(String imagePath, CascadeClassifier faceCascade) {
        // 读取图像文件
        Mat image = Imgcodecs.imread(imagePath);

        // 灰度转换
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 人脸检测
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(gray, faces);

        // 在检测到的人脸区域绘制矩形框
        System.out.println("Number of faces detected: " + faces.toArray().length);
        //System.out.println("Number of faces detected: " + faces.size());
        int i = 0;
        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
            i = i + 1;
            System.out.println("Face " + i + ": " + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
        }

        // 输出识别结果图片
        String outputImagePath = "target/output.png";
        Imgcodecs.imwrite(outputImagePath, image);
        System.out.println("人脸检测完成，结果保存在：" + outputImagePath);

        // 显示结果图像
        HighGui.imshow("Face Detection", image);
        HighGui.waitKey();
    }

    private static void detectFacesInVideo(String videoPath, CascadeClassifier faceCascade) {
        // 打开视频文件
        VideoCapture videoCapture = new VideoCapture(videoPath);

        // 检查视频是否成功打开
        if (!videoCapture.isOpened()) {
            System.out.println("无法打开视频文件");
            return;
        }

        // 获取视频的基本信息
        // 获取视频帧率和尺寸
        double frameRate = videoCapture.get(Videoio.CAP_PROP_FPS); // fps
        int frameWidth = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH); // width
        int frameHeight = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT); // height

        // 创建输出视频的编码器和写入器,使用VideoWriter时，确保已经正确安装了FFmpeg并将其添加到系统的环境变量中。
        //int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G'); // 无效编码，输出存在问题
        int fourcc = VideoWriter.fourcc('X', '2', '6', '4');  // 使用H.264编码器
        String outputVideoPath = "target/output.mp4";
        VideoWriter videoWriter = new VideoWriter(outputVideoPath, fourcc, frameRate, new Size(frameWidth, frameHeight));

        // 创建窗口来显示结果
        HighGui.namedWindow("Face Detection");

        // 逐帧读取视频并进行人脸检测
        Mat frame = new Mat();
        while (videoCapture.read(frame)) {
            // 转换为灰度图像
            Mat grayFrame = new Mat();
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

            // 人脸检测
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(grayFrame, faces);

            // 在检测到的人脸区域绘制矩形框
            System.out.println("Frame: " + videoCapture.get(Videoio.CAP_PROP_POS_FRAMES));
            System.out.println("Number of faces detected: " + faces.toArray().length);
            //System.out.println("Number of faces detected: " + faces.size());
            int i = 0;
            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
                i = i + 1;
                System.out.println("Face " + i + ": " + rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
            }

            // 写入输出视频
            videoWriter.write(frame);

            // 显示结果帧
            HighGui.imshow("Face Detection", frame);

            // 按ESC键退出
            if (HighGui.waitKey((int) Math.round(1000 / frameRate)) == 27) {
                break;
            }
        }

        // 释放视频捕获资源
        videoCapture.release();
        // 释放资源
        videoWriter.release();
        HighGui.destroyAllWindows();

        System.out.println("人脸检测完成，结果保存在：" + outputVideoPath);
    }

    private static void detectFacesFromCamera(CascadeClassifier faceCascade) {
        // 打开摄像头
        VideoCapture videoCapture = new VideoCapture(0);

        // 检查摄像头是否成功打开
        if (!videoCapture.isOpened()) {
            System.out.println("无法打开摄像头");
            return;
        }

        // 获取摄像头的默认帧率和尺寸
        double frameRate = videoCapture.get(Videoio.CAP_PROP_FPS);
        //int frameWidth = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        //int frameHeight = (int) videoCapture.get(Videoio.CAPROP_FRAME_HEIGHT);

        // 创建窗口来显示结果
        HighGui.namedWindow("Face Detection");

        // 逐帧读取摄像头图像并进行人脸检测
        Mat frame = new Mat();
        while (true) {
            // 读取摄像头图像
            videoCapture.read(frame);

            // 转换为灰度图像
            Mat gray = new Mat();
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

            // 人脸检测
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(gray, faces);

            // 在检测到的人脸区域绘制矩形框
            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
            }

            // 显示结果帧
            HighGui.imshow("Face Detection", frame);

            // 按ESC键退出
            if (HighGui.waitKey((int) Math.round(1000 / frameRate)) == 27) {
                break;
            }
        }

        // 释放视频捕获资源
        videoCapture.release();
    }
}
