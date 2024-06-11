package com.zja.javacv.rtsp;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 抓拍:Rtsp To PNG
 *
 * @Author: zhengja
 * @Date: 2024-06-05 11:02
 */
public class RtspSnapshot {

    public static void toImage(String rtspUrl) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtspUrl)) {
            grabber.setOption("rtsp_transport", "tcp");
            grabber.start();

            // 获取第一帧
            Frame frame = grabber.grabImage();
            if (frame != null) {
                if (frame.image != null) {
                    // 保存为PNG图片
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage bufferedImage = converter.getBufferedImage(frame);
                    File outputFile = new File("target/" + System.currentTimeMillis() + "_snapshot.png");
                    boolean result = ImageIO.write(bufferedImage, "png", outputFile);
                    System.out.println("抓取结果：" + result);
                    System.out.println(outputFile.getAbsolutePath());
                    grabber.stop();
                    grabber.release();
                } else {
                    System.err.println("No image data available");
                }
            } else {
                System.err.println("No frame grabbed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 提高抓取帧的效率
     * 有时候，初次尝试抓取的帧可能为空，可以尝试连续抓取几帧后再进行处理；
     * 尝试抓取帧时加入了重试机制，并对可能发生的各种异常进行了更细致的捕获和处理；
     */
    public static void toImageV2(String rtspUrl) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtspUrl)) {
            grabber.setOption("rtsp_transport", "tcp");
            grabber.start();

            int attempt = 0;
            final int maxAttempts = 5;
            Frame frame = null;

            // 尝试抓取多帧以提高成功率
            while (frame == null && attempt < maxAttempts) {
                frame = grabber.grabImage();
                attempt++;
                if (frame == null) {
                    System.out.println("Attempt " + attempt + ": No frame grabbed, retrying...");
                    Thread.sleep(1000); // 等待一秒再尝试
                }
            }

            if (frame != null) {
                if (frame.image != null) {
                    // 保存为PNG图片
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage bufferedImage = converter.getBufferedImage(frame);
                    File outputFile = new File("target/" + System.currentTimeMillis() + "_snapshot.png");
                    boolean result = ImageIO.write(bufferedImage, "png", outputFile);
                    System.out.println("抓取结果：" + result);
                    System.out.println(outputFile.getAbsolutePath());
                    grabber.stop();
                    grabber.release();
                } else {
                    System.err.println("Frame grabbed but no image data available");
                }
            } else {
                System.err.println("Failed to grab any frame after " + maxAttempts + " attempts");
            }
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("FFmpeg related error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String rtspUrl = "rtsp://your_rtsp_stream_url";
        RtspSnapshot.toImage(rtspUrl);
    }
}
