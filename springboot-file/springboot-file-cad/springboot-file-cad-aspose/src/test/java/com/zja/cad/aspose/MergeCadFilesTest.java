package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:54
 */
public class MergeCadFilesTest {

    // 合并两个 CAD 文件
    @Test
    public void testMergeCadFiles() throws IOException {
        // 读取第一个 CAD 文件
        String filePath1 = "input/test_cad1.dxf";
        CadImage cadImage1 = (CadImage) Image.load(filePath1);

        // 读取第二个 CAD 文件
        String filePath2 = "input/test_cad2.dxf";
        CadImage cadImage2 = (CadImage) Image.load(filePath2);

        // 将第二个文件的实体添加到第一个文件
        cadImage1.getEntities().addAll(cadImage2.getEntities());

        // 保存合并后的 CAD 文件
        String outputFilePath = "output/merged_test_cad.dxf";
        cadImage1.save(outputFilePath);

        // 重新读取文件并验证实体数量
        CadImage mergedCadImage = (CadImage) Image.load(outputFilePath);
        int expectedEntityCount = cadImage1.getEntities().size();
        int actualEntityCount = mergedCadImage.getEntities().size();

        assert actualEntityCount == expectedEntityCount;
    }

    // 合并多个 CAD 文件
    @Test
    public void testMergeCadFiles2() throws IOException {
        // 读取第一个 CAD 文件
        String filePath1 = "input/test_cad1.dxf";
        CadImage cadImage1 = (CadImage) Image.load(filePath1);

        // 读取第二个 CAD 文件
        String filePath2 = "input/test_cad2.dxf";
        CadImage cadImage2 = (CadImage) Image.load(filePath2);

        // 合并实体
        cadImage1.getEntities().addAll(cadImage2.getEntities());

        // 保存合并后的 CAD 文件
        String outputFilePath = "output/merged_test_cad.dxf";
        cadImage1.save(outputFilePath);

        // 重新读取文件并验证实体数量
        CadImage mergedCadImage = (CadImage) Image.load(outputFilePath);
        int actualEntityCount = mergedCadImage.getEntities().size();

        assert actualEntityCount == cadImage1.getEntities().size();
    }

}
