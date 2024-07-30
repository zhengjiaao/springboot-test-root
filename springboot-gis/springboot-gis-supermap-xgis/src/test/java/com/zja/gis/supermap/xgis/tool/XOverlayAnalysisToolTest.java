package com.zja.gis.supermap.xgis.tool;

import com.dist.xdata.gis.tool.XOverlayAnalysisTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 叠加分析工具
 * @Author: zhengja
 * @Date: 2024-07-23 14:52
 */
@SpringBootTest
public class XOverlayAnalysisToolTest {

    @Autowired
    XOverlayAnalysisTool xOverlayAnalysisTool;

    @Test
    public void overlayAnalysis() {
        // TODO
        // xOverlayAnalysisTool.intersect();
    }

}
