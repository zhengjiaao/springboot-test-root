package com.zja.gis.supermap.analyst;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.UUID;

/**
 * 空间分析：叠加分析
 * @Author: zhengja
 * @Date: 2024-07-23 9:07
 */
@SpringBootTest
public class SpatialAnalystTest {

    static final String UDBX_PATH = "D:\\temp\\udbx";

    /**
     * 叠加分析: 相交叠加分析、另外还支持，如裁剪（clip）、擦除（erase）、合并（union）、相交（intersect）、同一（identity）、对称差（xOR）和更新（update）。
     */
    @Test
    public void OverlayAnalyst_Intersect_test() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "OverlayAnalyst.udbx").toString();

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
        DatasetVector DatasetVectorIntersected = (DatasetVector) datasource.getDatasets().get("NewRegion_1");
        DatasetVector DatasetVectorIntersect = (DatasetVector) datasource.getDatasets().get("NewRegion_2");
        System.out.println("打开被相交数据集名称为：" + DatasetVectorIntersected.getName() + "数据集类型为：" + DatasetVectorIntersected.getType());// 打印查询其描述信息
        System.out.println("打开相交数据集名称为：" + DatasetVectorIntersect.getName() + "数据集类型为：" + DatasetVectorIntersect.getType());// 打印查询其描述信息
        System.out.println("获取数据集完成");// 打印数据集名称

        // 创建结果数据集(在当前数据源 datasource 中)
        System.out.println("开始创建结果数据集");
        String resultDatasetIntersectName = datasource.getDatasets().getAvailableDatasetName("resultDatasetIntersect");//  创建一个面矢量数据集，用于存储相交分析返回的结果//文件数据源
        DatasetVectorInfo DatasetVectorInfoIntersect = new DatasetVectorInfo();
        DatasetVectorInfoIntersect.setType(DatasetType.REGION);
        DatasetVectorInfoIntersect.setName(resultDatasetIntersectName);
        DatasetVectorInfoIntersect.setEncodeType(EncodeType.NONE);
        DatasetVector resultDatasetIntersect = datasource.getDatasets().create(DatasetVectorInfoIntersect);// 文件数据源

        // 设置叠加分析参数
        System.out.println("开始设置叠加分析参数");
        OverlayAnalystParameter overlayAnalystParamIntersect = new OverlayAnalystParameter();
        overlayAnalystParamIntersect.setSourceRetainedFields(new String[]{"SmID", "name", "type"});// 设置进行叠加分析的第一数据集或记录集中需要保留的字段名称集合。
        overlayAnalystParamIntersect.setOperationRetainedFields(new String[]{"SmID", "SmArea", "name", "type"});// 设置进行叠加分析的第二数据集或记录集中需要保留的字段名称集合。
        overlayAnalystParamIntersect.setTolerance(0.00000001);
        // 调用相交叠加分析方法实相交分析
        boolean OverlayAnalystIntersect = OverlayAnalyst.intersect(DatasetVectorIntersected, DatasetVectorIntersect, resultDatasetIntersect, overlayAnalystParamIntersect);
        System.out.println("是否执行相交成功：" + OverlayAnalystIntersect);

        // 输出结果数据集字段信息
        System.out.println("结果数据集名称:" + resultDatasetIntersect.getName() + "  结果数据集中字段的数量" + resultDatasetIntersect.getFieldCount());
        FieldInfos fieldInfos = resultDatasetIntersect.getFieldInfos();
        String[] fields = new String[fieldInfos.getCount()];
        String[] fieldsj = new String[fieldInfos.getCount()];
        for (int i = 0; i < fieldInfos.getCount(); i++) {
            fields[i] = fieldInfos.get(i).getName();
            fieldsj[i] = fieldInfos.get(i).getCaption();
        }
        System.out.println("结果数据集字段名称为 = " + StringUtils.join(fields, ","));
        System.out.println("结果数据集字段别名为 = " + StringUtils.join(fieldsj, ","));

        // 复制结果数据集（默认，已经存放到数据源中了）
        // datasource.copyDataset(resultDatasetIntersect, resultDatasetIntersect.getName()+1, resultDatasetIntersect.getEncodeType());

        datasource.close();
        workspace.close();

        // 保存工作空间
        // workspace.save();
        // System.out.println("保存工作空间" + workspace.save());
    }
}
