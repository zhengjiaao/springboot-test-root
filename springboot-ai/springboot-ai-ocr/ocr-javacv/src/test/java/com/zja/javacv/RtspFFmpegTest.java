package com.zja.javacv;

import org.apache.commons.io.FileUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Author: zhengja
 * @Date: 2024-06-04 15:53
 */
public class RtspFFmpegTest {

    // RTSP stream URL
    String rtspUrl = "rtsp://your_rtsp_stream_url";


    // 从RTSP视频流中抓拍一张图片
    @Test
    public void captureSnapshot_test1() {
        String outputFile = "target/snapshot.jpg"; // 输出的截图文件名

        String command = String.format("ffmpeg -i %s -vframes 1 %s", rtspUrl, outputFile);
        try {
            Process process = Runtime.getRuntime().exec(command);

            process.waitFor(); // 等待命令执行完成

            System.out.println("截图成功!");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 从RTSP视频流中抓拍一张图片
    @Test
    public void captureSnapshot_test2() {
        String outputFile = "target/snapshot2.jpg"; // 输出的截图文件名

        try {
            // 构建FFmpeg命令
            ProcessBuilder builder = new ProcessBuilder("ffmpeg", "-i", rtspUrl, "-frames:v", "1", "-f", "image2", "-y", outputFile);

            Process process = builder.start();
            process.waitFor(); // 等待FFmpeg进程执行完成
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 从RTSP视频流中抓拍一张图片
    @Test
    public void captureSnapshot_test3() {
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspUrl);
            grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
            grabber.setImageWidth(960);
            grabber.setImageHeight(540);

            grabber.start();
            // grabber.setFormat("h264");
            // 设置读取的最大数据，单位字节
            // grabber.setOption("probesize", "10000");
            // 设置分析的最长时间，单位微秒
            // grabber.setOption("analyzeduration", "10000");
            // grabber.setOption("rtsp_transport", "tcp");
            // grabber.setOption("user", "");
            // grabber.setOption("password", "");
            // grabber.setOption("password", "");
            // grabber.setImageWidth(1280);
            // grabber.setImageHeight(720);
            // grabber.setFormat("MJPEG");
            // grabber.setFormat("h264");
            // // grabber.setFormat("rtsp");
            // grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            // grabber.setFrameRate(25);
            // grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            // grabber.setImageWidth(2610);
            // grabber.setImageHeight(180);
            // grabber.start();

            Frame frame = grabber.grabImage();
            if (frame != null && frame.image != null) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.getBufferedImage(frame);
                boolean jpg = ImageIO.write(bufferedImage, "jpg", new File("target/a.jpg"));
                System.out.println(jpg);
                grabber.stop();
                grabber.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 从RTSP视频流中抓拍一张图片
    @Test
    public void captureSnapshot_test4() throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspUrl);
        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
        grabber.setImageWidth(1920);
        grabber.setImageHeight(1080);
        // grabber.setOption("user", "");
        // grabber.setOption("password", "");
        System.out.println("grabber start");
        grabber.start();

        // 1.播放视频
/*		CanvasFrame canvasFrame = new CanvasFrame("摄像机");
		canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvasFrame.setAlwaysOnTop(true);
		OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
		while (true){
			Frame frame = grabber.grabImage();
			Mat mat = converter.convertToMat(frame);
			canvasFrame.showImage(frame);
		}*/

        // 2.帧截图
        File outPut = new File("target/b.jpg");
        while (true) {
            Frame frame = grabber.grabImage();
            if (frame != null) {
                ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
                grabber.stop();
                grabber.release();
                System.out.println("图片已保存");
                break;
            }
        }
    }

    /**
     * 创建BufferedImage对象
     */
    public static BufferedImage FrameToBufferedImage(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
//		bufferedImage=rotateClockwise90(bufferedImage);
        return bufferedImage;
    }

    /**
     * 处理图片，将图片旋转90度。
     */
    public static BufferedImage rotateClockwise90(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        BufferedImage bufferedImage = new BufferedImage(height, width, bi.getType());
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bufferedImage.setRGB(j, i, bi.getRGB(i, j));
        return bufferedImage;
    }

    // 根据RTSP视频流进行实时播放视频流
    @Test
    public void captureSnapshot_test5() throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspUrl);
        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重
        grabber.setImageWidth(1920);
        grabber.setImageHeight(1080);
        // grabber.setOption("user", "");
        // grabber.setOption("password", "");
        System.out.println("grabber start");
        grabber.start();

        // 1.播放视频
        CanvasFrame canvasFrame = new CanvasFrame("摄像机");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasFrame.setAlwaysOnTop(true);
        // OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        while (true) {
            Frame frame = grabber.grabImage();
            // Mat mat = converter.convertToMat(frame);
            canvasFrame.showImage(frame);
        }
    }

    // 从RTSP视频流中录一段视频
    @Test
    public void captureSnapshot_test6() throws IOException {
        // Initialize frame grabber 初始化帧抓取器
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtspUrl);
        try {
            grabber.setOption("rtsp_transport", "tcp"); // Set transport protocol (optional) 设置传输协议（可选）
            grabber.start();

            // Create a frame recorder to record frames as video 创建帧记录器以将帧录制为视频
            // FrameRecorder recorder = FrameRecorder.createDefault("target/output.mp4", grabber.getImageWidth(), grabber.getImageHeight());
            FrameRecorder recorder = new FFmpegFrameRecorder("target/output.mp4", grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
            recorder.setFormat("mp4");
            // recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);  // todo 暂时不设置，会导致无法打开视频，报错：deprecated pixel format used, make sure you did set range correctly
            // recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoQuality(0); // 最高质量

            recorder.start();

            // 限制10s后结束录制
            long startTime = System.currentTimeMillis();
            final long recordDurationInMillis = 10 * 1000; // 10 seconds in milliseconds

            // Read frames from the RTSP stream and write them to the recorder 从RTSP流中读取帧并将其写入记录器
            Frame frame;
            while ((System.currentTimeMillis() - startTime) < recordDurationInMillis) {
                frame = grabber.grab();
                if (frame != null) {
                    recorder.record(frame);
                } else {
                    // Handle the case where frame is null, e.g., log a message or break the loop.
                    System.err.println("Frame is null, stopping recording early.");
                    break;
                }
            }

            recorder.stop();
            recorder.release();

            // Convert the recorded video file to InputStream 将录制的视频文件转换为InputStream
            // byte[] videoBytes = FileUtils.readFileToByteArray(new File("target/output.mp4"));
            // InputStream inputStream = new ByteArrayInputStream(videoBytes);

            // System.out.println("RTSP stream converted to InputStream successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release resources
            try {
                grabber.stop();
                grabber.release();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }
}
