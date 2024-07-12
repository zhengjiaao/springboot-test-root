/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-20 15:28
 * @Since:
 */
package com.zja.shapefile.Example;

import com.zja.shapefile.util.ResourceUtil;
import org.geotools.data.shapefile.ShapefileDataStore;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 查看Shapefile文件中有多少个图层
 *
 * @author: zhengja
 * @since: 2023/10/20 15:28
 */
public class ShapefileLayerCountExample {
    public static void main(String[] args) {
        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp");
        try {
            // 打开 Shapefile 数据源
            File shapefile = new File(shapefilePath);
            ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());

            // 获取图层名称列表
            List<String> layerNames = Arrays.asList(dataStore.getTypeNames());

            System.out.println("Number of Layers: " + layerNames.size());
            System.out.println("Layer Names: " + layerNames);

            // 关闭数据源
            dataStore.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
