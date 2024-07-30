package com.zja.gis.supermap.xgis.tool;

import com.dist.xdata.gis.data.EnumFileType;
import com.dist.xdata.gis.tool.XDataImportTool;
import com.dist.xdata.gis.tool.XDatasourceTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 空间数据转换（导入、导出）
 * @Author: zhengja
 * @Date: 2024-07-23 14:55
 */
@SpringBootTest
public class XDataImportToolTest {

    @Autowired
    XDatasourceTool xDatasourceTool;

    @Autowired
    XDataImportTool xDataImportTool;

    /**
     * 导入shp文件到udbx
     */
    @Test
    public void importShpToUdbx() {
        // todo
        xDataImportTool.importData("", EnumFileType.UDBX, xDatasourceTool.createMemoryDatasource(""));
        // xDataImportTool.importMapServer();
    }

}
