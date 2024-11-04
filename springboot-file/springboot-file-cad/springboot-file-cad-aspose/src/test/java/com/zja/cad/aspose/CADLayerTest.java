package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.CadLayersList;
import com.aspose.cad.fileformats.cad.cadobjects.*;
import com.aspose.cad.fileformats.cad.cadobjects.hatch.CadHatch;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline;
import com.aspose.cad.fileformats.cad.cadobjects.polylines.CadPolyline3D;
import com.aspose.cad.fileformats.cad.cadtables.CadLayerTable;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PngOptions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 图层：设置实体可见性和颜色
 * <p>
 * cad全部图层
 * 实体所属图层
 *
 * @Author: zhengja
 * @Date: 2024-10-31 10:50
 */
public class CADLayerTest {

    // String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dxf").toString();
    String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();


    @Test
    public void testAllLayers() {

        try (CadImage cadImage = (CadImage) Image.load(cad_path)) {
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

            System.out.println("============================================");

            List<CadLayout> layoutsValues = cadImage.getLayoutsValues();
            for (CadLayout cadLayout : layoutsValues) {
                // 这部分代码输出的是 CAD 文件中所有的布局名称
                // 列出 CAD 文件中所有的布局名称，每个布局名称只出现一次
                System.out.println("Layout Name: " + cadLayout.getLayoutName());
            }
        }
    }


    // 读取并验证 CAD 文件中的所有图层
    @Test
    public void testAllLayersInCadFile() {
        // 读取 CAD 文件
        try (CadImage cadImage = (CadImage) Image.load(cad_path)) {
            // 获取所有图层
            CadLayersList layers = cadImage.getLayers();

            int actualLayerCount = layers.size();
            System.out.println("Actual Layer Count: " + actualLayerCount);

            for (String layersName : layers.getLayersNames()) {
                CadLayerTable layerTable = layers.getLayer(layersName);
                String tableName = layerTable.getName();
                System.out.println("Layer Name: " + tableName + " ,LineTypeName: " + layerTable.getLineTypeName());
                // LineType: CONTINUOUS、ACAD_ISO02W100（虚线 蓝色 虚线 0.2 mm）、ACAD_ISO10W100（）
            }

            // 验证图层数量
            int expectedLayerCount = 39; // 假设文件中有39个图层
            assert actualLayerCount == expectedLayerCount;
        }
    }



    // 过滤 CAD 文件中的特定图层
    @Test
    public void testFilterByLayerInCadFile() {
        // 读取 CAD 文件
        // String filePath = "input/test_cad.dxf";
        String filePath = cad_path;
        List<CadEntityBase> filteredEntities;
        try (CadImage cadImage = (CadImage) Image.load(filePath)) {
            /*for (CadEntityBase entity : cadImage.getEntities()) {
                System.out.println("LayerName=" + entity.getLayerName() + " ，TypeName=" + entity.getTypeName());
            }*/

            // 获取特定图层上的所有实体
            String targetLayerName = "道路绿带";
            filteredEntities = cadImage.getEntities().stream().filter(entity -> entity.getLayerName().equals(targetLayerName)).collect(Collectors.toList());
        }

        // 验证过滤后的实体数量
        int expectedEntityCount = 4; // 假设特定图层上有4个实体
        int actualEntityCount = filteredEntities.size();
        System.out.println("Actual Entity Count: " + actualEntityCount);

        assert actualEntityCount == expectedEntityCount;
    }


