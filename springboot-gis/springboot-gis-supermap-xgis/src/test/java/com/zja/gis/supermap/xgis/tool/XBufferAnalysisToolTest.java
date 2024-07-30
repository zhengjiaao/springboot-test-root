package com.zja.gis.supermap.xgis.tool;

import com.dist.xdata.gis.tool.XBufferAnalysisTool;
import com.dist.xdata.gis.tool.XDatasourceTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-23 14:55
 */
@SpringBootTest
public class XBufferAnalysisToolTest {

    @Autowired
    XDatasourceTool xDatasourceTool;

    @Autowired
    XBufferAnalysisTool xBufferAnalysisTool;

    @Test
    public void test() {
        // 缓冲区分析
        // xBufferAnalysisTool.createBuffer();
    }
}
