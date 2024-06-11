package com.zja.javacv.rtsp;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;


/**
 * 带暂停的rtsp 到 mp 4
 *
 * @Author: zhengja
 * @Date: 2024-06-05 10:01
 */
public class RtspToMp4WithPause {
    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private boolean isRecording = true; // 初始化为正在录制状态
    private String outputFilePath = "target/output.mp4"; // 输出文件路径

    public RtspToMp4WithPause(String rtspUrl) throws Exception {
        grabber = new FFmpegFrameGrabber(rtspUrl);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();

        recorder = new FFmpegFrameRecorder(outputFilePath, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
        recorder.setFormat("mp4");
        // recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);  // todo 暂时不设置，会导致无法打开视频，报错：deprecated pixel format used, make sure you did set range correctly
        // recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoQuality(0); // 最高质量

        recorder.start();
    }

    // 暂停录制
    public void pauseRecording() {
        if (isRecording) {
            isRecording = false;
            try {
                Thread.sleep(100);
                recorder.stop();
                recorder.release(); // 释放当前的Recorder，以便后续重新开始
                recorder = null; // 清空引用
                System.out.println("Recording paused.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Recording is already paused.");
        }
    }

    // 恢复录制
    public void resumeRecording() throws Exception {
        if (!isRecording) {
            if (recorder == null) {
                recorder = new FFmpegFrameRecorder(outputFilePath, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
                recorder.setFormat("mp4");
                // recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                // recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
                recorder.setFrameRate(grabber.getFrameRate());
                recorder.setVideoQuality(0);
                recorder.start();
            }
            isRecording = true;
            System.out.println("Recording resumed.");
        } else {
            System.out.println("Recording is already ongoing.");
        }
    }

    public void stopRecording() throws Exception {
        if (isRecording) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            grabber.stop();
            grabber.release();
            System.out.println("Recording stopped.");
        }
    }

    public void run() {
        Frame frame;
        while (true) {
            if (isRecording) {
                try {
                    frame = grabber.grab();
                    if (frame != null) {
                        recorder.record(frame);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            } else {
                // 当暂停时，可以在这里添加逻辑等待恢复信号，或者睡眠以减少CPU占用
                try {
                    Thread.sleep(100); // 等待一秒后再次检查状态
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            RtspToMp4WithPause recorderManager = new RtspToMp4WithPause("rtsp://your_rtsp_stream_url");
            new Thread(recorderManager::run).start(); // 在新线程中运行以避免阻塞主线程

            // 示例：暂停和恢复录制
            Thread.sleep(5000); // 录制5秒后暂停
            recorderManager.pauseRecording();
            Thread.sleep(3000); // 暂停3秒后恢复录制
            recorderManager.resumeRecording();

            // 根据需要停止录制
            Thread.sleep(8000); // 录制8秒后停止录制
            recorderManager.stopRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
