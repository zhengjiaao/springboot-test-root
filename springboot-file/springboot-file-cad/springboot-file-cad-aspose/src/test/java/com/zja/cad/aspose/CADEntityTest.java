package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.CadLayersList;
import com.aspose.cad.fileformats.cad.cadobjects.*;
import com.aspose.cad.fileformats.cad.cadobjects.hatch.CadHatch;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline3D;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:49
 */
public class CADEntityTest {

    String dxf_cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dxf").toString();
    String dwg_cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();

    @Test
    public void test_1() {
        try (CadImage cadImage = (CadImage) Image.load(dwg_cad_path)) {
            CadLayersList layersList = cadImage.getLayers();
            for (String layersName : layersList.getLayersNames()) {
                // 这部分代码输出的是 CAD 文件中所有图层的名称
                // 列出 CAD 文件中所有的图层，每个图层名称只出现一次
                System.out.println("layersName: " + layersName);
            }

            System.out.println("--------------------------------------------");

            List<CadEntityBase> entities = cadImage.getEntities();
            for (CadEntityBase entity : entities) {
                // 这部分代码输出的是 CAD 文件中每个实体所属的图层名称。
                // 列出 CAD 文件中每个实体及其所属的图层名称。同一个图层名称可能会多次出现，因为多个实体可能属于同一个图层。
                System.out.println("LayerName: " + entity.getLayerName());
            }
        }
    }

    // 读取并验证 CAD 文件中的所有实体
    @Test
    public void testAllEntitiesInCadFile() {
        // 读取 CAD 文件
        try (CadImage cadImage = (CadImage) Image.load(dwg_cad_path)) {
            // 获取所有实体
            List<CadEntityBase> entities = cadImage.getEntities();
            // 实体数量
            int actualEntityCount = entities.size();
            System.out.println("Actual Entity Count: " + actualEntityCount);

            for (CadEntityBase entity : entities) {
                System.out.println("----------------------------------------");
                System.out.println("Entity : " + entity);
                System.out.println("Entity Id: " + entity.getId());
                System.out.println("Entity LayerName: " + entity.getLayerName());
                System.out.println("Entity LineTypeName: " + entity.getLineTypeName());
                System.out.println("Entity TypeName: " + entity.getTypeName().name());
                System.out.println("Entity LayoutTabName: " + entity.getLayoutTabName());
                System.out.println("Entity ColorName: " + entity.getColorName());
                System.out.println("Entity ColorHandle: " + entity.getColorHandle());
                System.out.println("Entity LineWeight: " + entity.getLineWeight());
                System.out.println("Entity Visible: " + entity.getVisible());
                System.out.println("Entity Attributes size: " + entity.getAttributes().size());
            }
        }
    }

