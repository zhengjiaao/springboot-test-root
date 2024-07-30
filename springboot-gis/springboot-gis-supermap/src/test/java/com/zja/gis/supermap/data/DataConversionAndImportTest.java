package com.zja.gis.supermap.data;

import com.supermap.data.Charset;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.conversion.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 数据转换模块 导入数据
 * @Author: zhengja
 * @Date: 2024-07-18 11:00
 */
@Slf4j
@SpringBootTest
public class DataConversionAndImportTest {

    static final String TEMP_PATH = "D:\\temp";

    @Test
    public void importGDBToUdbx() {
        String gdbPath = Paths.get(TEMP_PATH, "gdb", "上海润和总部园.gdb").toString();
        String toUdbx = importToUdbx(gdbPath);
        System.out.println(toUdbx);
    }

    @Test
    public void importMDBToUdbx() {
        String mdbPath = Paths.get(TEMP_PATH, "mdb", "上海润和总部园.mdb").toString();
        String toUdbx = importToUdbx(mdbPath);
        System.out.println(toUdbx);
    }

    @Test
    public void importSHPToUdbx() {
        String shpPath = Paths.get(TEMP_PATH, "shp", "上海润和总部园", "上海润和总部园.shp").toString();
        String toUdbx = importShpToUdbx(shpPath);
        System.out.println(toUdbx);
    }

    public static DatasourceConnectionInfo openFileDatasourceConnectionInfo(String dataFile) {
        String lowDataFile = dataFile.toLowerCase();
        EngineType dataType;
        if (lowDataFile.lastIndexOf(".udbx") != -1) {
            dataType = EngineType.UDBX;
        } else if (lowDataFile.lastIndexOf(".udb") != -1) {
            dataType = EngineType.UDB;
        } else if (lowDataFile.lastIndexOf(".tif") == -1 && lowDataFile.lastIndexOf(".tiff") == -1 && lowDataFile.lastIndexOf(".bmp") == -1 && lowDataFile.lastIndexOf(".gif") == -1 && lowDataFile.lastIndexOf(".jpg") == -1 && lowDataFile.lastIndexOf(".img") == -1 && lowDataFile.lastIndexOf(".png") == -1) {
            dataType = EngineType.IMAGEPLUGINS;
        } else if (lowDataFile.lastIndexOf(".shp") == -1 && lowDataFile.lastIndexOf(".dwg") == -1 && lowDataFile.lastIndexOf(".dxf") == -1 && lowDataFile.lastIndexOf(".gml") == -1 && lowDataFile.lastIndexOf(".csv") == -1) {
            dataType = EngineType.VECTORFILE;
        } else {
            dataType = EngineType.VECTORFILE;
        }

        return openFileDatasourceConnectionInfo(dataType, dataFile);
    }

    public static DatasourceConnectionInfo openFileDatasourceConnectionInfo(EngineType dataType, String dataFile) {
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(dataType);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());
        return datasourceConnectionInfo;
    }

    public static String importToUdbx(String gdbPathOrMdbPath) {
        String tempDir = (new File(gdbPathOrMdbPath)).getParent();
        String udbxPath = Paths.get(tempDir, FilenameUtils.getName(gdbPathOrMdbPath) + ".udbx").toString();
        File file = new File(udbxPath);
        if (file.exists()) {
            log.info("存在导入的udbx直接返回{}", udbxPath);
            return udbxPath;
        } else {
            log.info("新建udbx文件{}", udbxPath);
            DatasourceConnectionInfo datasourceConnectionInfo = openFileDatasourceConnectionInfo(udbxPath);
            datasourceConnectionInfo.setEngineType(EngineType.UDBX);
            if (gdbPathOrMdbPath.contains("gdb")) {
                importGDB(gdbPathOrMdbPath, datasourceConnectionInfo);
            } else {
                importMdb(gdbPathOrMdbPath, datasourceConnectionInfo, "");
            }

            datasourceConnectionInfo.dispose();
            return udbxPath;
        }
    }

    public static void importMdb(String sourceFile, DatasourceConnectionInfo targetDatasourceInfo, String targetDatasetName) {
        ImportSettingPersonalGDBVector importSetting = new ImportSettingPersonalGDBVector();
        importSetting.setImportMode(ImportMode.APPEND);
        importSetting.setSourceFileCharset(Charset.UTF8);
        importSetting.setIsImportEmptyDataset(true);
        importSetting.setSourceFilePath(sourceFile);
        importSetting.setTargetDatasourceConnectionInfo(targetDatasourceInfo);
        if (targetDatasetName != null && !targetDatasetName.isEmpty()) {
            importSetting.setTargetDatasetName(targetDatasetName);
        }

        log.info("*****************开始转换mdb文件为udbx文件*****************");
        DataImport dataImport = new DataImport();
        dataImport.getImportSettings().add(importSetting);
        ImportResult result = dataImport.run();
        dataImport.dispose();
        log.info("*****************成功转换mdb文件为udbx文件.*****************");
    }

    public static void importGDB(String sourceFile, DatasourceConnectionInfo targetDatasourceInfo) {
        ImportSettingFileGDBVector importSetting = new ImportSettingFileGDBVector();
        importSetting.setImportMode(ImportMode.APPEND);
        importSetting.setSourceFileCharset(Charset.UTF8);
        importSetting.setImportEmptyDataset(true);
        importSetting.setSourceFilePath(sourceFile);
        importSetting.setTargetDatasourceConnectionInfo(targetDatasourceInfo);

        log.info("*****************开始转换gdb文件为udbx文件*****************");
        DataImport dataImport = new DataImport();
        dataImport.getImportSettings().add(importSetting);
        ImportResult result = dataImport.run();
        dataImport.dispose();
        log.info("*****************成功转换gdb文件为udbx文件.*****************");
    }

    public static void importShp(String sourceFile, DatasourceConnectionInfo targetDatasourceInfo, String targetDatasetName) {
        ImportSettingSHP importSetting = new ImportSettingSHP();
        importSetting.setImportMode(ImportMode.APPEND);
        importSetting.setSourceFileCharset(Charset.UTF8);
        importSetting.setSourceFilePath(sourceFile);
        importSetting.setTargetDatasourceConnectionInfo(targetDatasourceInfo);
        importSetting.setTargetDatasetName(targetDatasetName);

        log.info("*****************开始转换shp文件为udbx文件*****************");
        DataImport dataImport = new DataImport();
        dataImport.getImportSettings().add(importSetting);
        ImportResult result = dataImport.run();
        dataImport.dispose();
        log.info("*****************成功转换shp文件为udbx文件.*****************");
    }

    public static String importShpToUdbx(String sourceFile) {
        String tempDir = (new File(sourceFile)).getParent();
        String udbxPath = Paths.get(tempDir, UUID.randomUUID() + ".udbx").toString();
        DatasourceConnectionInfo datasourceConnectionInfo = openFileDatasourceConnectionInfo(udbxPath);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        System.out.println(FilenameUtils.getName(sourceFile));
        importShp(sourceFile, datasourceConnectionInfo, FilenameUtils.getBaseName(sourceFile));
        datasourceConnectionInfo.dispose();
        return udbxPath;
    }

}
