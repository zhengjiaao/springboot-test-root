package com.zja.gis.supermap.data;

import com.supermap.data.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.UUID;

/**
 * 转换坐标系
 * @Author: zhengja
 * @Date: 2024-07-18 10:58
 */
@SpringBootTest
public class CoordinateSystemConvertTest {

    static final String UDBX_PATH = "D:\\temp\\udbx";

    @Test
    public void transform() {
        // 文件路径，支持多种格式
        String dataFile = Paths.get(UDBX_PATH, "上海润和总部园.udbx").toString();

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

        // 创建新的数据集
        String m_resultName = "outPutDataset";
        Dataset m_sourceDatasetCopy = (DatasetVector) sourceDataset.getDatasource().copyDataset(sourceDataset, m_resultName, EncodeType.NONE);

        // PrjCoordSys targetPrjCoordSys = PrjCoordSys.fromEPSG(4326); // 4326 单位：度
        PrjCoordSys targetPrjCoordSys = PrjCoordSys.fromEPSG(3857); // 3857 单位：米

        // 转换坐标系 CoordSysTranslator.convert()，支持 datasource、Dataset、Geometry、Point2Ds、inputFile等转换
        CoordSysTranslator.convert(m_sourceDatasetCopy, targetPrjCoordSys, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);

        datasource.close();
        workspace.close();
    }

}