    // 读取并验证 CAD 文件中的特定图层上的实体
    @Test
    public void testEntitiesOnSpecificLayerInCadFile() {
        // 读取 CAD 文件
        // String filePath = "input/test_cad.dxf";
        List<CadLine> lines;
        try (CadImage cadImage = (CadImage) Image.load(dwg_cad_path)) {

            // 获取特定图层上的所有线
            String targetLayerName = "道路中心线";
            // lines = cadImage.getEntities().stream()
            //         .filter(entity -> entity instanceof CadLine && ((CadLine) entity).getLayerName().equals(targetLayerName))
            //         .map(entity -> (CadLine) entity).collect(Collectors.toList());

            lines = cadImage.getEntities().stream().filter(entity -> entity instanceof CadLine).map(entity -> (CadLine) entity).collect(Collectors.toList());

            // List<String> layersNames = cadImage.getLayers().getLayersNames();
            // CadLayerTable layer = cadImage.getLayers().getLayer(targetLayerName);

            for (CadEntityBase entity : cadImage.getEntities()) {
                if (entity instanceof CadMText) {
                    CadMText cadMText = (CadMText) entity;
                    System.out.println("CadMText: " + cadMText.getLayerName() + cadMText.getText());
                }
                if (entity instanceof CadLine) {
                    CadLine line = (CadLine) entity;
                    System.out.println("CadLine: " + line.getLayerName());
                }
                if (entity instanceof CadMultiLine) {
                    CadMultiLine cadMultiLine = (CadMultiLine) entity;
                    System.out.println("CadMultiLine: " + cadMultiLine.getLayerName());
                }
                if (entity instanceof CadPolyline) {
                    CadPolyline polyline = (CadPolyline) entity;
                    System.out.println("CadPolyline: " + polyline.getLayerName());
                }
                if (entity instanceof CadText) {
                    CadText cadText = (CadText) entity;
                    System.out.println("CadText: " + cadText.getLayerName());
                }
                if (entity instanceof CadArc) {
                    CadArc cadArc = (CadArc) entity;
                    System.out.println("CadArc: " + cadArc.getLayerName());
                }
                if (entity instanceof CadCircle) {
                    CadCircle cadCircle = (CadCircle) entity;
                    System.out.println("CadCircle: " + cadCircle.getLayerName());
                }
                if (entity instanceof CadEllipse) {
                    CadEllipse cadEllipse = (CadEllipse) entity;
                    System.out.println("CadEllipse: " + cadEllipse.getLayerName());
                }
                if (entity instanceof CadSpline) {
                    CadSpline cadSpline = (CadSpline) entity;
                    System.out.println("CadSpline: " + cadSpline.getLayerName());
                }
                if (entity instanceof CadPoint) {
                    CadPoint cadPoint = (CadPoint) entity;
                    System.out.println("CadPoint: " + cadPoint.getLayerName());
                }
                if (entity instanceof CadPolyline3D) {
                    CadPolyline3D cadPolyline3D = (CadPolyline3D) entity;
                    System.out.println("CadPolyline3D: " + cadPolyline3D.getLayerName());
                }
                if (entity instanceof CadSolid) {
                    CadSolid cadSolid = (CadSolid) entity;
                    System.out.println("CadSolid: " + cadSolid.getLayerName());
                }
                if (entity instanceof CadRegion) {
                    CadRegion cadRegion = (CadRegion) entity;
                    System.out.println("CadRegion: " + cadRegion.getLayerName());
                }
                if (entity instanceof CadSection) {
                    CadSection cadSection = (CadSection) entity;
                    System.out.println("CadSection: " + cadSection.getLayerName());
                }
                if (entity instanceof CadLwPolyline) {
                    CadLwPolyline cadLwPolyline = (CadLwPolyline) entity;
                    System.out.println("CadLwPolyline: " + cadLwPolyline.getLayerName());
                }
                if (entity instanceof CadHatch) {
                    CadHatch cadHatch = (CadHatch) entity;
                    System.out.println("CadHatch: " + cadHatch.getLayerName());
                }
            }
        }

        // 验证线的数量
        int expectedLineCount = 2; // 假设特定图层上有2条线
        int actualLineCount = lines.size();
        System.out.println("Actual Line Count: " + actualLineCount);

        // assert actualLineCount == expectedLineCount;
    }

    // 读取并验证 CAD 文件中的特定实体类型
    @Test
    public void testSpecificEntityTypeInCadFile() {
        // 读取 CAD 文件
        CadImage cadImage = (CadImage) Image.load(dxf_cad_path);

        // 获取所有线
        List<CadLine> lines = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadLine)
                .map(entity -> (CadLine) entity).collect(Collectors.toList());

