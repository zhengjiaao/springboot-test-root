package com.zja.javacv;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: zhengja
 * @Date: 2024-06-06 9:35
 */
public class FFmpegExample {
    private static final Logger logger = LoggerFactory.getLogger(FFmpegExample.class);

    @Test
    public void test() {

        // 创建了一个FFmpegFrameGrabber对象来读取输入视频文件
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("D:\\temp\\opencv\\Video\\input_1.mp4");
        // 创建了一个FFmpegFrameRecorder对象来写入输出视频文件
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("D:\\temp\\opencv\\Video\\output_1.mp4", 1280, 720, 2);

        try {
            grabber.start();
            recorder.start();

            // 使用grabFrame()方法从输入视频中逐帧读取，并使用record()方法将帧写入输出视频
            Frame frame;
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }
        } catch (Exception e) {
            logger.error("Error processing video", e);
        } finally {
            if (recorder != null) {
                try {
                    recorder.stop();
                } catch (Exception e) {
                    logger.error("Error stopping recorder", e);
                }
            }
            if (grabber != null) {
                try {
                    grabber.stop();
                } catch (Exception e) {
                    logger.error("Error stopping grabber", e);
                }
            }
        }
    }
}
