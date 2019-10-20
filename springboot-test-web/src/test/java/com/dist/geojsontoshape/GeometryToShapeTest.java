package com.dist.geojsontoshape;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.nio.charset.Charset;

/**支持追加写入shp文件的方法
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 16:21
 */
public class GeometryToShapeTest {

    static GeometryCreator gCreator = GeometryCreator.getInstance();

    /**
     * 将几何对象信息写入一个shapefile文件并读取 == 可叠加写入 == MultiPolygon类型
     * 目前shape文件被局限于只能包含同种shape类型，比如Point集合的shape文件中不能掺杂其他类型
     * 但在将来shape文件可能会允许包含多种shape类型 == 混合shape？
     * @throws Exception
     */
    public static void writeSHP(String path, Geometry geometry, String desc) throws Exception {

        // 1.创建shape文件对象
        File file = new File(path);
        ShapefileDataStore ds = new ShapefileDataStore(file.toURI().toURL());

        if (!file.exists()) {
            //如果文件不存在，创建schema，存在的话，就不创建了，防止覆盖
            SimpleFeatureTypeBuilder tBuilder = new SimpleFeatureTypeBuilder();
            // 5.设置
            // WGS84:一个二维地理坐标参考系统，使用WGS84数据
            tBuilder.setCRS(DefaultGeographicCRS.WGS84);
            tBuilder.setName("shapefile");
            // 6.置该shape文件几何类型
            tBuilder.add( "the_geom", Polygon.class );
            // 7.添加一个id
            tBuilder.add("osm_id", Long.class);
            // 8.添加名称
            tBuilder.add("name", String.class);
            // 9.添加描述
            tBuilder.add("des", String.class);
            SimpleFeatureType buildFeatureType = tBuilder.buildFeatureType();
            // 10.设置此数据存储的特征类型
            ds.createSchema(buildFeatureType);
        }

        // 11.设置编码
        ds.setCharset(Charset.forName("UTF-8"));

        // 12.设置writer
        // 为给定的类型名称创建一个特性写入器
        String typeName = ds.getTypeNames()[0];
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriterAppend(typeName,
                Transaction.AUTO_COMMIT);

        // Interface SimpleFeature：一个由固定列表值以已知顺序组成的SimpleFeatureType实例。
        // 13.写一个特征
        SimpleFeature feature = writer.next();

        feature.setAttribute("the_geom", geometry);
        /**
         * 下面的属性值，外面可以当做一个实体对象传进来，不写死！
         */
        feature.setAttribute("osm_id", 1234567890l);
        feature.setAttribute("name", "建筑物");
        feature.setAttribute("des", desc);

        // 14.写入
        writer.write();

        // 15.关闭
        writer.close();

        // 16.释放资源
        ds.dispose();

        // 17.读取shapefile文件的图形信息
        ShpFiles shpFiles = new ShpFiles(path);
        /*
         * ShapefileReader( ShpFiles shapefileFiles, boolean strict,
         * --是否是严格的、精确的 boolean useMemoryMapped,--是否使用内存映射 GeometryFactory gf,
         * --几何图形工厂 boolean onlyRandomAccess--是否只随机存取 )
         */
        ShapefileReader reader = new ShapefileReader(shpFiles, false, true, new GeometryFactory(), false);
        while (reader.hasNext()) {
            System.err.println(reader.nextRecord().shape());
        }
        reader.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("===============创建自己的shp文件==============");
        String MPolygonWKT1 = "MULTIPOLYGON(((121.5837313 31.2435225,121.5852142 31.2444795,121.5860999 31.2434539,121.586133 31.2433016,121.5856866 31.243208,121.5846169 31.2425171,121.5837313 31.2435225)))";
        String wktPolygon = "POLYGON((113.6248492 34.7384023, 113.6248465 34.7381533, 113.6248492 34.7384024, 113.6248492 34.7384023))";
        MultiPolygon multiPolygon1 = gCreator.createMulPolygonByWKT(MPolygonWKT1);
        //写入一个多多边形 【建筑物】== 信合花园
        writeSHP("D:/FileTest/shape/multipol.shp", multiPolygon1,"信合花园1");
        Polygon polygonByWKT = gCreator.createPolygonByWKT(wktPolygon);
        writeSHP("D:/FileTest/shape/multipol.shp", polygonByWKT,"信合花园2");

        String MPolygonWKT2 = "MULTIPOLYGON(((121.5869337 31.2479069,121.5874496 31.248256,121.5877683 31.247914,121.5872516 31.2475652,121.5869337 31.2479069)))";
        //MultiPolygon multiPolygon2 = gCreator.createMulPolygonByWKT(MPolygonWKT2);
        //再追加写入一个多多边形 【建筑物】== 信合花园
        //writeSHP("C:/my/multipol.shp", multiPolygon2,"新金桥大厦");
        //System.out.println("===============打开shp文件==============");

    }

}
