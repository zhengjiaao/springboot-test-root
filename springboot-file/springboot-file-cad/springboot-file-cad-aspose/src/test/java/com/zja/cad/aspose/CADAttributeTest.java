package com.zja.cad.aspose;

import com.aspose.cad.IDrawingEntity;
import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadobjects.*;
import com.aspose.cad.fileformats.cad.cadobjects.hatch.CadHatch;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:50
 */
@Deprecated
public class CADAttributeTest {

    // String dwg_cad_path = Paths.get("D:\\temp\\cad", "Line.dwg").toString();
    String dwg_cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();
    // String dwg_cad_path = Paths.get("target", "simple_dwg_Line.dwg").toString();

    // 读取并输出 CAD 文件中所有实体的全部属性
    @Test
    public void testGetAllAttributesFromCadFile() {
        // 读取 CAD 文件
        CadImage cadImage = (CadImage) Image.load(dwg_cad_path);

        // 获取所有实体
        List<CadEntityBase> entities = cadImage.getEntities();

        // 遍历所有实体并输出其属性
        for (CadEntityBase entity : entities) {
            System.out.println("Entity Type: " + entity.getClass().getSimpleName());

            System.out.println("  Entity Id: " + entity.getId());
            System.out.println("  Entity UID: " + entity.getUID());
            System.out.println("  Layer Name: " + entity.getLayerName());
            System.out.println("  Visible: " + entity.getVisible());
            System.out.println("  Color ID: " + entity.getColorId());
            System.out.println("  Color Name: " + entity.getColorName());
            System.out.println("  Color Value: " + entity.getColorValue());
            System.out.println("  Color Handle: " + entity.getColorHandle());
            System.out.println("  AssocViewPortHandle: " + entity.getAssocViewPortHandle());
            System.out.println("  Hyperlink: " + entity.getHyperlink());
            System.out.println("  PlotStyle: " + entity.getPlotStyle());
            System.out.println("  Material: " + entity.getMaterial());
            System.out.println("  Line TypeName: " + entity.getLineTypeName());
            System.out.println("  Line Weight: " + entity.getLineWeight());
            System.out.println("  Line Scale: " + entity.getLineScale());
            System.out.println("  ProxyBytesCount: " + entity.getProxyBytesCount());
            System.out.println("  StorageFlag: " + entity.getStorageFlag());
            System.out.println("  Transparency: " + entity.getTransparency());
            System.out.println("  XDirMissingFlag: " + entity.getXDirMissingFlag());

            List<CadObjectAttribute> attributes = entity.getAttributes();
            for (CadObjectAttribute attribute : attributes) {
                System.out.println("  Attribute: " + attribute);
                System.out.println("  Attribute Name: " + attribute.getName());
            }

            /*List<Cad3DPoint> bounds = entity.getBounds();
            for (Cad3DPoint bound : bounds) {
                System.out.println("  Bound: " + bound.getX() + ", " + bound.getY() + ", " + bound.getZ());
            }*/

            List<CadEntityBase> childObjects = entity.getChildObjects();
            for (CadEntityBase childObject : childObjects) {
                System.out.println("  Child Object: " + childObject);
            }

            List<IDrawingEntity> childs = entity.getChilds();
            for (IDrawingEntity child : childs) {
                System.out.println("  Child: " + child);
            }

            if (entity instanceof CadMText) { // 文本
                CadMText cadMText = (CadMText) entity;
                System.out.println("CadMText: " + entity.getClass().getSimpleName());
                System.out.println("  Text: " + cadMText.getText());
                System.out.println("  FullText: " + cadMText.getFullText());
                System.out.println("  FullClearText: " + cadMText.getFullClearText());
                System.out.println("  TextStyleName: " + cadMText.getTextStyleName());
                System.out.println("  ColumnType: " + cadMText.getColumnType());
                System.out.println("  ColumnCount: " + cadMText.getColumnCount());
                System.out.println("  ColumnFlow: " + cadMText.getColumnFlow());
                System.out.println("  ColumnGutter: " + cadMText.getColumnGutter());
                System.out.println("  ColumnWidth: " + cadMText.getColumnWidth());
                System.out.println("  ColumnAutoHeight: " + cadMText.getColumnAutoHeight());
                System.out.println("  AttachmentPoint: " + cadMText.getAttachmentPoint());
                System.out.println("  AdditionalText: " + cadMText.getAdditionalText());
                System.out.println("  RotationAngleRad: " + cadMText.getRotationAngleRad());

            } else if (entity instanceof CadLine) { // 直线
                CadLine line = (CadLine) entity;
                System.out.println("CadLine: " + entity.getClass().getSimpleName());
                System.out.println("  First Point: (" + line.getFirstPoint().getX() + ", " + line.getFirstPoint().getY() + ")"); // 起点(X、Y、Z)
                System.out.println("  Second Point: (" + line.getSecondPoint().getX() + ", " + line.getSecondPoint().getY() + ")"); // 终点(X、Y、Z)
                System.out.println("  Thickness: " + line.getThickness()); // 线宽
                System.out.println("  Line Scale: " + line.getLineScale()); // 线宽缩放

                System.out.println("  Layer Name: " + line.getLayerName());
                System.out.println("  Line Type Name: " + line.getLineTypeName());
                System.out.println("  Color ID: " + line.getColorId());
                System.out.println("  Line Weight: " + line.getLineWeight()); // 线宽

            } else if (entity instanceof CadMultiLine) { // 多段线
                CadMultiLine cadMultiLine = (CadMultiLine) entity;
                System.out.println("CadMultiLine: " + cadMultiLine.getLayerName());

            } else if (entity instanceof CadPolyline) { // 多边线
                CadPolyline polyline = (CadPolyline) entity;
                System.out.println("CadPolyline: " + polyline.getLayerName());
            } else if (entity instanceof CadLwPolyline) { //  lightweight polyline
                CadLwPolyline cadLwPolyline = (CadLwPolyline) entity;
                System.out.println("CadLwPolyline: " + entity.getClass().getSimpleName());
                System.out.println("  ConstantWidth: " + cadLwPolyline.getConstantWidth());
                System.out.println("  Elevation: " + cadLwPolyline.getElevation());
                System.out.println("  ExtrusionDirection: " + cadLwPolyline.getExtrusionDirection());
                System.out.println("  Flag: " + cadLwPolyline.getFlag());
                System.out.println("  PointCount: " + cadLwPolyline.getPointCount());
                System.out.println("  StartWidth: " + cadLwPolyline.getStartWidth());
                System.out.println("  EndWidth: " + cadLwPolyline.getEndWidth());
            } else if (entity instanceof CadCircle) { // 圆
                CadCircle circle = (CadCircle) entity;
                System.out.println("CadCircle: " + circle.getLayerName());
                System.out.println("  Center Point: (" + circle.getCenterPoint().getX() + ", " + circle.getCenterPoint().getY() + ")");
                System.out.println("  Radius: " + circle.getRadius());
            } else if (entity instanceof CadText) { // 文本
                CadText text = (CadText) entity;
                System.out.println("CadText: " + text.getLayerName());
                System.out.println("  Text: " + text.getDefaultValue());
                System.out.println("  Insertion Point: (" + text.getFirstAlignment().getX() + ", " + text.getFirstAlignment().getY() + ")");
                System.out.println("  Text Height: " + text.getTextHeight());
                System.out.println("  Scale X: " + text.getScaleX());
                System.out.println("  Text Style Type: " + text.getStyleType());

                System.out.println("  Color ID: " + text.getColorId());
                System.out.println("  Layer Name: " + text.getLayerName());

            } else if (entity instanceof CadHatch) { // 草图
                CadHatch hatch = (CadHatch) entity;
                System.out.println("CadHatch: " + entity.getClass().getSimpleName());
                System.out.println("  Hatch Pattern: " + hatch.getPatternName());
                System.out.println("  Hatch Pattern Type: " + hatch.getHatchPatternType());
                System.out.println("  Hatch String: " + hatch.getHatchString());
                System.out.println("  Hatch Angle: " + hatch.getHatchAngle());
                System.out.println("  Hatch Style: " + hatch.getHatchStyle());
                System.out.println("  Hatch PatternDoubleFlag: " + hatch.getHatchPatternDoubleFlag());
                System.out.println("  Hatch ScaleOrSpacing: " + hatch.getHatchScaleOrSpacing());
                System.out.println("  GradientName: " + hatch.getGradientName());

            } else if (entity instanceof CadLeader) { // 引线
                CadLeader leader = (CadLeader) entity;
                System.out.println("CadLeader: " + entity.getClass().getSimpleName());
                System.out.println("  Leader Type: " + leader.getLayerName());
            } else {
                System.out.println("  Unknown Entity Type: " + entity.getClass().getSimpleName());
            }

            System.out.println("------------------------------------------");
        }

        // 确保至少有一个实体被处理
        assertTrue(entities.size() > 0);
    }