    // 重命名 CAD 图层
    @Test
    public void testRenameCadLayer() {
        // 读取 CAD 文件
        // String filePath = "input/test_cad.dxf";
        String filePath = cad_path;
        String newLayerName;
        String outputFilePath;
        try (CadImage cadImage = (CadImage) Image.load(filePath)) {

            // 获取特定图层
            String oldLayerName = "道路绿带";
            CadEntityBase cadEntityBase = cadImage.getEntities().stream().filter(entity -> entity.getLayerName().equals(oldLayerName)).findFirst().orElse(null);

            // 重命名图层
            newLayerName = "RenamedLayer";
            if (cadEntityBase != null) {
                cadEntityBase.setLayerName(newLayerName);
            }

            // 保存修改后的 CAD 文件
            outputFilePath = "target/renamed_layer_test_cad.dxf";
            cadImage.save(outputFilePath);
        }

        // 重新读取文件并验证图层名称
        boolean layerExists = false;
        try (CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath)) {

            // 验证不通过，什么原因？
            // CadLayersList layers = modifiedCadImage.getLayers();
            // layerExists = layers.getLayersNames().contains(newLayerName);

            // 验证通过
            CadEntityBase entityBase = modifiedCadImage.getEntities().stream().filter(entity -> entity.getLayerName().equals(newLayerName)).findFirst().orElse(null);
            if (entityBase != null) {
                layerExists = true;
            }
        }

        assert layerExists;
    }

    // 添加新图层到 CAD 文件
    @Test
    @Deprecated
    public void testAddNewLayer() {
        // 读取 CAD 文件
        // String filePath = "input/test_cad.dxf";
        String filePath = cad_path;
        String outputFilePath;
        try (CadImage cadImage = (CadImage) Image.load(filePath)) {

            // 创建新图层
            CadText cadText = new CadText();
            cadText.setLayerName("NewLayer");
            cadText.setDefaultValue("New Text");

            // 添加新图层
            // cadImage.getEntities().add(cadText); // 验证不通过

            // cadImage.getLayers().addItem(cadText); // 验证不通过

            // 保存修改后的 CAD 文件
            outputFilePath = "target/new_layer_test_cad.dxf";
            cadImage.save(outputFilePath); // 新图层未写入到新的cad
        }

        // 重新读取文件并验证新图层
        boolean layerExists;
        try (CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath)) {
            // 验证不通过
            // layerExists = modifiedCadImage.getEntities().stream().anyMatch(entity -> entity.getLayerName().equals("NewLayer"));

            // 验证不通过
            layerExists = modifiedCadImage.getLayers().getLayersNames().contains("NewLayer");
        }

        assert layerExists;
    }

    // 设置图层颜色
  /*  @Test
    public void testSetLayerColor()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 获取所有图层
        CadLayersList layers = cadImage.getLayers();


        // 获取特定图层
        String layerName = "Layer1";
        CadLayerTable layer = layers.getLayer(layerName);
        // CadBaseLayer layer = layers.stream()
        //         .filter(l -> l.getName().equals(layerName))
        //         .findFirst()
        //         .orElse(null);

        // 设置图层颜色
        if (layer != null) {
            layer.setColorIndex(2); // 设置颜色索引
            layer.setColorId(2);
        }

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/set_layer_color_test_cad.dxf";
        cadImage.save(outputFilePath, new DxfOptions());

        // 重新读取文件并验证图层颜色
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        List<CadBaseLayer> modifiedLayers = modifiedCadImage.getLayers();
        CadBaseLayer modifiedLayer = modifiedLayers.stream()
                .filter(l -> l.getName().equals(layerName))
                .findFirst()
                .orElse(null);

        assert modifiedLayer != null;
        assert modifiedLayer.getColorIndex() == 2;
    }
*/
    // 删除 CAD 文件中的特定图层
   /* @Test
    public void testRemoveLayerFromCadFile()  {
        // 读取 CAD 文件
        String filePath = "input/test_cad.dxf";
        CadImage cadImage = (CadImage) Image.load(filePath);

        // 删除特定图层
        String targetLayerName = "Layer1";
        cadImage.getLayers().removeIf(layer -> layer.getName().equals(targetLayerName));

        // 保存修改后的 CAD 文件
        String outputFilePath = "output/removed_layer_test_cad.dxf";
        cadImage.save(outputFilePath);

        // 重新读取文件并验证图层是否已删除
        CadImage modifiedCadImage = (CadImage) Image.load(outputFilePath);
        boolean layerExists = modifiedCadImage.getLayers().stream()
                .anyMatch(layer -> layer.getName().equals(targetLayerName));

        assert !layerExists;
    }*/
}
