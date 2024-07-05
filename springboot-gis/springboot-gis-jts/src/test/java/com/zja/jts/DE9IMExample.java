package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * 九交模型: 用于判断两个几何对象是否相交等
 * <p>
 * IntersectionMatrix对象（通常简称为DE-9IM矩阵），该对象详细描述了polygon1和polygon2这两个多边形之间的拓扑关系。DE-9IM矩阵是一种标准表示法，用于精确描述两个几何对象之间可能存在的9种基本的空间相互作用，包括相交、包含、重合等关系。
 * <p>
 * DE-9IM矩阵是一个9位的字符串，其中每个字符代表了两个几何对象在某种空间关系上的布尔值（T为真，F为假，*为“不关心”或“任意”），
 * 这九个位置分别对应以下关系：
 * 1.Interior/Interior（内部/内部）: 表示polygon1的内部是否与polygon2的内部相交。
 * 2.Interior/Boundary（内部/边界）
 * 3.Interior/Exterior（内部/外部）
 * 4.Boundary/Interior（边界/内部）
 * 5.Boundary/Boundary（边界/边界）
 * 6.Boundary/Exterior（边界/外部）
 * 7.Exterior/Interior（外部/内部）
 * 8.Exterior/Boundary（外部/边界）
 * 9.Exterior/Exterior（外部/外部）
 * <p>
 * 例如，一个典型的矩阵可能是T*****FF*，这表示polygon1的内部与polygon2的内部相交（T），而其他关系则未特别指定（*）或者确定为不相交（F）。
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:34
 */
public class DE9IMExample {

    public static void main(String[] args) throws ParseException {
        // 创建GeometryFactory对象
        GeometryFactory factory = new GeometryFactory();

        // 创建两个几何对象，例如两个多边形
        WKTReader reader = new WKTReader(factory);
        String poly1WKT = "POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))";
        String poly2WKT = "POLYGON((5 5, 15 5, 15 15, 5 15, 5 5))";
        Geometry polygon1 = reader.read(poly1WKT);
        Geometry polygon2 = reader.read(poly2WKT);

        // 计算九交模型：
        // 通过IntersectionMatrix，你可以执行复杂的空间查询，比如判断一个多边形是否完全包含另一个多边形、是否相交但不包含、是否完全分离等。
        IntersectionMatrix im = polygon1.relate(polygon2);

        // 打印九交模型结果
        System.out.println("九交模型结果: " + im.toString());

        System.out.println("相交=" + im.isIntersects());
        System.out.println("是不相交的=" + im.isDisjoint());
        //。isContains()和isWithin()是互逆的，分别从源对象和目标对象的角度判断包含关系
        System.out.println("包含=" + im.isContains());
        System.out.println("被包含=" + im.isWithin());
        // 而isCoveredBy()和isCovers()也是互逆的，用于判断一个对象是否被另一个对象完全覆盖。
        System.out.println("被覆盖=" + im.isCoveredBy()); // 即第一个几何体被第二个几何体完全包围而不超出，则返回true；否则返回false。
        System.out.println("覆盖=" + im.isCovers()); // 与isCoveredBy()相对, 即第一个几何体完全包围第二个几何体，则返回true；否则返回false。

        System.out.println("------------------");

        if (im.isContains()) {
            System.out.println("Polygon1 contains Polygon2.");
        } else if (im.isWithin()) {
            System.out.println("Polygon2 contains Polygon1.");
        } else if (im.isIntersects()) {
            System.out.println("Polygon1 and Polygon2 intersect but neither contains the other.");
        } else if (im.isDisjoint()) {
            System.out.println("Polygon1 and Polygon2 are disjoint(do not intersect).");
        }

        System.out.println("------------------");

        // 分析并打印空间关系
        if (im.matches("T*F**FFF*")) {
            System.out.println("Polygon1 contains Polygon2.");
        } else if (im.matches("*T***FF*F")) {
            System.out.println("Polygon2 contains Polygon1.");
        } else if (im.matches("T********")) {
            System.out.println("Polygon1 and Polygon2 intersect but neither contains the other.");
        } else if (im.matches("FF*FF****")) {
            System.out.println("Polygon1 and Polygon2 are disjoint (do not intersect).");
        } else {
            System.out.println("Other complex spatial relationship.");
        }

        System.out.println("------------------");
    }


    /**
     * @throws ParseException
     */
    @Test
    public void SpatialRelations_test() throws ParseException {
        // 几何对象工厂
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 8307);

        // 使用两个多边形作为示例，一个较大（假设为容器），一个较小（假设为被包含对象）。

        // 定义两个多边形的坐标
        String wktPolygon1 = "POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0))"; // 较大的多边形（容器）
        String wktPolygon2 = "POLYGON ((1 1, 9 1, 9 9, 1 9, 1 1))"; // 较小的多边形（被包含对象）

        // 创建多边形
        WKTReader reader = new WKTReader(factory);
        Polygon containerPolygon = (Polygon) reader.read(wktPolygon1);
        Polygon containedPolygon = (Polygon) reader.read(wktPolygon2);

        // 计算IntersectionMatrix
        IntersectionMatrix im = containerPolygon.relate(containedPolygon);

        // 验证空间关系
        System.out.println("Container Polygon contains Contained Polygon? " + im.isContains()); // isContains()应该返回true，因为containerPolygon确实包含了containedPolygon
        System.out.println("Contained Polygon is within Container Polygon? " + im.isWithin()); // isWithin()也应该返回false，因为从containedPolygon的角度看，它不位于containerPolygon内部
        System.out.println("Container Polygon is covered by Contained Polygon? " + im.isCoveredBy()); // isCoveredBy()应该返回false，因为containerPolygon并不是被containedPolygon所覆盖。
        System.out.println("Contained Polygon covers Container Polygon? " + im.isCovers()); // isCovers()应该返回true，因为从containerPolygon的角度看，它覆盖了containedPolygon。
    }

}
