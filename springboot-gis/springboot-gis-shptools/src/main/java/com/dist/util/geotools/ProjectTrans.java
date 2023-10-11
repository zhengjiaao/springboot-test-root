package com.zja.util.geotools;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProjectTrans {
    private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
    private static WKTReader reader = new WKTReader( geometryFactory );
    private static WKTWriter write = new WKTWriter();
    static ProjectTrans proj = new ProjectTrans();


    final String strWKTMercator = "PROJCS[\"World_Mercator\","
            + "GEOGCS[\"GCS_WGS_1984\","
            + "DATUM[\"WGS_1984\","
            + "SPHEROID[\"WGS_1984\",6378137,298.257223563]],"
            + "PRIMEM[\"Greenwich\",0],"
            + "UNIT[\"Degree\",0.017453292519943295]],"
            + "PROJECTION[\"Mercator_1SP\"],"
            + "PARAMETER[\"False_Easting\",0],"
            + "PARAMETER[\"False_Northing\",0],"
            + "PARAMETER[\"Central_Meridian\",0],"
            + "PARAMETER[\"latitude_of_origin\",0],"
            + "UNIT[\"Meter\",1]]";
    /**
     * 经纬度转WEB墨卡托
     * @param geom
     * @return
     */
    public Geometry lonlat2WebMactor(Geometry geom){
        try{
            //这里是以OGC WKT形式定义的是World Mercator投影，网页地图一般使用该投影

//			CoordinateReferenceSystem crsTarget = CRS.parseWKT(strWKTMercator);
            CoordinateReferenceSystem crsTarget = CRS.decode("EPSG:3857");

            // 投影转换
            MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crsTarget);
            return JTS.transform(geom, transform);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void projectShape(String inputShp, String outputShp){
        try {
            //源shape文件
            ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(inputShp).toURI().toURL());
            //创建目标shape文件对象
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
            params.put(ShapefileDataStoreFactory.URLP.key, new File(outputShp).toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            // 设置属性
            SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
            //下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置
            CoordinateReferenceSystem crs = CRS.parseWKT(strWKTMercator);
            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), crs));

            //设置writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            //写记录
            SimpleFeatureIterator it = fs.getFeatures().features();
            try {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    SimpleFeature fNew = writer.next();
                    fNew.setAttributes(f.getAttributes());
                    Geometry geom = proj.lonlat2WebMactor((Geometry)f.getAttribute("the_geom"));
                    fNew.setAttribute("the_geom", geom);
                }
            }
            finally {
                it.close();
            }
            writer.write();
            writer.close();
            ds.dispose();
            shapeDS.dispose();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 工具类测试方法
     * @param args
     */
    public static void main(String[] args){
        String wktPoint = "POINT(100.02715479879 33.462715497945)";
        String wktLine = "LINESTRING(108.32803893589 41.306670233001,99.950999898452 25.84722546391)";
        String wktPolygon = "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))";
        try {
            long start = System.currentTimeMillis();

            /*Geometry geom1 = (Geometry) reader.read(wktPoint);
            Geometry geom1T = proj.lonlat2WebMactor(geom1);

            Geometry geom2 = (Geometry) reader.read(wktLine);
            Geometry geom2T = proj.lonlat2WebMactor(geom2);*/

            Geometry geom3 = (Geometry) reader.read(wktPolygon);
            Geometry geom3T = proj.lonlat2WebMactor(geom3);

            /*System.out.println(write.write(geom1T));
            System.out.println(write.write(geom2T));*/
            System.out.println(write.write(geom3T));
            String inputShp = "D:\\data\\beijing\\China4326.shp",
                    outputShp = "D:\\data\\beijing\\China3857.shp";
            proj.projectShape(inputShp, outputShp);
            System.out.println("坐标转换完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}