    // 读取并验证 CAD 文件中的特定属性值
    @Test
    public void testSpecificAttributeValueInCadFile() throws IOException {
        // 读取 CAD 文件
        // String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(dwg_cad_path);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);
        System.out.println(line);

        // 获取线两个端点的坐标
        Cad3DPoint firstPoint = line.getFirstPoint();
        System.out.println("First Point: " + firstPoint.getX() + ", " + firstPoint.getY());
        Cad3DPoint secondPoint = line.getSecondPoint();
        System.out.println("Second Point: " + secondPoint.getX() + ", " + secondPoint.getY());

        if (firstPoint != null && secondPoint != null) {
            double distance = firstPoint.distance(secondPoint);
            System.out.println("Distance: " + distance);
        }

        // 设置线的厚度
        // line.setThickness(0.5);

        // 验证特定属性值
        double expectedThickness = 0.5;
        double actualThickness = line.getThickness();
        System.out.println("Actual Thickness: " + actualThickness);

        assert actualThickness == expectedThickness;
    }

    // 修改 CAD 文件中的实体属性
    @Test
    @Deprecated //  未验证
    public void testModifyEntityAttributeInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);

        // 修改线的厚度
        double newThickness = 1.0;
        line.setThickness(newThickness);

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/modified_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证属性是否修改成功
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        CadLine modifiedLine = (CadLine) modifiedCadImage.getEntities().get(0);
        double actualThickness = modifiedLine.getThickness();

        assert actualThickness == newThickness;
    }

    // 查询 CAD 文件中的特定属性
   /* @Test
    public void testQuerySpecificPropertyInCadFile() throws IOException {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取第一条线
        CadLine line = (CadLine) cadImage.getEntities().get(0);

        // 查询线的长度
        double length = Math.sqrt(Math.pow(line.getEndX() - line.getStartX(), 2) + Math.pow(line.getEndY() - line.getStartY(), 2));

        // 验证长度
        double expectedLength = 100.0; // 假设线的长度为100.0
        assert Math.abs(length - expectedLength) < 0.001;
    }*/
}
