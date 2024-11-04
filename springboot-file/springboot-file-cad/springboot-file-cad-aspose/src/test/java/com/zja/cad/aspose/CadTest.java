package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadobjects.Cad3DPoint;
import com.aspose.cad.fileformats.cad.cadobjects.CadEntityBase;
import com.aspose.cad.fileformats.cad.cadobjects.CadLine;
import com.aspose.cad.imageoptions.DxfOptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:07
 */
@Deprecated
public class CadTest {

    // String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dxf").toString();
    String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();


    // 生成 CAD 文件
  /*  @Test
    @Deprecated
    public void testCreateCadFile() {
        String outputPath = "target/new_cad_file.dxf";
        // 创建一个新的 CAD 图像
        try (CadImage cadImage = new CadImage()) {

            // 添加一条线
            Cad3DPoint startPoint = new Cad3DPoint(0, 0, 0);
            Cad3DPoint endPoint = new Cad3DPoint(100, 100, 0);

            CadLine line = new CadLine();
            line.setFirstPoint(startPoint);
            line.setSecondPoint(endPoint);
            line.setLayerName("0"); // 默认图层名称为 "0"
            cadImage.getEntities().add(line);

            // 保存 CAD 文件
            cadImage.save(outputPath, new DxfOptions());

            // 检查文件是否创建成功
            File file = new File(outputPath);
            assert file.exists();
        }

    }*/


    // 更新 CAD 文件
    /*@Test
    public void testUpdateCadFile() throws IOException {
        // 读取 CAD 文件
        CadImage cadImage = (CadImage) Image.load(cad_path);

        // 更新线条的位置
        CadLine line = (CadLine) cadImage.getBlocks().getObjects().get(0);
        line.setEndPointX(200);
        line.setEndPointY(200);

        // 保存更新后的 CAD 文件
        String outputFilePath = "output/updated_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 检查文件是否更新成功
        File file = new File(outputFilePath);
        assert file.exists();
    }*/

    // 添加多个对象到 CAD 文件
   /* @Test
    public void testAddMultipleObjectsToCadFile() throws IOException {
        // 创建一个新的 CAD 图像
        CadImage cadImage = (CadImage) Image.createImage(CadImage.class, null);

        // 添加多条线
        CadLine line1 = new CadLine();
        line1.setStartPointX(0);
        line1.setStartPointY(0);
        line1.setEndPointX(100);
        line1.setEndPointY(100);
        cadImage.getBlocks().getObjects().add(line1);

        CadLine line2 = new CadLine();
        line2.setStartPointX(50);
        line2.setStartPointY(50);
        line2.setEndPointX(150);
        line2.setEndPointY(150);
        cadImage.getBlocks().getObjects().add(line2);

        // 保存 CAD 文件
        String filePath = "output/multiple_objects_test_cad.dxf";
        cadImage.save(filePath);

        // 重新读取文件并验证对象数量
        CadImage loadedCadImage = (CadImage) Image.load(filePath);
        int actualObjectCount = loadedCadImage.getBlocks().getObjects().size();

        assert actualObjectCount == 2;
    }*/

    // 从 CAD 文件中删除对象
    /*@Test
    public void testDeleteObjectFromCadFile() throws IOException {
        // 读取 CAD 文件
        String inputFilePath = cad_path;
        String outputFilePath;
        try (CadImage cadImage = (CadImage) Image.load(inputFilePath)) {

            // 获取所有实体
            List<CadEntityBase> entities = cadImage.getEntities();
            System.out.println("实体数量：" + entities.size());
            // 删除第一条线
            if (!entities.isEmpty()) {
                entities.remove(0);
            }

            // 保存更新后的 CAD 文件
            outputFilePath = "target/deleted_test_cad.dxf";
            cadImage.save(outputFilePath, new DxfOptions());
        }

        // 重新读取文件并验证对象已删除
        int actualObjectCount;
        try (CadImage updatedCadImage = (CadImage) Image.load(outputFilePath)) {
            actualObjectCount = updatedCadImage.getEntities().size();

            System.out.println("实体数量：" + actualObjectCount);
        }
    }*/

}
