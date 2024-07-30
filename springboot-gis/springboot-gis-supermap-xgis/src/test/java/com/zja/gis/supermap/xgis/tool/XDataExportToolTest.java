package com.zja.gis.supermap.xgis.tool;

import cn.hutool.json.JSONUtil;
import com.dist.xdata.gis.base.TxtResolveHelper;
import com.dist.xdata.gis.data.EnumFileType;
import com.dist.xdata.gis.data.EnumTxtCoordinateType;
import com.dist.xdata.gis.data.XDatasource;
import com.dist.xdata.gis.data.XFeatureClass;
import com.dist.xdata.gis.model.LoadDataDTO;
import com.dist.xdata.gis.tool.XDataExportTool;
import com.dist.xdata.gis.tool.XDatasourceTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 空间数据转换（导入、导出）
 * @Author: zhengja
 * @Date: 2024-07-18 10:16
 */
@SpringBootTest
public class XDataExportToolTest {

    @Autowired
    XDatasourceTool xDatasourceTool;

    @Autowired
    XDataExportTool xDataExportTool;

    /**
     * 1.单个图层导出: udbx 转 shp，并导出 shp
     */
    @Test
    public void exportUdbxToSHP() {
        String udbxPath = "D:\\temp\\udbx\\二维矢量数据.udbx";
        XDatasource xDatasource = xDatasourceTool.openDatasource(udbxPath);
        // XFeatureClass xFeatureClass = xDatasource.openFeatureClass("demo");
        XFeatureClass xFeatureClass = xDatasource.openFeatureClass(0);

        xDataExportTool.exportData(xFeatureClass, EnumFileType.SHP, "demo.shp");
    }

    /**
     * 2.数据源多个图层: gdb 转 shp，并导出 shp
     */
    @Test
    public void exportGdbToSHP() {
        // String gdbPath = "D:\\temp\\gdb\\aa.gdb";
        String gdbPath = "D:\\temp\\gdb\\交通厅.gdb";
        // 目标shp文件夹
        String shpPath = "D:\\temp\\gdb\\toShp";
        XDatasource xDatasource = xDatasourceTool.openDatasource(gdbPath);
        xDataExportTool.exportData(xDatasource, EnumFileType.SHP, shpPath);
    }

    /**
     * 2.数据源多个图层: geoJson 转 dxf，并导出 dxf
     *
     * 其他矢量数据转换导出可以参考此示例，更换导出类型参数即可
     */
    @Test
    public void geoJson2Dxf() {
        String geoJsonPath = "D:\\temp\\geojson\\浙江省.json"; // geojson、json
        String dxfPath = "D:\\temp\\geojson\\to\\toCad.dxf";
        XDatasource xDatasource = xDatasourceTool.openDatasource(geoJsonPath);
        xDataExportTool.exportData(xDatasource, EnumFileType.DXF, dxfPath);
    }

    /**
     * 3.特殊矢量数据: GeoJson 转 界址点TXT，并导出 TXT
     */
    @Test
    public void geoJson2TxT() {
        String geoJson = JSONUtil.readJSONObject(new File("D:\\temp\\geojson\\上海润和总部园.geojson"), Charset.defaultCharset()).toString();
        String targetTxtPath = "D:\\temp\\geojson\\to\\toTest.txt";
        TxtResolveHelper.geoJson2Txt(geoJson, null, targetTxtPath, EnumTxtCoordinateType.BOUNDARY_MARK_TXT);
    }

    /**
     * 3.特殊矢量数据: 界址点TXT 转 GeoJson
     */
    @Test
    public void Txt2GeoJson() {
        String txtPath = "D:\\temp\\geojson\\to\\toTest.txt";
        List<LoadDataDTO> loadDataDTOS = TxtResolveHelper.txt2GeoJson(txtPath, EnumTxtCoordinateType.BOUNDARY_MARK_TXT, null, null);
        System.out.println(JSONUtil.toJsonStr(loadDataDTOS));
    }

}
