package com.zja.gis.supermap.xgis.tool;

import com.dist.xdata.gis.base.MapServerHelper;
import com.dist.xdata.gis.base.MdbResolveHelper;
import com.dist.xdata.gis.data.*;
import com.dist.xdata.gis.mapserver.QueryPara;
import com.dist.xdata.gis.model.GeoJsonObject;
import com.dist.xdata.gis.tool.XDatasourceTool;
import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * 读取空间数据 ReadSpatialData
 * @Author: zhengja
 * @Date: 2024-07-18 10:10
 */
@SpringBootTest
public class XDatasourceToolTest {

    @Autowired
    XDatasourceTool xDatasourceTool;

    /**
     *  读取空间矢量数据(GDB/SHP/UDBX...)
     */
    @Test
    public void readSpatialData_VectorData() {
        String filePath = "D:\\temp\\udbx\\二维矢量数据.udbx"; // (GDB/SHP/UDBX...)
        XDatasource xDatasource = xDatasourceTool.openDatasource(filePath);

        // 获取图层信息列表
        List<XDatasetInfo> allDatasetInfos = xDatasource.getAllDatasetInfos();

        for (XDatasetInfo allDatasetInfo : allDatasetInfos) {
            System.out.println(allDatasetInfo.getName());
            // 打开图层
            XFeatureClass xFeatureClass = xDatasource.openFeatureClass(allDatasetInfo.getName());

            // 查询图层信息
            XFeatureCursor xFeatureCursor = xFeatureClass.query(null);
            xFeatureCursor.moveFirst();
            XFeature xFeature;
            while ((xFeature = xFeatureCursor.getNextFeature()) != null) {
                // 获取该要素的字段信息
                Map<String, Object> allFieldInfos = xFeature.getAllFieldInfos(true);
                System.out.println(allFieldInfos);

            }
        }
        xDatasourceTool.close(xDatasource);
    }

    /**
     * 读取MDB数据(以表格形式读取)
     */
    @Test
    public void readSpatialData_MDB() {
        String mdbPath = "D:\\temp\\mdb\\2021年城市体检评估指标体系标准.mdb";

        MdbResolveHelper mdbResolveHelper = new MdbResolveHelper(mdbPath);
        // 获取表格名称
        List<String> tableNames = mdbResolveHelper.getTableNames();

        for (String tableName : tableNames) {
            Table jackTable = mdbResolveHelper.getTable(tableName);
            // 获取列名
            List<Column> columns = (List<Column>) jackTable.getColumns();

            for (Row row : jackTable) {
                for (Column column : columns) {
                    Object value = row.get(column);
                    System.out.println(value);
                }
            }
        }
        mdbResolveHelper.close();
    }

    /**
     * 读取地图服务数据
     */
    @Test
    public void ReadMapService() {
        // 暂时支持arcgis动态服务、超图data和map服务，后面服务可扩展
        String mapServiceUrl = "http://192.168.200.34:16080/arcgis/rest/services/XZQH/XZQH_Buffer/MapServer/0";

        // 可以支持SQL筛选、空间字符串筛选、指定输出字段等等
        QueryPara queryPara = QueryPara.builder().where("1=1").build();

        // 小数据量可以直接返回geoJson对象(没有一千条限制)
        GeoJsonObject geoJsonObject = MapServerHelper.query(mapServiceUrl, queryPara);
        System.out.println(geoJsonObject);

        // 大数据量可以返回本地geoJson文件,一千条要素为一个文件
        List<String> geoJsonFileList = MapServerHelper.query2GeoJsonFile(mapServiceUrl, queryPara);
        System.out.println(geoJsonFileList);
    }

}
