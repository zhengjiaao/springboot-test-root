package com.zja.gis.supermap.data;

import com.supermap.data.*;
import com.supermap.data.conversion.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 数据转换模块 导出数据
 * @Author: zhengja
 * @Date: 2024-07-18 10:03
 */
@Slf4j
@SpringBootTest
public class DataConversionAndExportTest {

    static final String UDBX_PATH = "D:\\temp\\udbx";

    /**
     * 导出udbx文件为GeoJson文件：单个图层导出为GeoJson文件
     */
    @Test
    public void exportUDBXToGeoJson() {
        String udbxFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();
        String exportFile = Paths.get(UDBX_PATH, "上海润和总部园.geojson").toString();

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(udbxFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开数据源
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        // 获取数据集
        DatasetVector sourceDataset = (DatasetVector) datasource.getDatasets().get("上海润和总部园");

        // 转换并导出数据文件
        exportGeoJson(sourceDataset, exportFile);
    }

    public static void exportGeoJson(DatasetVector sourceDatasetVector, String exportGeoJsonFile) {
        ExportSettingGeoJson exportSetting = new ExportSettingGeoJson();
        exportSetting.setOverwrite(true); // 默认 false
        exportSetting.setTargetFileType(FileType.GEOJSON);
        exportSetting.setTargetFilePath(exportGeoJsonFile);
        exportSetting.setTargetFileCharset(Charset.UTF8);
        exportSetting.setSourceData(sourceDatasetVector);

        log.info("*****************开始转换udbx文件为GeoJson文件*****************");
        DataExport dataExport = new DataExport();
        dataExport.getExportSettings().add(exportSetting);
        dataExport.run();
        dataExport.dispose();
        log.info("*****************成功转换udbx文件为GeoJson文件.*****************");
    }

    /**
     * 单个图层导出为shp文件
     */
    @Test
    public void exportUDBXToSHP() {
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();
        String exportFile = Paths.get(UDBX_PATH, "ExportShp", "上海润和总部园.shp").toString();

        if (!new File(exportFile).getParentFile().exists()) {
            new File(exportFile).getParentFile().mkdirs();
        }

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开数据源
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        // 获取数据集
        DatasetVector sourceDataset = (DatasetVector) datasource.getDatasets().get("上海润和总部园");

        // 导出数据集为 shp文件格式
        exportSHP(sourceDataset, exportFile);
    }

    public void exportSHP(DatasetVector sourceDatasetVector, String shpFilePath) {
        ExportSetting exportSetting = new ExportSetting();
        exportSetting.setOverwrite(true);
        exportSetting.setTargetFileCharset(Charset.UTF8);
        exportSetting.setTargetFileType(FileType.SHP);
        exportSetting.setTargetFilePath(shpFilePath);
        exportSetting.setSourceData(sourceDatasetVector);

        log.info("*****************开始转换udbx文件为shp文件*****************");
        DataExport dataExport = new DataExport();
        dataExport.getExportSettings().add(exportSetting);
        dataExport.run();
        dataExport.dispose();
        log.info("*****************成功转换udbx文件为shp文件.*****************");
    }

}