package com.zja.javacv;

import org.junit.Test;

/**
 * @Author: zhengja
 * @Date: 2024-06-05 10:52
 */
public class RtspToMp4WithPauseTest {

    // RTSP stream URL
    String rtspUrl = "rtsp://your_rtsp_stream_url";


    // 带暂停的录制：rtspToMp4
    @Test
    public void rtspToMp4_test() {
        try {
            RtspToMp4WithPause recorderManager = new RtspToMp4WithPause(rtspUrl);
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
