package com.zja.gis.arcgis;

import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;

import java.nio.file.Paths;

/**
 *
 * @Author: zhengja
 * @Date: 2024-08-06 14:06
 */
@Deprecated // todo 未测试成功，java 版本 与 Arcgis版本不兼容，或者没有安装Arcgis环境
public class ReadMdbFieldAliasesTest {

    static final String MDB_PATH = "D:\\temp\\mdb";

    public static void main(String[] args) {
        // 假设你已经有了mdbFilePath，它是MDB文件的路径
        String mdbFilePath = Paths.get(MDB_PATH, "1111.mdb").toString();

        // 打开geodatabase
        Geodatabase geodatabase = new Geodatabase(mdbFilePath);
        // geodatabase.open(mdbFilePath);

        GeodatabaseFeatureTable geodatabaseFeatureTable = geodatabase.getGeodatabaseFeatureTable("GSZTGNQML");

        Field fieldName1 = geodatabaseFeatureTable.getField("XZQHDM");
        String alias = fieldName1.getAlias();
        System.out.println("Field Name: " + fieldName1.getName() + ", Field Alias: " + alias);

        // 关闭geodatabase
        geodatabase.close();
    }
}
