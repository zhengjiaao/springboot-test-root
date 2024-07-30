package com.zja.gis.supermap.data;

import com.supermap.data.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * 工作空间 Workspace
 * @Author: zhengja
 * @Date: 2024-07-23 9:41
 */
@SpringBootTest
public class WorkspaceTest {

    static final String UDBX_PATH = "D:\\temp\\udbx";

    /**
     * 打开工作空间
     */
    @Test
    public void open_UDBX_test() {
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

            // 全部数据集
            Datasets datasets = datasource.getDatasets();

            for (int i = 0; i < datasets.getCount(); i++) {
                System.out.println("----------------------------------");
                Dataset dataset = datasets.get(i);
                System.out.println(dataset.getName());
            }

            // 获取指定数据集
            DatasetVector datasetVector = (DatasetVector) datasource.getDatasets().get("上海润和总部园");
            Map<Integer, Feature> features = datasetVector.getAllFeatures();
            features.forEach((key, feature) -> {
                System.out.println(key + "=" + feature);
                System.out.println("name=" + feature.getString("name")); // 获取属性
            });
        }
    }

    /**
     * 打开工作空间
     */
    @Test
    @Deprecated // todo 无法打开数据源
    public void openWorkspace_SMWU_test() {

        // 打开工作空间
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
        info.setServer("D:\\temp\\udbx\\空间分析.smwu");
        info.setType(WorkspaceType.SMWU);
        boolean flag = workspace.open(info);
        System.out.println("打开工作空间：" + flag);

        // 获取全部数据源
        Datasources datasources = workspace.getDatasources();
        System.out.println("返回数据源个数为：" + datasources.getCount());

        // 打开数据库数据源 todo 无法打开数据源
        Datasource targetDatasource = datasources.get(0);
        // Datasource targetDatasource = workspace.getDatasources().get("上海润和总部园");
        // targetDatasource.open(targetDatasource.getConnectionInfo()); // 出现被占用情况

        // 打开数据库数据源 todo 无法打开数据源
        // Datasource datasource = workspace.getDatasources().open("上海润和总部园");

        // 打开数据库数据源 todo 无法打开数据源 ，貌似：EngineType: 2054 不对
        Workspace workspace2 = new Workspace();
        Datasource datasource = workspace2.getDatasources().open(targetDatasource.getConnectionInfo());

        System.out.println("返回数据源别名为：" + datasource.getAlias() + "  数据源打开的状态:" + datasource.isOpened() + "  数据源是否以只读方式打开:" + datasource.isReadOnly());// 打印查询其描述信息

        // 获取数据集
        DatasetVector DatasetVectorIntersected = (DatasetVector) datasource.getDatasets().get("NewRegion_1");
        DatasetVector DatasetVectorIntersect = (DatasetVector) datasource.getDatasets().get("NewRegion_2");
        System.out.println("打开被相交数据集名称为：" + DatasetVectorIntersected.getName() + "数据集类型为：" + DatasetVectorIntersected.getType());// 打印查询其描述信息
        System.out.println("打开相交数据集名称为：" + DatasetVectorIntersect.getName() + "数据集类型为：" + DatasetVectorIntersect.getType());// 打印查询其描述信息
        System.out.println("获取数据集完成");// 打印数据集名称
    }


}
