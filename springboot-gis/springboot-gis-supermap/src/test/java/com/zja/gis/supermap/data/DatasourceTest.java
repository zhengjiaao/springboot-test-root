package com.zja.gis.supermap.data;

import com.supermap.data.*;
import com.supermap.data.conversion.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * 数据源 Datasource
 * @Author: zhengja
 * @Date: 2024-07-18 11:28
 */
@Slf4j
@SpringBootTest
public class DatasourceTest {

    static final String TEMP_PATH = "D:\\temp";
    static final String UDBX_PATH = "D:\\temp\\udbx";

    /**
     * 打开 UDBX 数据源
     */
    @Test
    public void open_UDBX_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "test.udbx").toString();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 打开数据源
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);
        if (datasource != null) {
            // 打印数据源信息
            System.out.println(datasource.getConnectionInfo().getServer());
            System.out.println(datasource.getConnectionInfo().getEngineType());
            System.out.println(datasource.getConnectionInfo().getAlias());

            // 数据集
            Datasets datasets = datasource.getDatasets();

            // 获取指定数据集
            /*DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            Map<Integer, Feature> features = datasetVector.getAllFeatures();
            features.forEach((key, feature) -> {
                System.out.println(key + "=" + feature);
                System.out.println(feature.getString("name")); // 获取属性
            });*/

            for (int i = 0; i < datasets.getCount(); i++) {
                System.out.println("----------------------------------");

                Dataset dataset = datasets.get(i);

                System.out.println("datasetName=" + dataset.getName());
                System.out.println("datasetType=" + dataset.getType());
                System.out.println("datasetID=" + dataset.getID());
                System.out.println("datasetSchema=" + dataset.getSchema());
                System.out.println("datasetTableName=" + dataset.getTableName());

                PrjCoordSys prjCoordSys = dataset.getPrjCoordSys();
                if (prjCoordSys != null) {
                    System.out.println("prjCoordSys Name=" + prjCoordSys.getName());
                    System.out.println("prjCoordSys Type=" + prjCoordSys.getType());
                    System.out.println("prjCoordSys CoordUnit=" + prjCoordSys.getCoordUnit());
                    System.out.println("prjCoordSys EPSGCode=" + prjCoordSys.getEPSGCode());
                }

                EncodeType encodeType = dataset.getEncodeType();
                System.out.println("encodeType=" + encodeType);
                Rectangle2D rectangle2D = dataset.getBounds();
                System.out.println("bounds=" + rectangle2D);
                GeoStoreType geoStoreType = dataset.getGeoStoreType();
                System.out.println("geoStoreType=" + geoStoreType);

                if (dataset instanceof DatasetVector) { // DatasetVector、DatasetGrid、DatasetImage
                    DatasetVector datasetVector = (DatasetVector) dataset;
                    // System.out.println(datasetVector.getPrjCoordSys().getName());
                    // System.out.println(datasetVector.getPrjCoordSys().getType());
                    // System.out.println(datasetVector.getPrjCoordSys().getCoordUnit());
                    // System.out.println(datasetVector.getPrjCoordSys().getEPSGCode());

                    Map<Integer, Feature> features = datasetVector.getAllFeatures();
                    features.forEach((key, feature) -> {
                        System.out.println(key + "=" + feature);
                        System.out.println(feature.getString("name")); // 获取属性
                    });
                }
            }

            workspace.save();
            workspace.close();
        }
    }

    @Test
    public void open_SHP_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "ShpExport", "上海润和总部园.shp").toString();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.VECTORFILE);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 打开数据源
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);
        if (datasource != null) {
            // 打印数据源信息
            System.out.println(datasource.getConnectionInfo().getServer());
            System.out.println(datasource.getConnectionInfo().getEngineType());
            System.out.println(datasource.getConnectionInfo().getAlias());

            // 获取指定数据集
            DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            Map<Integer, Feature> features = datasetVector.getAllFeatures();
            features.forEach((key, feature) -> {
                System.out.println(key + "=" + feature);
                System.out.println(feature.getString("name")); // 获取属性
            });

            // 全部数据集
            Datasets datasets = datasource.getDatasets();

            for (int i = 0; i < datasets.getCount(); i++) {
                System.out.println("----------------------------------");
                Dataset dataset = datasets.get(i);
                System.out.println(dataset.getName());
            }
        }
    }

    @Test
    public void open_GDB_test() {
        String dataFile = Paths.get(TEMP_PATH, "gdb", "上海润和总部园.gdb").toString();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.VECTORFILE);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 打开数据源
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        if (datasource != null) {
            // 打印数据源信息
            System.out.println(datasource.getConnectionInfo().getServer());
            System.out.println(datasource.getConnectionInfo().getEngineType());
            System.out.println(datasource.getConnectionInfo().getAlias());

            // 全部数据集
            Datasets datasets = datasource.getDatasets();

            for (int i = 0; i < datasets.getCount(); i++) {
                System.out.println("----------------------------------");
                Dataset dataset = datasets.get(i);
                System.out.println(dataset.getName());
            }

            // 获取指定数据集
         /*   DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            Map<Integer, Feature> features = datasetVector.getAllFeatures();
            features.forEach((key, feature) -> {
                System.out.println(key + "=" + feature);
                System.out.println(feature.getString("name")); // 获取属性
            });*/
        }
    }

    @Test
    @Deprecated // todo 打开数据源失败
    public void open_MDB_test() {
        String dataFile = Paths.get(TEMP_PATH, "mdb", "上海润和总部园.mdb").toString();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.MDB);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 打开数据源
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        if (datasource != null) {
            // 打印数据源信息
            System.out.println(datasource.getConnectionInfo().getServer());
            System.out.println(datasource.getConnectionInfo().getEngineType());
            System.out.println(datasource.getConnectionInfo().getAlias());

            // 全部数据集
            Datasets datasets = datasource.getDatasets();

            for (int i = 0; i < datasets.getCount(); i++) {
                System.out.println("----------------------------------");
                Dataset dataset = datasets.get(i);
                System.out.println(dataset.getName());
            }

            // 获取指定数据集
            /*DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            Map<Integer, Feature> features = datasetVector.getAllFeatures();
            features.forEach((key, feature) -> {
                System.out.println(key + "=" + feature);
                System.out.println(feature.getString("name")); // 获取属性
            });*/
        }
    }


    @Test
    public void open_FileDatasource_test() {
        // 打开工作空间
        Workspace workspace = new Workspace();

        // 打开数据源
        Datasource dataSource = openFileDataSource(workspace, Paths.get(TEMP_PATH, "shp", "上海润和总部园", "上海润和总部园.shp").toString());
        if (dataSource != null) {
            System.out.println(dataSource.getConnectionInfo().getServer());
            System.out.println(dataSource.getConnectionInfo().getEngineType());
            System.out.println(dataSource.getConnectionInfo().getAlias());
            System.out.println(dataSource.getDatasets().getCount());

            // 获取全部数据集
            Datasets datasets = dataSource.getDatasets();
            for (int i = 0; i < datasets.getCount(); i++) {
                System.out.println("----------------------------------");
                Dataset dataset = datasets.get(i);
                System.out.println(dataset.getName());
            }
        }
    }

    public static Datasource openFileDataSource(Workspace workspace, String dataFile) {
        DatasourceConnectionInfo datasourceConnectionInfo = openFileDatasourceConnectionInfo(dataFile);
        return openDataSource(workspace, datasourceConnectionInfo);
    }

    public static Datasource openDataSource(Workspace workspace, DatasourceConnectionInfo datasourceConnectionInfo) {
        Datasource datasource = null;
        if (workspace != null) {
            String alias = datasourceConnectionInfo.getAlias();
            if ("UntitledDatasource".equals(alias)) {
                datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());
            }

            Datasources datasources = workspace.getDatasources();
            if (datasources.contains(alias)) {
                return datasources.get(alias);
            }

            datasource = workspace.getDatasources().open(datasourceConnectionInfo);
        }

        return datasource;
    }

    public static DatasourceConnectionInfo openFileDatasourceConnectionInfo(String dataFile) {
        String lowDataFile = dataFile.toLowerCase();
        EngineType dataType;
        if (lowDataFile.lastIndexOf(".udbx") != -1) {
            dataType = EngineType.UDBX;
        } else if (lowDataFile.lastIndexOf(".udb") != -1) {
            dataType = EngineType.UDB;
        } else if (lowDataFile.lastIndexOf(".shp") == -1 && lowDataFile.lastIndexOf(".dwg") == -1 && lowDataFile.lastIndexOf(".dxf") == -1 && lowDataFile.lastIndexOf(".gml") == -1 && lowDataFile.lastIndexOf(".csv") == -1) {
            if (lowDataFile.lastIndexOf(".tif") == -1 && lowDataFile.lastIndexOf(".tiff") == -1 && lowDataFile.lastIndexOf(".bmp") == -1 && lowDataFile.lastIndexOf(".gif") == -1 && lowDataFile.lastIndexOf(".jpg") == -1 && lowDataFile.lastIndexOf(".img") == -1 && lowDataFile.lastIndexOf(".png") == -1) {
                return null;
            }

            dataType = EngineType.IMAGEPLUGINS;
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
}
