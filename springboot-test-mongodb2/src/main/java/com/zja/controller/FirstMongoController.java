package com.zja.controller;

import com.zja.dto.ShopDTO;
import com.zja.dto.UserDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author ZhengJa
 * @description First 模板
 * @data 2019/11/1
 */
@RestController
@RequestMapping(value = "mongo/first")
@Api(tags = {"FirstMongoController"}, value = "第一个mongo数据源")
public class FirstMongoController extends BaseController{

    //存取文件
    @Autowired
    @Qualifier("firstGridFsTemplate")
    private GridFsTemplate gridFsTemplate;

    @Autowired
    @Qualifier("firstGridFSBucket")
    private GridFSBucket gridFSBucket;

    @Autowired
    @Qualifier("firstMongoTemplate")
    private MongoTemplate mongoTemplate;


    /***************** mongo数据源1 文件的操作 ****************/
    //注意：所有接口最好使用postman工具测试(有些接口必须用postman工具测试)，swagger测试文件上传和查看等有会有问题

    //注意：单文件上传可以用swagger测试，多文件上传要用postman工具测试
    @ApiOperation(value = "单文件上传", notes = "文件默认是上传到数据中的fs.files和fs.chunks中,files是用来存储文件的信息，文件名，md5,文件大小，还有刚刚的metadata,上传时间等等数据", httpMethod = "POST")
    @RequestMapping(value = "v1/uploadFile1", method = RequestMethod.POST)
    public Object uploadFile1(@ApiParam(value = "选择文件") @RequestParam MultipartFile multiportFile
            /*,@ApiParam(value = "文件名字，带文件后缀", defaultValue = "fileNewName", required = false) @RequestParam String fileNewName*/) throws Exception {

        // 获得提交的文件名
        String fileName = multiportFile.getOriginalFilename();
        // 获得文件输入流
        InputStream ins = multiportFile.getInputStream();
        // 获得文件类型
        String contentType = multiportFile.getContentType();
        //存储文件的额外信息，比如用户ID,后面要查询某个用户的所有文件时就可以直接查询,可以不传
        DBObject metadata = new BasicDBObject("userId", "1001");

        // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
        //ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType);

        ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType, metadata);
        Map map = new HashMap();
        map.put("fileId", objectId.toString());
        map.put("文件具体信息", objectId);
        return map;
    }


    //注意：单文件上传可以用swagger测试，多文件上传要用postman工具测试
    @ApiOperation(value = "多文件批量上传", notes = "文件默认是上传到数据中的fs.files和fs.chunks中,files是用来存储文件的信息，文件名，md5,文件大小，还有刚刚的metadata,上传时间等等数据", httpMethod = "POST")
    @RequestMapping(value = "v1/uploadManyFile1", method = RequestMethod.POST)
    public Object uploadManyFile1(@ApiParam(value = "选择单/多文件") @RequestParam(value = "file") MultipartFile[] multipartFiles) throws Exception {
        Map map = new HashMap();
        System.out.println("multipartFiles.length " + multipartFiles.length);
        if (multipartFiles != null && multipartFiles.length > 0) {
            int count = 0;
            for (MultipartFile multipartFile : multipartFiles) {
                // 获得提交的文件名
                String fileName = multipartFile.getOriginalFilename();
                // 获得文件输入流
                InputStream ins = multipartFile.getInputStream();
                // 获得文件类型
                String contentType = multipartFile.getContentType();
                //存储文件的额外信息，比如用户ID,后面要查询某个用户的所有文件时就可以直接查询,可以不传
                DBObject metadata = new BasicDBObject("userId", "1001");

                // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
                //ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType);

                // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
                ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType, metadata);

                map.put("fileId" + count, objectId.toString());
                map.put("fileName" + count, fileName);
                map.put("文件具体信息" + count, objectId);
                count++;
            }
        }
        return map;
    }

    @ApiOperation(value = "根据文件名称获取文件", httpMethod = "GET")
    @RequestMapping(value = "v1/queryFile1", method = RequestMethod.GET)
    public void queryFile1(@ApiParam(value = "文件名称") @RequestParam String fileName) {
        // TODO: 2019/2/16 真实使用的时候需要判断集合的大小
        GridFSFindIterable result = gridFsTemplate.find(Query.query(GridFsCriteria.whereFilename().is(fileName)));
        GridFSFile gridFSFile = result.first();
        System.out.println("getFilename=" + gridFSFile.getFilename());
    }

    @ApiOperation(value = "根据_id获取文件", httpMethod = "GET")
    @RequestMapping(value = "v1/queryFileid1", method = RequestMethod.GET)
    public Object queryFileid(@ApiParam(value = "文件id") @RequestParam String fileId) {
        GridFSFindIterable result = gridFsTemplate.find(Query.query(Criteria.where("_id").is(fileId)));
        GridFSFile gridFSFile = result.first();
        if (gridFSFile == null) {
            return null;
        }
        System.out.println("getFilename=" + gridFSFile.getFilename());
        return gridFSFile.getFilename();
    }

    @ApiOperation(value = "获取所有文件", httpMethod = "GET")
    @RequestMapping(value = "v1/queryAllFile1", method = RequestMethod.GET)
    public Object queryAllFile1() {
        GridFsResource[] resultFiles = gridFsTemplate.getResources("*");
        Map map = new HashMap();
        for (GridFsResource resource : resultFiles) {
            //resource.getInputStream();
            BsonObjectId objectId = (BsonObjectId) resource.getId();
            map.put(objectId.getValue(), resource.getFilename());
        }
        return map;
    }

    @ApiOperation(value = "下载文件", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadFile1", method = RequestMethod.GET)
    public void downloadFile1(@ApiParam(value = "文件_id") @RequestParam String fileId) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        // 查询单个文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSBucket.openDownloadStream(gridFSFile.getObjectId()));
        String fileName = gridFSFile.getFilename().replace(",", "");
        //处理中文文件名乱码
        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");  //UTF-8  处理文件名乱码
        } else {
            //非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");  //UTF-8
        }
        // 通知浏览器进行文件下载
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        IOUtils.copy(gridFsResource.getInputStream(), response.getOutputStream());
    }

    @ApiOperation(value = "删除文件", httpMethod = "GET")
    @RequestMapping(value = "v1/deletefile1", method = RequestMethod.GET)
    public Object deleteFile1(@ApiParam(value = "文件在mongo的id") @RequestParam String fileId) {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        // 查询单个文件
        GridFSFile gfsfile = gridFsTemplate.findOne(query);
        if (gfsfile == null) {
            return "文件不存在！";
        }
        gridFsTemplate.delete(query);
        return "删除成功";
    }

    @ApiOperation(value = "删除所有文件", httpMethod = "GET")
    @RequestMapping(value = "v1/deleteAllFile", method = RequestMethod.GET)
    public Object deleteAllFile() {
        //调用查询所有文件的方法
        Map queryAllFile1 = (Map) queryAllFile1();
        Set keySet = queryAllFile1.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String next = (String) iterator.next();
            //查询文件是否存在
            Object queryFileid = queryFileid(next);
            if (queryFileid != null) {
                //删除文件
                deleteFile1(next);
            }
        }
        return "删除所有文件成功！";
    }


    @ApiOperation(value = "获取文件中的数据", httpMethod = "GET")
    @RequestMapping(value = "v1/getFileData", method = RequestMethod.GET)
    public Object getFileData(@ApiParam(value = "文件在mongo的id") @RequestParam String fileId) throws IOException {
        //根据id查找文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsSource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFS);
        //获取流中的数据
        String string = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(string);
        return string;
    }

    //用postman工具测试
    @ApiOperation(value = "根据名称获取文件并在线查看",notes = "查看Mongo中的文件")
    @RequestMapping(value = "v1/gridfs/get/file", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public int getGridfs(@RequestParam(value = "fileName", required = true) String fileName,
                         HttpServletResponse response) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(fileName)));
        if (gridFSFile==null){
            System.out.println("File not found" + fileName);
            throw new RuntimeException("No file with name: " + fileName);
        }
        GridFsResource gridFsResource = convertGridFSFile2Resource(gridFSFile);
        return IOUtils.copy(gridFsResource.getInputStream(),response.getOutputStream());
    }

    private GridFsResource convertGridFSFile2Resource(GridFSFile gridFsFile) {
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFsFile.getObjectId());
        return new GridFsResource(gridFsFile, gridFSDownloadStream);
    }

    /** ############# mongo数据源1 文件的操作 结束 ############### **/



    /************** mongo数据源1 文档数据的操作  **********/

    @ApiOperation(value = "保存文档数据到test1Mongo",notes = "数据源1",httpMethod = "GET")
    @RequestMapping(value = "save/document1",method = RequestMethod.GET)
    public void saveDocument1(){
        UserDTO user = new UserDTO();
        user.setId("1");
        user.setUsername("数据源1");

        ShopDTO shopDTO = new ShopDTO("1","数据源1",23);
        ShopDTO shopDTO2 = new ShopDTO("2","数据源1",23);
        ShopDTO shopDTO3 = new ShopDTO("3","数据源1",23);
        ShopDTO shopDTO4 = new ShopDTO("4","数据源1",23);
        mongoTemplate.save(shopDTO,"shops");
        mongoTemplate.save(shopDTO2,"shops");
        mongoTemplate.save(shopDTO3,"shops");
        mongoTemplate.save(shopDTO4,"shops");

        mongoTemplate.save(user);
    }

    @ApiOperation(value = "单条件查询获取文档数据-test1Mongo",notes = "数据源1",httpMethod = "GET")
    @RequestMapping(value = "get/document1/id",method = RequestMethod.GET)
    public Object getDocument1(){
        //id 可以改成其它字段做条件查询
        Query query = new Query(Criteria.where("id").is("1"));
        List<UserDTO> users = mongoTemplate.find(query, UserDTO.class);
        List<ShopDTO> shops = mongoTemplate.find(query, ShopDTO.class, "shops");

        Map map = new HashMap();
        map.put("users",users);
        map.put("shops",shops);
        return map;
    }

    @ApiOperation(value = "获取集合中所有文档数据-test1Mongo",notes = "获取users的所有文档的方式",httpMethod = "GET")
    @RequestMapping(value = "get/all/document1",method = RequestMethod.GET)
    public Object getAllDocument1(){
        List<UserDTO> userDTOS = mongoTemplate.findAll(UserDTO.class);
        List<ShopDTO> shopDTOS = mongoTemplate.findAll(ShopDTO.class, "shops");

        //如果集合名不存在，会在第一次插入文档时创建
        MongoCollection<Document> shops = mongoTemplate.getCollection("shops");

        Map map = new HashMap();
        map.put("userDTOS",userDTOS);
        map.put("shopDTOS",shopDTOS);
        return map;
    }

    /** ########### mongo数据源1 文档数据的操作 结束  ############# **/


    /*********** 以下演示是以 数据源1(test1Mongo) 操作(shops) ==== 数据源2是一样的,这里不重复演示  *************/

    @ApiOperation(value = "复合条件查询,排序-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/conditionalQuery",method = RequestMethod.GET)
    public Object conditionalQuery(){

        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("name").is("数据源1"),
                Criteria.where("age").is(23));
        Query query = new Query(criteria);   //组合查询放入query
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"id"));  //结果集进行排序
        query.with(sort);
        List<ShopDTO> shopDTOS = mongoTemplate.find(query,ShopDTO.class,"shops");
        //mongoDB组合查询数据成功,集合为shops,文档为：shopDTOS
        return shopDTOS;
    }

    @ApiOperation(value = "分页条件查询-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/pageQuery",method = RequestMethod.GET)
    public Object pageQuery(@ApiParam(value = "skip",defaultValue = "1") @RequestParam Integer skip,
                            @ApiParam(value = "limit",defaultValue = "3") @RequestParam Integer limit){

        Query query = new Query();
        //mongoDB分页查询下标为1开始总共3行数据
        query.skip(skip).limit(limit); //// 从那条记录开始， 取多少条记录
        List<ShopDTO> shopDTOS = mongoTemplate.find(query,ShopDTO.class,"shops");
        //mongoDB分页查询数据成功,集合为shops,文档为：shopDTOS
        return shopDTOS;
    }

    @ApiOperation(value = "模糊(regex)查询-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/regexQuery",method = RequestMethod.GET)
    public Object regexQuery(){
        //模糊查询 regex，满足条件直接更新
        Query query = new Query(Criteria.where("name").regex("数据"));
        List<ShopDTO> shopDTOS = mongoTemplate.find(query,ShopDTO.class,"shops");
        for (ShopDTO shopDO : shopDTOS){
            System.out.println(shopDO.getId()+"/ "+shopDO.getName()+"/ "+shopDO.getAge());
        }
        Update update = new Update();
        update.set("name","数据源1-更新");
        update.set("age",20);
        UpdateResult wr = mongoTemplate.updateMulti(query,update,"shops");
        return wr;
    }

    @ApiOperation(value = "更新文档-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/updaateDocument",method = RequestMethod.GET)
    public Object updaateDocument1(@ApiParam(value = "id",defaultValue = "1") @RequestParam String id){
        //记住一定要是 "_id"
        Query query = new Query(Criteria.where("_id").is(id));
        List<ShopDTO> list2 = mongoTemplate.find(query,ShopDTO.class,"shops");
        System.out.println("mongoDB查询主键为1数据成功,集合为shops,文档为：");
        for (ShopDTO shopDO:list2){
            System.out.println(shopDO.getId()+"/ "+shopDO.getName()+"/ "+shopDO.getAge());
        }
        Update update = new Update();
        update.set("name","数据源1-根据id更新");
        update.set("age",18);
        UpdateResult wr = mongoTemplate.updateFirst(query,update,"shops");
        System.out.println("mongoDB更新数据成功,集合为shops,行数为：" + wr.getModifiedCount());
        return wr;
    }

    @ApiOperation(value = "根据_id删除文档-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/deleteDocument",method = RequestMethod.GET)
    public Object deleteDocument1(@ApiParam(value = "_id",defaultValue = "1") @RequestParam String id){

        //记住一定要是 "_id"
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, "shops");
        System.out.println("mongoDB删除数据成功,集合为shops,行数为：" + result.getDeletedCount() + "删除的ID为"+id);
        return result;
    }

    @ApiOperation(value = "删除所有文档-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/deleteAllDocument",method = RequestMethod.GET)
    public Object deleteAllDocument1(){
        List<UserDTO> andRemove = mongoTemplate.remove(UserDTO.class).findAndRemove();
        return andRemove;
    }

}
