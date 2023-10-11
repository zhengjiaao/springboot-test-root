package com.zja.util.geotools;


import com.alibaba.fastjson.JSON;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.zja.dto.PolygonObject;
import com.zja.dto.ShpDatas;
import com.zja.dto.ShpInfo;
import com.zja.util.IO.StringTokenReader;
import com.zja.util.result.ResponseMessage;
import com.zja.util.result.ResponseResult;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**shapefile读写工具类
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 15:45
 */
public class ShpTools {

    //模板路径
    private String shpTemplate = "/shpTemplate/shpTemplate.shp";

    /**
     * 集合对象构造器【自定义的】
     */
    private  static GeometryCreator gCreator = GeometryCreator.getInstance();

    /**
     * 边界
     */
    private  static ReferencedEnvelope bounds;

    // 画布的宽度
    private static final int IMAGE_WIDTH = 2400;

    // 画布的高度
    private static final int IMAGE_HEIGHT = 1200;


    /**
     * 通过shp文件路径，读取shp内容
     * @param filePath
     * @throws Exception
     */
    public static ShpDatas readShpByPath(String filePath, Integer limit) throws Exception {

        // 一个数据存储实现，允许从Shapefiles读取和写入
        ShapefileDataStore shpDataStore =  new ShapefileDataStore(new File(filePath).toURI().toURL());
        // 设置编码【防止中文乱码】
        shpDataStore.setCharset(Charset.forName("UTF-8"));

        // getTypeNames:获取所有地理图层，这里我只取第一个【如果是数据表，取出的就是表名】
        String typeName = shpDataStore.getTypeNames()[0];
        System.out.println("shp【图层】名称："+typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> result = getFeatures(shpDataStore, typeName);

        // 迭代特征集合
        FeatureIterator<SimpleFeature> iterator = result.features();

        ShpDatas shpDatas = new ShpDatas();
        shpDatas.setName(typeName);
        shpDatas.setShpPath(filePath);
        buildShpDatas(limit, iterator, shpDatas);
        iterator.close();
        return  shpDatas;
    }


    /**
     * 根据数据源及图层名称拿到特征集合
     * @param shpDataStore
     * @param typeName
     * @return
     * @throws IOException
     */
    private static FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(ShapefileDataStore shpDataStore, String typeName) throws IOException {

        // 通过此接口可以引用单个shapefile、数据库表等。与数据存储进行比较和约束
        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = shpDataStore.getFeatureSource(typeName);
        // 一个用于处理FeatureCollection的实用工具类。提供一个获取FeatureCollection实例的机制
        FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();
        System.out.println("地理要素【记录】："+result.size()+"个");
        System.out.println("==================================");
        return result;
    }

    /**
     * 构建shpDatas对象
     * @param limit
     * @param iterator
     * @param shpDatas
     */
    private static void buildShpDatas(Integer limit, FeatureIterator<SimpleFeature> iterator, ShpDatas shpDatas) {
        // 这里我们只迭代前limit个
        int stop = 0;
        while (iterator.hasNext()) {
            if (stop > limit) {
                break;
            }
            // 拿到一个特征
            SimpleFeature feature = iterator.next();
            // 取出特征里面的属性集合
            Collection<Property> p = feature.getProperties();

            // 遍历属性集合
            Map<String,Object> prop = new HashMap<>();
            for (Property pro : p) {
                String key = pro.getName().toString();
                String val = pro.getValue().toString();
                prop.put(key, val);
                System.out.println("key【字段】："+key+"\t||  value【值】："+val);
            }
            System.out.println("\n============================ 序号："+stop+"\n");
            shpDatas.addProp(prop);
            stop++;
        } // end 最外层 while
    }

    /**
     * 将一个几何对象写进shapefile
     * @param filePath
     * @param geometry
     */
    public  static  void writeShpByGeom(String filePath, Geometry geometry) throws Exception{

        ShapefileDataStore ds = getshpDS(filePath, geometry);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0],
                Transaction.AUTO_COMMIT);

