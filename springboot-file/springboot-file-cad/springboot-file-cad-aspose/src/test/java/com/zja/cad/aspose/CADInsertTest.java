package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:50
 */
@Deprecated
public class CADInsertTest {

    // 读取并验证 CAD 文件中的插入实体
   /* @Test
    public void testInsertEntitiesInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有插入实体
        List<CadInsert> insertEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadInsert)
                .map(entity -> (CadInsert) entity)
                .toList();

        // 验证插入实体数量
        int expectedInsertCount = 2; // 假设文件中有2个插入实体
        int actualInsertCount = insertEntities.size();

        assert actualInsertCount == expectedInsertCount;

        // 验证插入实体属性
        String[] expectedBlockNames = {"Block1", "Block2"};
        double[] expectedInsertX = {50.0, 100.0};
        double[] expectedInsertY = {50.0, 100.0};

        for (int i = 0; i < insertEntities.size(); i++) {
            assert insertEntities.get(i).getName().equals(expectedBlockNames[i]);
            assert insertEntities.get(i).getInsertionPointX() == expectedInsertX[i];
            assert insertEntities.get(i).getInsertionPointY() == expectedInsertY[i];
        }
    }*/
}
