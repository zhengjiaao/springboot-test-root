/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-19 13:49
 * @Since:
 */
package com.zja.shapefile.other;

import com.zja.shapefile.util.ResourceUtil;

import java.io.File;

/**
 * 如何确定Shapefile文件中的主要图层是哪个？
 * <p>
 * 例如，如果Shapefile文件的基本名称为my_shapefile，则主要图层的.shp文件名称为my_shapefile.shp。
 *
 * @author: zhengja
 * @since: 2023/10/19 13:49
 */
public class ShapefileMainLayerFinder {
    public static void main(String[] args) {
        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp");

        File shapefile = new File(shapefilePath);
        String shapefileBaseName = shapefile.getName().replaceFirst("[.][^.]+$", "");

        File[] filesInDirectory = shapefile.getParentFile().listFiles();
        if (filesInDirectory != null) {
            for (File file : filesInDirectory) {
                if (file.isFile() && file.getName().startsWith(shapefileBaseName) && file.getName().endsWith(".shp")) {
                    System.out.println("Main layer: " + file.getName());
                    break;
                }
            }
        }
    }
}