        // Interface SimpleFeature：一个由固定列表值以已知顺序组成的SimpleFeatureType实例。
        SimpleFeature feature = writer.next();
        feature.setAttribute("name", "XXXX名称");
        feature.setAttribute("path", "D:/FileTest/shape");
        feature.setAttribute("the_geom", geometry);
        feature.setAttribute("id", 1010L);
        feature.setAttribute("des", "XXXX描述");

        System.out.println("========= 写入【"+geometry.getGeometryType()+"】成功 ！=========");

        // 写入
        writer.write();

        // 关闭
        writer.close();

        // 释放资源
        ds.dispose();

    }

    /**
     * 支持追加写入shp文件的方法：可以持续性的像shp文件写入数据，不会覆盖shp文件里之前的数据
     *
     * 将几何对象信息写入一个shapefile文件并读取 == 可叠加写入 == MultiPolygon类型
     * 目前shape文件被局限于只能包含同种shape类型，比如Point集合的shape文件中不能掺杂其他类型
     * 但在将来shape文件可能会允许包含多种shape类型 == 混合shape
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

            //创建 shp文件 方式二，根据已知shp坐标系创建
            //10.设置此数据存储的特征类型  和  地理坐标
            /*System.out.println("shpTemplate= "+this.getClass().getResource(shpTemplate).getPath());

            //Map map = getCoordinate("C:\\Users\\Administrator\\Desktop\\空间地理转换\\规划分区是否符合生态红线要求_1567315808013\\规划分区是否符合生态红线要求_1567315808013.shpTemplate");
            Map map = getCoordinate( this.getClass().getResource(shpTemplate).getPath());
            //SimpleFeatureType simpleFeatureType = (SimpleFeatureType) map.get("Schema");
            CoordinateReferenceSystem coordinateReferenceSystem = (CoordinateReferenceSystem) map.get("CoordinateReferenceSystem");

            //10.设置此数据存储的特征类型  和  地理坐标
            ds.createSchema(SimpleFeatureTypeBuilder.retype(buildFeatureType, coordinateReferenceSystem));
            */

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
        feature.setAttribute("osm_id", 123450L);
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

    /**
     * 获取Shape文件的坐标系信息。
     *
     * @param shpFilePath shp文件路径
     */
    public static Map getCoordinate(String shpFilePath) {

        ShapefileDataStore dataStore = buildDataStore(shpFilePath);
        Map map = new HashMap();
        try {
            map.put("Schema", dataStore.getSchema());
            map.put("CoordinateReferenceSystem", dataStore.getSchema().getCoordinateReferenceSystem());
            return map;
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataStore.dispose();
        }
        return null;
    }

    /**
     * 构建ShapeDataStore对象。
     *
     * @param shpFilePath shape文件路径。
     * @return
     */
    public static ShapefileDataStore buildDataStore(String shpFilePath) {
        ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        try {
            ShapefileDataStore dataStore = (ShapefileDataStore) factory
                    .createDataStore(new File(shpFilePath).toURI().toURL());
            if (dataStore != null) {
                dataStore.setCharset(Charset.forName("UTF-8"));
            }
            return dataStore;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




    /**
     * 将一个几何对象写进shapefile
     * @param shpInfo
     */
    public  static ResponseResult writeShpByGeom(ShpInfo shpInfo) throws Exception{

        // 特殊字符串解析器
        StringTokenReader reader = new StringTokenReader();
        // 根据几何对象的wkt字符串，反解【解析】成Geometry对象
        Geometry geometry = reader.read(shpInfo.getGeom());
        // 拿到shp对象所在的目录【文件夹】
        String path = shpInfo.getPath();
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }

        if(!file.isDirectory()){
            return  new ResponseResult(ResponseMessage.BAD_REQUEST,"path不是有效的文件夹" );
        }

        String filePath = shpInfo.getPath()+"/"+shpInfo.getName()+".shp";
        ShapefileDataStore ds = getshpDS(filePath, geometry);
        String typeName = ds.getTypeNames()[0];
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer ;
        if(shpInfo.isAppendWrite()){
            // 追加写几何对象
            writer = ds.getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT);
        }else{
            // 覆盖写几何对象
            writer = ds.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);
        }

        // Interface SimpleFeature：一个由固定列表值以已知顺序组成的SimpleFeatureType实例。
        SimpleFeature feature = writer.next();
        feature.setAttribute("name", shpInfo.getName());
        feature.setAttribute("path", shpInfo.getPath());
        feature.setAttribute("the_geom", geometry);
        feature.setAttribute("id", shpInfo.getId());
        feature.setAttribute("des", shpInfo.getDes());

        System.out.println("========= 写入【"+geometry.getGeometryType()+"】成功 ！=========");

        // 写入
        writer.write();

        // 关闭
        writer.close();

        // 释放资源
        ds.dispose();

        // 返回创建成功后的shp文件路径
        return  new ResponseResult(ResponseMessage.OK,filePath);

    }

    /**
     * 拿到配置好的DataStore
     * @param filePath
     * @param geometry
     * @return
     * @throws IOException
     */
    private static ShapefileDataStore getshpDS(String filePath, Geometry geometry) throws IOException {
        // 1.创建shape文件对象
        File file = new File(filePath);

        Map<String, Serializable> params = new HashMap<>();

        // 2、用于捕获参数需求的数据类 URLP:url to the .shp file.
        params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());

        // 3、创建一个新的数据存储【如果存在，则不创建】
        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

        // 4、定义图形信息和属性信息 -- SimpleFeatureTypeBuilder 构造简单特性类型的构造器
        SimpleFeatureTypeBuilder tBuilder = new SimpleFeatureTypeBuilder();

        // 5、设置 -- WGS84:一个二维地理坐标参考系统，使用WGS84数据
        tBuilder.setCRS(DefaultGeographicCRS.WGS84);
        tBuilder.setName("shapefile");

        // 添加名称
        tBuilder.add("name", String.class);
        // 添加shp所在目录名称
        tBuilder.add("path", String.class);
        // 添加 一个几何对象
        tBuilder.add("the_geom", geometry.getClass());
        // 添加一个id
        tBuilder.add("id", Long.class);
        // 添加描述
        tBuilder.add("des", String.class);

        // 设置此数据存储的特征类型
        ds.createSchema(tBuilder.buildFeatureType());

        // 设置编码
        ds.setCharset(Charset.forName("UTF-8"));
        return ds;
    }

    /**
     * 打开shp文件,获取地图内容
     * @param filePath  文件路径
     * @param isOpenByChoose 是否自定义打开shp文件
     * @throws Exception
     */
    public static MapContent getMapContentByPath(String filePath, boolean isOpenByChoose, String color) throws  Exception{

        File file;
        if(isOpenByChoose){
            // 1.1、 数据源选择 shp扩展类型的
            file = JFileDataStoreChooser.showOpenFile("shp", null);
        }else{
            // 1.2、根据路径拿到文件对象
            file = new File(filePath);
        }

        if(file==null){
            return null;
        }
        // 2、得到打开的文件的数据源
        FileDataStore store = FileDataStoreFinder.getDataStore(file);

        // 3、设置数据源的编码，防止中文乱码
        ((ShapefileDataStore)store).setCharset(Charset.forName("UTF-8"));

        /**
         * 使用FeatureSource管理要素数据
         * 使用Style（SLD）管理样式
         * 使用Layer管理显示
         * 使用MapContent管理所有地图相关信息
         */

        // 4、以java对象的方式访问地理信息 --    简单地理要素
        SimpleFeatureSource featureSource = store.getFeatureSource();

        bounds = featureSource.getBounds();

        // 5、创建映射内容，并将我们的shapfile添加进去
        MapContent mapContent = new MapContent();

        // 6、设置容器的标题
        mapContent.setTitle("Appleyk's GeoTools");

        Color color1;
        if(color == null || "".equals(color)){
            color1 = Color.BLACK;
        }else if("red".equals(color)){
            color1 = Color.RED;
        }else if("green".equals(color)){
            color1 = Color.GREEN;
        }else if("blue".equals(color)){
            color1 = Color.BLUE;
        }else{
            color1 = Color.ORANGE;
        }

        // 7、创建简单样式 【颜色填充】
        Style style = SLD.createSimpleStyle(featureSource.getSchema(),color1);

        // 8、显示【shapfile地理信息+样式】
        Layer layer = new FeatureLayer(featureSource, style);

        // 9、将显示添加进map容器
        mapContent.addLayer(layer);

        return  mapContent;
    }

    public  static  void showMap(MapContent mapContent){
        JMapFrame.showMap(mapContent);
    }

    /**
     * shp文件转Image【格式定png】
     * @param shpFilePath shp目标文件
     * @param destImagePath 转成图片的文件 == 如果没有，转成的图片写进response输出流里
     * @param response 响应流
     * @throws Exception
     */
    public static void shp2Image(String shpFilePath,String destImagePath,String color, HttpServletResponse response) throws  Exception{

        // 流渲染器
        StreamingRenderer renderer = new StreamingRenderer();
        MapContent mapContent = getMapContentByPath(shpFilePath,false,color );
        renderer.setMapContent(mapContent);
        Rectangle imageBounds = new Rectangle(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        BufferedImage dumpImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = dumpImage.createGraphics();
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.paint(g2d, imageBounds, bounds);
        g2d.dispose();
        if(destImagePath == null || "".equals(destImagePath)){
            ImageIO.write(dumpImage, "png", response.getOutputStream());
        }else{
            ImageIO.write(dumpImage, "png", new File(destImagePath+".png"));
        }
    }

    public static void main(String[] args) throws  Exception{

        /*System.out.println("=================开始从shp文件读取数据信息===================");

        File file = ResourceUtils.getFile("classpath:static/shpTest[Point]/dp_tl.shp");
        // 从shp文件里面读取属性信息
        readShpByPath(file.getAbsolutePath(),10);

        System.out.println("=================下面开始往shp文件里面写几何对象===================");

        // 先创建文件夹D:/FileTest/shape
        String filePath = "D:/FileTest/shape/test.shp";
        // 先创建文件夹D:/FileTest/shape
        String filePath2 = "D:/FileTest/shape/Polygon.shp";

        String pointWkt="POINT (120.76164848270959 31.22001141278534)";
        Point point = gCreator.createPointByWKT(pointWkt);

        // Polygon【面】
        String polygonWkt="POLYGON ((103.859188 34.695908, 103.85661 34.693788, 103.862027 34.69259, 103.863709 34.695078, 103.859188 34.695908))";
        com.vividsolutions.jts.geom.Polygon polygon = gCreator.createPolygonByWKT(polygonWkt);

        // LineString【线】
        String linestringWkt="LINESTRING(113.511315990174 41.7274734296674,113.51492087909 41.7284983348307,113.516079593384 41.727649586406,113.515907932007 41.7262243043929,113.514019656861 41.7247989907606,113.512131381714 41.7250872589898,113.51138036319 41.7256637915682,113.511315990174 41.7274734296674)";
        LineString lineString = gCreator.createLineByWKT(linestringWkt);

        // MultiPolygon【多面】
        String multiPolyWkt = "MULTIPOLYGON(((101.870371 25.19228,101.873633 25.188183,101.880564 25.184416,101.886808 25.186028,101.892043 25.189969,101.896592 25.190163,101.903716 25.190785,101.905454 25.193464,101.899897 25.196202,101.894146 25.197911,101.891657 25.19826,101.886078 25.197658,101.884211145538 25.2007060137013,101.88172564506 25.1949712942389,101.87874 25.199619,101.874641 25.200998,101.868547 25.202415,101.863741 25.202415,101.85887 25.202842,101.854557 25.202182,101.852604 25.199736,101.852282 25.19628,101.854492 25.194183,101.855608 25.192668,101.863698 25.192105,101.870371 25.19228)))";
        MultiPolygon multiPolygon = gCreator.createMulPolygonByWKT(multiPolyWkt);

        // 几何对象的范围【矩形边界】
        Envelope envelope = polygon.getEnvelopeInternal();
        System.out.println(envelope);

        // 往shp文件里面写几何对象
        //writeShpByGeom(filePath,point);

        // 往shp文件里面写几何对象
        writeShpByGeom(filePath2,polygon);

        System.out.println("=====================读取刚刚写入的数据===================");
        File file2 = ResourceUtils.getFile(filePath2);
        // 从shp文件里面读取属性信息
        readShpByPath(file2.getAbsolutePath(),10);*/


        System.out.println("=======================以下是国土目数据测试===========================");

        //项目数据
        String wktjson = "{\"rings\":[[[120.01481126800002,30.686383128000074],[120.0148690840001,30.686365174000098],[120.01505557100008,30.686386795000022],[120.01534595500003,30.686396879000057],[120.01533167000001,30.686650692000097],[120.01565841500003,30.686592511000079],[120.01581216000015,30.686459598000095],[120.01579599200014,30.686333997000094],[120.01590043000007,30.686279364000015],[120.01597513900005,30.68623994400004],[120.01605798600006,30.686164802000039],[120.01612006500014,30.686082449000065],[120.01615709799999,30.685964206000051],[120.01619881600007,30.685867604000052],[120.01626902600002,30.685713640000102],[120.01633536600001,30.685538208000064],[120.01637259399998,30.685373515000027],[120.01638904800009,30.685158677000103],[120.01641810800001,30.684851113000057],[120.01642214500008,30.684646835000031],[120.01640564100005,30.684568011000081],[120.01637652100013,30.684521564000057],[120.01632684,30.684471423999994],[120.01628550900014,30.68445348300002],[120.01616539300007,30.684432033000022],[120.01585044900003,30.68440338900005],[120.01565439900006,30.684391274000024],[120.015467918,30.684384082000051],[120.01531462100002,30.684373367000052],[120.01519043200004,30.684341182000054],[120.01506197099998,30.684330465000055],[120.01496262400011,30.684330476000056],[120.01483416400005,30.684334186000097],[120.01470590700001,30.684330505000059],[120.01458558800009,30.684301839000085],[120.01448379700001,30.684301851000086],[120.01442414800015,30.684301857999998],[120.01431238500004,30.684316297000038],[120.01417151100011,30.684348512000039],[120.01400580100008,30.684366477000015],[120.01375315500005,30.684352076000067],[120.01363303700006,30.684319890000069],[120.01356666500007,30.684280484000094],[120.0135336770001,30.684223303000071],[120.0135336660001,30.684140959000008],[120.013562544,30.683926119999995],[120.01361219200011,30.683750692000046],[120.01364482500006,30.683640464000074],[120.0135336500001,30.683612460000067],[120.01352890900014,30.683611293000059],[120.0130657400001,30.683497335000045],[120.01291850800008,30.683631426000037],[120.01283484800001,30.683710024000057],[120.01269762700011,30.683841305999994],[120.01267759200005,30.68386117600004],[120.01266764400012,30.683870026000061],[120.01263019200003,30.683907354000041],[120.01262583600013,30.683912205000087],[120.01256361500012,30.683973378000086],[120.01247047100006,30.684072836000055],[120.01242721200003,30.684119026000058],[120.01237113800001,30.684178901000038],[120.01236387100003,30.684187234000017],[120.01231385100006,30.684245450000038],[120.01220088800004,30.684387271000048],[120.01209549500003,30.684533247000047],[120.01199750300009,30.684682943999999],[120.011985641,30.684702610000027],[120.01197263499999,30.684724171000035],[120.01193908600014,30.684782704000082],[120.01190765100012,30.684836420000085],[120.0118397750001,30.684960974999999],[120.0118086790001,30.685024842000043],[120.01180833900007,30.685026549000007],[120.01200530200002,30.685068190000077],[120.01209651000011,30.685100556000091],[120.01214618900005,30.685139789000051],[120.01214619700013,30.685200667000025],[120.01211709200004,30.685265068000025],[120.01207984100006,30.685293751000088],[120.01200940300009,30.685311705000064],[120.01190598200007,30.685311715000065],[120.01173191700005,30.685286569000027],[120.01171567400004,30.685283066000093],[120.01170924100005,30.685294882000093],[120.01165156700007,30.685451392000076],[120.01162679300015,30.685537209000071],[120.01161995800011,30.685560887000076],[120.01158403400008,30.685647691000064],[120.01153725400002,30.685823585000051],[120.01149532900001,30.685997305000036],[120.01148370600004,30.686136573000013],[120.01149734100009,30.686231173000024],[120.01147101500014,30.686503884000029],[120.01146758300003,30.68653944700004],[120.01131855300005,30.686883723000015],[120.01129074399996,30.687062548000068],[120.01125816200012,30.687240742000068],[120.0112268390001,30.687418973000071],[120.0111839530001,30.687559033000113],[120.01116686700001,30.687684713000031],[120.01098274900006,30.688737978000049],[120.01146460500006,30.688663955000052],[120.01181681000006,30.688617298000011],[120.01222255400015,30.688545648000034],[120.01250024300012,30.688488438000007],[120.01270952200002,30.68841047200004],[120.01279828200009,30.688377384000056],[120.01294343100002,30.68830575800008],[120.0132084830001,30.68814473700008],[120.01342792900004,30.687990758000041],[120.01362681300007,30.687836781000001],[120.01377582600006,30.68774016900009],[120.01394153200009,30.687643379000075],[120.014119658,30.68755397700005],[120.01431020000004,30.68744662700005],[120.01439305000005,30.68737852400001],[120.01447997000005,30.687303209000085],[120.01453370900006,30.687253233000035],[120.01458764100008,30.687124255000111],[120.01462080200007,30.686973990000112],[120.01464968600007,30.686809298000075],[120.01468711000005,30.686572992000062],[120.01470358700008,30.686479912999999],[120.01474083399997,30.686419206000039],[120.01481126800002,30.686383128000074]]],\"spatialReference\":{\"wkid\":4490,\"latestWkid\":4490}}";

        PolygonObject polygonObject = JSON.parseObject(wktjson, PolygonObject.class);
        java.util.List<java.util.List<Double[]>> rings = polygonObject.getRings();
        Coordinate coords[] = null;
        for (List<Double[]> dou : rings) {
            int i = 0;
            coords = new Coordinate[dou.size()];
            for (Double[] dous : dou) {
                coords[i] = new Coordinate(dous[0], dous[1]);
                System.out.println("coords[i]=" + coords[i]);
                i++;
            }
        }
        System.out.println("spatialReference= " + polygonObject.getSpatialReference());
        Geometry geometry = WktToGeometry.createGeometry(coords,polygonObject.getSpatialReference().get("wktid"));

        // 先创建文件夹D:/FileTest/shape
        String filePath3 = "D:/FileTest/shape/GuoTuPolygon.shp";

        // 往shp文件里面写几何对象
        writeSHP(filePath3,geometry,"第一次加入数据");

        System.out.println("=====================读取shp文件国土目数据测试===================");
        File file3 = ResourceUtils.getFile(filePath3);
        // 从shp文件里面读取属性信息
        readShpByPath(file3.getAbsolutePath(),10);


        System.out.println("=====================shp文件追加数据测试===================");

        writeSHP(filePath3,geometry,"追加数据");

        System.out.println("=====================追加数据后-查看shp文件追加数据测试===================");
        File file4 = ResourceUtils.getFile(filePath3);
        // 从shp文件里面读取属性信息
        readShpByPath(file4.getAbsolutePath(),10);


       /* System.out.println("=====================从国土shp文件读取数据格式========================");
        // 先创建文件夹D:/FileTest/shape
        String filePath4 = "D:\\RJAZ\\Tencenthuancun\\文件暂存区\\ArcGIS\\规划分区是否符合基本农田要求_1566811347526\\规划分区是否符合基本农田要求_1566811347526.shp";
        File file4 = ResourceUtils.getFile(filePath4);
        // 从shp文件里面读取属性信息
        readShpByPath(file4.getAbsolutePath(),5);*/

        /*****************************读取shp文件数据测试*******************************/

        ShpDatas shpDatas = readShpByPath(filePath3, 100);
        System.out.println(shpDatas.getProps());

        /*Geometry geometry =WktToGeometry.createGeometry(coords,4490);
        //生成shpTemplate文件 模板
        writeSHP(filePath6,geometry);*/

        System.out.println("Start GeoTools success!");
    }



}