        // 线的数量
        int actualLineCount = lines.size();
        System.out.println("Actual Line Count: " + actualLineCount);
    }

    // 读取并验证 CAD 文件中的文本实体
    @Test
    @Deprecated
    public void testTextEntitiesInCadFile() {
        // 读取 CAD 文件
        CadImage cadImage = (CadImage) Image.load(dwg_cad_path);

        // 获取所有文本实体
        List<CadText> textEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadText)
                .map(entity -> (CadText) entity).collect(Collectors.toList());

        // 验证文本实体数量
        int actualTextCount = textEntities.size();
        assert actualTextCount > 0;

        // 验证文本内容
        String[] expectedTextContents = {"Text1", "Text2"};
        for (int i = 0; i < textEntities.size(); i++) {
            System.out.println("Text Entity " + i + ": " + textEntities.get(i));
            System.out.println("Text Entity DefaultValue" + i + ": " + textEntities.get(i).getDefaultValue());
            // assert textEntities.get(i).getDefaultValue().equals(expectedTextContents[i]);
        }
    }

    // 读取并验证 CAD 文件中的圆弧实体
  /*  @Test
    public void testArcEntitiesInCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有圆弧实体
        List<CadArc> arcEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadArc)
                .map(entity -> (CadArc) entity)
                .toList();

        // 验证圆弧实体数量
        int expectedArcCount = 2; // 假设文件中有2个圆弧实体
        int actualArcCount = arcEntities.size();

        assert actualArcCount == expectedArcCount;

        // 验证圆弧属性
        double[] expectedCenterX = {50.0, 100.0};
        double[] expectedCenterY = {50.0, 100.0};
        double[] expectedRadius = {20.0, 30.0};

        for (int i = 0; i < arcEntities.size(); i++) {
            assert arcEntities.get(i).getCenterX() == expectedCenterX[i];
            assert arcEntities.get(i).getCenterY() == expectedCenterY[i];
            assert arcEntities.get(i).getRadius() == expectedRadius[i];
        }
    }*/

    // 读取并验证 CAD 文件中的多段线实体
  /*  @Test
    public void testPolylineEntitiesInCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有多段线实体
        List<CadLwPolyline> polylineEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadLwPolyline)
                .map(entity -> (CadLwPolyline) entity)
                .toList();

        // 验证多段线实体数量
        int expectedPolylineCount = 1; // 假设文件中有1个多段线实体
        int actualPolylineCount = polylineEntities.size();

        assert actualPolylineCount == expectedPolylineCount;

        // 验证多段线顶点数量
        int expectedVertexCount = 4; // 假设多段线有4个顶点
        int actualVertexCount = polylineEntities.get(0).getVertices().size();

        assert actualVertexCount == expectedVertexCount;
    }*/

    // 读取并验证 CAD 文件中的圆实体
   /* @Test
    public void testCircleEntitiesInCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有圆实体
        List<CadCircle> circleEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadCircle)
                .map(entity -> (CadCircle) entity)
                .toList();

        // 验证圆实体数量
        int expectedCircleCount = 2; // 假设文件中有2个圆实体
        int actualCircleCount = circleEntities.size();

        assert actualCircleCount == expectedCircleCount;

        // 验证圆属性
        double[] expectedCenterX = {50.0, 100.0};
        double[] expectedCenterY = {50.0, 100.0};
        double[] expectedRadius = {20.0, 30.0};

        for (int i = 0; i < circleEntities.size(); i++) {
            assert circleEntities.get(i).getCenterX() == expectedCenterX[i];
            assert circleEntities.get(i).getCenterY() == expectedCenterY[i];
            assert circleEntities.get(i).getRadius() == expectedRadius[i];
        }
    }*/


    // 读取并验证 CAD 文件中的椭圆实体
  /*  @Test
    public void testEllipseEntitiesInCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有椭圆实体
        List<CadEllipse> ellipseEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadEllipse)
                .map(entity -> (CadEllipse) entity)
                .toList();

        // 验证椭圆实体数量
        int expectedEllipseCount = 2; // 假设文件中有2个椭圆实体
        int actualEllipseCount = ellipseEntities.size();

        assert actualEllipseCount == expectedEllipseCount;

        // 验证椭圆属性
        double[] expectedCenterX = {50.0, 100.0};
        double[] expectedCenterY = {50.0, 100.0};
        double[] expectedMajorAxis = {30.0, 40.0};
        double[] expectedMinorAxis = {20.0, 30.0};

        for (int i = 0; i < ellipseEntities.size(); i++) {
            assert ellipseEntities.get(i).getCenterX() == expectedCenterX[i];
            assert ellipseEntities.get(i).getCenterY() == expectedCenterY[i];
            assert ellipseEntities.get(i).getMajorAxis() == expectedMajorAxis[i];
            assert ellipseEntities.get(i).getMinorAxis() == expectedMinorAxis[i];
        }
    }*/

    // 读取并验证 CAD 文件中的面域实体
   /* @Test
    public void testRegionEntitiesInCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有面域实体
        List<CadRegion> regionEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadRegion)
                .map(entity -> (CadRegion) entity)
                .toList();

        // 验证面域实体数量
        int expectedRegionCount = 1; // 假设文件中有1个面域实体
        int actualRegionCount = regionEntities.size();

        assert actualRegionCount == expectedRegionCount;

        // 验证面域顶点数量
        int expectedVertexCount = 4; // 假设面域有4个顶点
        int actualVertexCount = regionEntities.get(0).getVertices().size();

        assert actualVertexCount == expectedVertexCount;
    }*/

    // 生成新的 CAD 文件
  /*  @Test
    public void testGenerateNewCadFile()  {
        // 创建新的 CAD 图像
        CadImage cadImage = new CadImage();

        // 添加一条线
        CadLine line = new CadLine();
        line.setStartX(0.0);
        line.setStartY(0.0);
        line.setEndX(100.0);
        line.setEndY(100.0);
        line.setLayerName("Layer1");
        cadImage.getEntities().add(line);

        // 保存 CAD 文件
        String outputFilePath = "output/generated_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证实体
        CadImage generatedCadImage = (CadImage) Image.load(outputFilePath);
        int actualEntityCount = generatedCadImage.getEntities().size();

        assert actualEntityCount == 1;
        CadLine generatedLine = (CadLine) generatedCadImage.getEntities().get(0);
        assert generatedLine.getStartX() == 0.0;
        assert generatedLine.getStartY() == 0.0;
        assert generatedLine.getEndX() == 100.0;
        assert generatedLine.getEndY() == 100.0;
        assert generatedLine.getLayerName().equals("Layer1");
    }*/

    // 复制 CAD 实体
  /*  @Test
    public void testCopyCadEntity()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine originalLine = (CadLine) cadImage.getEntities().get(0);

        // 复制线
        CadLine copiedLine = (CadLine) originalLine.deepClone();
        copiedLine.setLayerName("CopiedLayer");

        // 添加复制的线
        cadImage.getEntities().add(copiedLine);

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/copied_entity_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证复制的线
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        int actualEntityCount = modifiedCadImage.getEntities().size();

        assert actualEntityCount == 2;
        CadLine modifiedLine = (CadLine) modifiedCadImage.getEntities().get(1);
        assert modifiedLine.getStartX() == originalLine.getStartX();
        assert modifiedLine.getStartY() == originalLine.getStartY();
        assert modifiedLine.getEndX() == originalLine.getEndX();
        assert modifiedLine.getEndY() == originalLine.getEndY();
        assert modifiedLine.getLayerName().equals("CopiedLayer");
    }*/

    // 移动 CAD 实体
   /* @Test
    public void testMoveCadEntity()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);

        // 移动线
        double deltaX = 50.0;
        double deltaY = 50.0;
        line.setStartX(line.getStartX() + deltaX);
        line.setStartY(line.getStartY() + deltaY);
        line.setEndX(line.getEndX() + deltaX);
        line.setEndY(line.getEndY() + deltaY);

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/moved_entity_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证移动后的线
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        CadLine modifiedLine = (CadLine) modifiedCadImage.getEntities().get(0);
        assert modifiedLine.getStartX() == line.getStartX();
        assert modifiedLine.getStartY() == line.getStartY();
        assert modifiedLine.getEndX() == line.getEndX();
        assert modifiedLine.getEndY() == line.getEndY();
    }

    // 设置 CAD 实体的可见性
    @Test
    public void testSetCadEntityVisibility()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);

        // 设置线的可见性
        line.setVisible(false);

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/set_visibility_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证可见性
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        CadLine modifiedLine = (CadLine) modifiedCadImage.getEntities().get(0);
        assert !modifiedLine.isVisible();
    }

    // 从 CAD 文件中删除特定实体
    @Test
    public void testDeleteCadEntity()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);

        // 删除线
        cadImage.getEntities().remove(line);

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/deleted_entity_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证实体已删除
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        int actualEntityCount = modifiedCadImage.getEntities().size();

        assert actualEntityCount == 0;
    }

    // 从 CAD 文件中提取特定实体
    @Test
    public void testExtractSpecificEntities()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 提取所有线实体
        List<CadLine> lineEntities = cadImage.getEntities().stream()
                .filter(entity -> entity instanceof CadLine)
                .map(entity -> (CadLine) entity)
                .toList();

        // 保存提取的实体到新文件
        String outputFilePath = "output/extracted_lines_test_cad.dxf";
        CadImage extractedCadImage = new CadImage();
        extractedCadImage.getEntities().addAll(lineEntities);
        extractedCadImage.save(outputFilePath);

        // 重新读取文件并验证提取的实体
        CadImage extractedImage = (CadImage) Image.load(outputFilePath);
        int actualEntityCount = extractedImage.getEntities().size();

        assert actualEntityCount == lineEntities.size();
    }*/
}
