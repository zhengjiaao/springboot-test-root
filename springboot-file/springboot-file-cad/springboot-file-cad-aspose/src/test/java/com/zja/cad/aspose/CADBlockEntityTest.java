package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadobjects.CadLine;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:50
 */
@Deprecated
public class CADBlockEntityTest {

    // 读取并验证 CAD 文件中的所有块
    /*@Test
    public void testAllBlocksInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有块
        List<CadBlock> blocks = cadImage.getBlocks().getObjects();

        // 验证块数量
        int expectedBlockCount = 2; // 假设文件中有2个块
        int actualBlockCount = blocks.size();

        assert actualBlockCount == expectedBlockCount;

        // 验证块名称
        String[] expectedBlockNames = {"Block1", "Block2"};
        for (int i = 0; i < blocks.size(); i++) {
            assert blocks.get(i).getName().equals(expectedBlockNames[i]);
        }
    }

    // 读取并验证 CAD 文件中的特定块中的实体
    @Test
    public void testEntitiesInSpecificBlockInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取特定块
        String targetBlockName = "Block1";
        CadBlock block = cadImage.getBlocks().getObjects().stream()
                .filter(b -> b.getName().equals(targetBlockName))
                .findFirst()
                .orElse(null);

        // 验证块是否存在
        assert block != null;

        // 获取特定块中的所有线
        List<CadLine> lines = block.getEntities().stream()
                .filter(entity -> entity instanceof CadLine)
                .map(entity -> (CadLine) entity)
                .toList();

        // 验证线的数量
        int expectedLineCount = 2; // 假设特定块中有2条线
        int actualLineCount = lines.size();

        assert actualLineCount == expectedLineCount;
    }

    // 读取并验证 CAD 文件中的块实体
    @Test
    public void testBlockEntitiesInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有块实体
        List<CadBlock> blockEntities = cadImage.getBlocks().stream()
                .filter(entity -> entity instanceof CadBlock)
                .map(entity -> (CadBlock) entity)
                .toList();

        // 验证块实体数量
        int expectedBlockCount = 2; // 假设文件中有2个块实体
        int actualBlockCount = blockEntities.size();

        assert actualBlockCount == expectedBlockCount;

        // 验证块名称
        String[] expectedBlockNames = {"Block1", "Block2"};

        for (int i = 0; i < blockEntities.size(); i++) {
            assert blockEntities.get(i).getName().equals(expectedBlockNames[i]);
        }
    }*/
}
