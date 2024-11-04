package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadobjects.CadLine;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 11:01
 */
@Deprecated
public class CadPropertyTest {

    // 查询 CAD 文件中的特定属性
    @Test
    public void testQuerySpecificPropertyInCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);

        // 查询线的长度
        // double length = Math.sqrt(Math.pow(line.getEndX() - line.getStartX(), 2) + Math.pow(line.getEndY() - line.getStartY(), 2));

        // 验证长度
        double expectedLength = 100.0; // 假设线的长度为100.0
        // assert Math.abs(length - expectedLength) < 0.001;
    }

    // 读取并验证 CAD 文件中的特定对象属性
    @Test
    public void testObjectPropertyInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        // CadLine line = (CadLine) cadImage.getBlocks().getObjects().get(0);

        // 验证线的属性
        double expectedStartX = 0;
        double expectedStartY = 0;
        double expectedEndX = 100;
        double expectedEndY = 100;

        // assert line.getStartPointX() == expectedStartX;
        // assert line.getStartPointY() == expectedStartY;
        // assert line.getEndPointX() == expectedEndX;
        // assert line.getEndPointY() == expectedEndY;
    }
}
