package com.zja.gis.supermap.analyst;

import com.supermap.analyst.spatialanalyst.BufferAnalyst;
import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.UUID;

/**
 * 空间分析：缓冲区
 * @Author: zhengja
 * @Date: 2024-07-23 16:56
 */
@SpringBootTest
public class BufferAnalystTest {

    static final String UDBX_PATH = "D:\\temp\\udbx";

    @Test
    public void createBuffer_test() {
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

        // 获取数据集
        DatasetVector sourceDataset = (DatasetVector) datasource.getDatasets().get("上海润和总部园");

        // 创建新的数据集
        String resultName = "outPutDataset_Buffer_2";
        DatasetVector resultDataset = (DatasetVector) sourceDataset.getDatasource().copyDataset(sourceDataset, resultName, EncodeType.NONE);

        double distance = 1000.0; // 缓冲区距离，单位：米

        BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();
        bufferAnalystParameter.setLeftDistance(distance);  // 设置左侧距离
        bufferAnalystParameter.setRightDistance(distance); // 设置右侧距离
        // bufferAnalystParameter.setEndType(BufferEndType.ROUND); // 设置缓冲区端点类型: FLAT 平头缓冲、ROUND 圆头缓冲, 注：点、面数据(不需要)，线数据（需要）

        // 创建缓冲区
        BufferAnalyst.createBuffer(sourceDataset, resultDataset, bufferAnalystParameter, false, true); // true 保留属性

        datasource.close();
        workspace.close();
    }

    @Test
    public void createMultiBuffer_test() {
        // 创建多缓冲区
        // BufferAnalyst.createMultiBuffer();
    }

    @Test
    public void createLineOneSideMultiBuffer_test() {
        // 创建单面多缓冲区
        // BufferAnalyst.createLineOneSideMultiBuffer();
    }

}
