package com.zja.gis.supermap.data;

import com.alibaba.fastjson.support.geo.FeatureCollection;
import com.supermap.data.*;
import com.zja.gis.supermap.util.GeometryUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-23 16:19
 */
@SpringBootTest
public class DatasetVectorTest {

    static final String TEMP_PATH = "D:\\temp";
    static final String UDBX_PATH = "D:\\temp\\udbx";

    /**
     * 打开 UDBX 数据源
     */
    @Test
    public void open_UDBX_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();

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

            // 获取全部数据集
            Datasets datasets = datasource.getDatasets();
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
                }
            }

            System.out.println("===================================");

            // 获取指定数据集
            DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            Map<Integer, Feature> features = datasetVector.getAllFeatures();
            features.forEach((key, feature) -> {
                System.out.println(key + "=" + feature);
                System.out.println("name=" + feature.getString("name")); // 获取属性
                System.out.println("Geometry=\n" + feature.getGeometry()); // 获取属性
                String geoJson = GeometryUtil.geometryToGeoJson(feature.getGeometry());
                System.out.println(geoJson);
                System.out.println(GeometryUtil.geometryToWKT(feature.getGeometry()));
                Geometry geometry = feature.getGeometry();
                System.out.println("GeometryType=" + geometry.getType());
                System.out.println("GeoStyle=" + geometry.getStyle());
                System.out.println("GeometryBounds=" + geometry.getBounds());
                System.out.println("GeometryInnerPoint=" + geometry.getInnerPoint());
                // System.out.println("GeometryXML=" + geometry.toXML());

                System.out.println("----------------------------------");

                if (geometry.getType() == GeometryType.GEOPOINT) {
                    GeoPoint geoPoint = (GeoPoint) geometry;
                } else if (geometry.getType() == GeometryType.GEOREGION) {
                    GeoRegion geoRegion = (GeoRegion) geometry;
                    System.out.println(geoRegion.getArea());
                    System.out.println(geoRegion.getPerimeter());
                    System.out.println(geoRegion.getBounds());
                    System.out.println(geoRegion.getInnerPoint());
                    // System.out.println(geoRegion.toXML());
                }
            });

            workspace.save();
            workspace.close();
        }
    }

    // 创建数据集（空的）
    @Test
    public void createDataset_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园-new.udbx").toString();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 创建数据源
        Datasource datasource = workspace.getDatasources().create(datasourceConnectionInfo);

        if (datasource != null) {
            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
            datasetVectorInfo.setName("上海润和总部园");  // 数据集名称
            datasetVectorInfo.setType(DatasetType.REGION); // 面数据集
            datasetVectorInfo.setEncodeType(EncodeType.NONE);

            Datasets datasets = datasource.getDatasets();
            // datasets.create(datasetVectorInfo);   // 创建数据集，支持：DatasetVectorInfo、DatasetGridInfo、DatasetImageInfo、DatasetVolumeInfo、DatasetTopologyInfo等
            DatasetVector datasetVector = datasets.create(datasetVectorInfo);// 创建数据集，支持：DatasetVectorInfo、DatasetGridInfo、DatasetImageInfo、DatasetVolumeInfo、DatasetTopologyInfo等

            // DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().create(datasetVectorInfo);
            // DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().create(datasetVectorInfo, new PrjCoordSys(PrjCoordSysType.PCS_WGS_1984_WEB_MERCATOR));

            System.out.println(datasetVector.getName());
            System.out.println(datasetVector.getType());
            System.out.println(datasetVector.getPrjCoordSys().getName());
            System.out.println(datasetVector.getPrjCoordSys().getType());
            System.out.println(datasetVector.getPrjCoordSys().getCoordUnit());
            System.out.println(datasetVector.getPrjCoordSys().getEPSGCode());
        }

        // workspace.save();
        workspace.close();
    }

    // 创建数据集(含要素集合、要素)
    @Test
    public void createDataset2_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园-new.udbx").toString();

        // 创建数据源连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());

        // 打开工作空间
        Workspace workspace = new Workspace();

        // 创建数据源
        Datasource datasource = workspace.getDatasources().create(datasourceConnectionInfo);

        if (datasource != null) {
            Datasets datasets = datasource.getDatasets();

            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
            datasetVectorInfo.setName("上海润和总部园");  // 数据集名称
            datasetVectorInfo.setType(DatasetType.REGION); // 面数据集
            datasetVectorInfo.setEncodeType(EncodeType.NONE);

            DatasetVector datasetVector = datasets.create(datasetVectorInfo);
            // datasetVector.append();

            FieldInfos fieldInfos = datasetVector.getFieldInfos();
            FieldInfo info = fieldInfos.get("name");
            if (info == null){
                // 添加字段
                FieldInfo fieldInfo = new FieldInfo("name", FieldType.TEXT);
                fieldInfo.setMaxLength(50);
                fieldInfos.add(fieldInfo);
            }

            Recordset recordset = datasetVector.getRecordset(true, CursorType.DYNAMIC);

            Geometry geometry = GeometryUtil.geoJsonToGeometry("{\"type\":\"Polygon\",\"coordinates\":[[[121.60178065890112,31.201435346105855],[121.60463063887497,31.201499573958207],[121.60739550965144,31.20090193215891],[121.6098711080249,31.19968655226468],[121.61187464745537,31.197943178888044],[121.61325822051758,31.19580053832107],[121.6139197177245,31.193416828415093],[121.61423004285494,31.190700570883825],[121.61428027826813,31.18981801902345],[121.61388329339984,31.187361388107207],[121.61272302314767,31.185090591009037],[121.6108872465415,31.183177324480448],[121.60851479459133,31.181766242942609],[121.60578504864724,31.180964027252107],[121.6040135203018,31.180668806942195],[121.60093318749306,31.18055223787221],[121.5979464554174,31.181210566582345],[121.5973482664349,31.181427062780889],[121.59482690467658,31.18272323810197],[121.59282234657958,31.184569607575395],[121.59149178784684,31.18682141121033],[121.59054820339604,31.189193128713425],[121.59012198485152,31.190651926451474],[121.5899835878419,31.1921110756699],[121.59030499838652,31.194327392019266],[121.59125011463819,31.196407855618867],[121.59276101326953,31.198224877150414],[121.59474505832013,31.199667016885678],[121.59708057830298,31.200645822577479],[121.59877772096118,31.201012131021476],[121.60178065890112,31.201435346105855]]]}");

            // 添加 Geometry
            recordset.addNew(geometry);
            recordset.update();

            // 添加字段属性值，注意：必需先存在 Geometry 才能设置成功字段的属性值
            recordset.edit();
            recordset.setFieldValue("name", "张江润和总部园");
            recordset.update();
        }

        workspace.close();
    }

    // 复制数据集
    @Test
    public void copyDataset_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());
        Workspace workspace = new Workspace();
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        if (datasource != null) {
            DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            if (datasetVector != null) {

                String datasetVectorName = "上海润和总部园_copy";

                // 复制数据集
                DatasetVector datasetVectorCopy = (DatasetVector) datasource.copyDataset(datasetVector, datasetVectorName, EncodeType.NONE);

                System.out.println(datasetVectorCopy.getName());
                System.out.println(datasetVectorCopy.getType());
                System.out.println(datasetVectorCopy.getPrjCoordSys().getName());
                System.out.println(datasetVectorCopy.getPrjCoordSys().getType());
            }
        }

        workspace.close();
    }

    @Test
    public void renameDataset_test() {
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());
        Workspace workspace = new Workspace();
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        if (datasource != null) {
            // 重命名数据集名称
            String oldName = "上海润和总部园";
            String newName = "新数据集名称";
            renameDataset(datasource, oldName, newName);
        }

        workspace.close();
    }

    /**
     * 重命名数据集
     * Rename dataset.
     */
    public void renameDataset(Datasource datasource, String oldName, String newName) {
        if (!datasource.getDatasets().isAvailableDatasetName(newName)) {
            throw new RuntimeException("不可用的数据集名称：" + newName);
        }
        try {
            datasource.getDatasets().rename(oldName, newName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // 修改数据集（数据集内部数据）
    @Test
    public void modifyDataset_test() {
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());
        Workspace workspace = new Workspace();
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);

        if (datasource != null) {
            DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园_copy");
            if (datasetVector != null) {
                // 得到数据集的记录集
                // Get the recordset of the dataset.
                Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);

                // 更新 字段属性值
                int fieldCount = recordset.getFieldCount();
                System.out.println("fieldCount=" + fieldCount);
                updateFieldValue(recordset, 0, "name", "张江润和总部园");

                // 更新 几何点对象的空间属性
                Geometry geometry = GeometryUtil.geoJsonToGeometry("{\"type\":\"Polygon\",\"coordinates\":[[[121.60178065890112,31.201435346105855],[121.60463063887497,31.201499573958207],[121.60739550965144,31.20090193215891],[121.6098711080249,31.19968655226468],[121.61187464745537,31.197943178888044],[121.61325822051758,31.19580053832107],[121.6139197177245,31.193416828415093],[121.61423004285494,31.190700570883825],[121.61428027826813,31.18981801902345],[121.61388329339984,31.187361388107207],[121.61272302314767,31.185090591009037],[121.6108872465415,31.183177324480448],[121.60851479459133,31.181766242942609],[121.60578504864724,31.180964027252107],[121.6040135203018,31.180668806942195],[121.60093318749306,31.18055223787221],[121.5979464554174,31.181210566582345],[121.5973482664349,31.181427062780889],[121.59482690467658,31.18272323810197],[121.59282234657958,31.184569607575395],[121.59149178784684,31.18682141121033],[121.59054820339604,31.189193128713425],[121.59012198485152,31.190651926451474],[121.5899835878419,31.1921110756699],[121.59030499838652,31.194327392019266],[121.59125011463819,31.196407855618867],[121.59276101326953,31.198224877150414],[121.59474505832013,31.199667016885678],[121.59708057830298,31.200645822577479],[121.59877772096118,31.201012131021476],[121.60178065890112,31.201435346105855]]]}");
                updateGeometry(recordset, 0, geometry);
                // updateGeometry(recordset, 0, new GeoPoint(100, 100));
            }
        }

        workspace.close();
    }

    /**
     * 修改ID为id的几何点对象的空间属性
     * @param recordset 目标记录集
     * @param id 目标点几何对象的ID
     * Modify the specified point object's attribute.
     * @param recordset Target recordset.
     * @param id Target ID of the Geometry object.
     */
    private static void updateGeometry(Recordset recordset, int id, Geometry geometry) {
        recordset.seekID(id);
        // GeoPoint geoPoint = new GeoPoint(100, 100);
        recordset.edit();
        // recordset.setGeometry(geoPoint);
        recordset.setGeometry(geometry); // 修改几何点对象
        // recordset.addNew(geometry); // 添加几何点对象
        recordset.update();
    }

    /**
     * 修改记录的全部属性字段值
     * @param recordset 目标记录集
     * Modify all the attribute field.
     * @param recordset Target recordset
     */
    private static void updateFieldValue(Recordset recordset) {
        int length = recordset.getRecordCount();
        if (length != 0) {
            for (int i = 1; i <= length; i++) {
                updateFieldValue(recordset, i, "name", "超图");
            }
            System.out.println("全部记录已修改完成.");
        } else {
            System.out.println("记录不存在");
        }
    }

    /**
     * 修改ID为数组中元素的记录的属性字段值
     * @param recordset 目标记录集
     * @param id 目标记录的SmID
     */
    private static void updateFieldValue(Recordset recordset, int id, String fieldName, String fieldValue) {
        recordset.seekID(id);
        recordset.edit();
        // recordset.setFieldValue("name", "超图");
        recordset.setFieldValue(fieldName, fieldValue);
        recordset.update();
    }

    // 删除数据集
    @Test
    public void deleteDataset_test() {
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(dataFile);
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        datasourceConnectionInfo.setAlias(UUID.randomUUID().toString());
        Workspace workspace = new Workspace();
        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);
        if (datasource != null) {
            Datasets datasets = datasource.getDatasets();
            if (datasets != null && datasets.getCount() > 0) {
                datasource.getDatasets().delete("outPutDataset"); // 删除指定数据集
                // datasource.getDatasets().deleteAll(); // 删除全部数据集
            }
            datasource.close();
        }
        workspace.close();
    }

}
