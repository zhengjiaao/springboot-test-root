/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-20 15:35
 * @Since:
 */
package com.zja.shapefile.swing;

import com.zja.shapefile.util.ResourceUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;

import javax.swing.*;
import java.io.File;

/**
 * 在 Java Swing 应用程序中集成 Shapefile 文件的显示和交互
 * 在 Swing 界面中显示地图和图层的组件和工具
 *
 * @author: zhengja
 * @since: 2023/10/20 15:35
 */
public class ShapefileSwingExample {

    public static void main(String[] args) {
        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp"); // 替换为实际的Shapefile路径

        try {
            // 打开 Shapefile 数据源
            File shapefile = new File(shapefilePath);
            ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());

            // 获取第一个图层
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);

            Style style = SLD.createSimpleStyle(featureSource.getSchema()); // 拖显示
            Layer layer = new FeatureLayer(featureSource, style);  //遇到空白页面：图层渲染设置不正确,为图层设置一个简单的默认样式，以确保其可见性。

            // 创建地图内容
            MapContent mapContent = new MapContent();
            mapContent.addLayer(layer);

            System.out.println("Number of Layers: " + mapContent.layers().size()); //遇到空白页面：地图内容为空

            // 创建地图框架并显示地图
            JMapFrame mapFrame = new JMapFrame(mapContent);
            mapFrame.setSize(800, 600); // 遇到空白页面：显示区域设置不正确,调整地图框架的大小
            mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //mapContent.getViewport().setCoordinateReferenceSystem(crs); // 遇到空白页面：坐标参考系统不匹配
            mapFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
