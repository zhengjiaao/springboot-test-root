/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 16:12
 * @Since:
 */
package com.zja.proj4j;

import org.locationtech.proj4j.*;

/**
 * @author: zhengja
 * @since: 2023/11/07 16:12
 */
public class Proj4JExample {

    public static void main(String[] args) {
        // 定义源坐标系和目标坐标系的 EPSG 编码
        String sourceCrsCode = "EPSG:4326"; // WGS84 经纬度坐标系
        String targetCrsCode = "EPSG:3857"; // Web Mercator 投影坐标系

        // 创建坐标转换器
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem sourceCrs = crsFactory.createFromName(sourceCrsCode);
        CoordinateReferenceSystem targetCrs = crsFactory.createFromName(targetCrsCode);
        CoordinateTransformFactory transformFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = transformFactory.createTransform(sourceCrs, targetCrs);

        // 定义源坐标
        double sourceX = -122.419416;
        double sourceY = 37.774929;

        // 进行坐标转换
        ProjCoordinate sourceCoord = new ProjCoordinate(sourceX, sourceY);
        ProjCoordinate targetCoord = new ProjCoordinate();
        transform.transform(sourceCoord, targetCoord);

        // 输出目标坐标
        double targetX = targetCoord.x;
        double targetY = targetCoord.y;
        System.out.println("Target Coordinate: " + targetX + ", " + targetY);
        //输出结果：Target Coordinate: -1.3627667052329928E7, 4547679.438563918
    }
}
