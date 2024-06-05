package com.zja.javacv;

import org.junit.Test;

/**
 * 抓拍测试
 *
 * @Author: zhengja
 * @Date: 2024-06-05 11:11
 */
public class RtspSnapshotTest {

    // RTSP stream URL
    String rtspUrl = "rtsp://your_rtsp_stream_url";

    @Test
    public void toImage() {
        RtspSnapshot.toImage(rtspUrl);
    }

    @Test
    public void toImageV2() {
        RtspSnapshot.toImageV2(rtspUrl);
    }
}
