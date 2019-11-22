package com.dist.controller;

import com.dist.dto.ShopDTO;
import com.dist.dto.UserDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 所有接口最好使用postman工具测试(有些接口必须用postman工具测试)，swagger测试文件上传和查看等有会有问题
 * @author ZhengJa
 * @description First 模板
 * @data 2019/11/1
 */
@RestController
@RequestMapping(value = "mongo/second")
@Api(tags = {"SecondMongoController"}, value = "第二个mongo数据源")
public class SecondMongoController extends BaseController{

    //存取文件
    @Autowired
    @Qualifier("secondGridFsTemplate")
    private GridFsTemplate gridFsTemplate;

    @Autowired
    @Qualifier("secondGridFSBucket")
    private GridFSBucket gridFSBucket;

    @Autowired
    @Qualifier("secondMongoTemplate")
    private MongoTemplate mongoTemplate;


    /*****************  以下是 数据源2的测试 ****************/


    @ApiOperation(value = "单文件上传-test2Mongo", httpMethod = "POST")
    @RequestMapping(value = "v1/uploadFile2", method = RequestMethod.POST)
    public Object uploadFile2(@ApiParam(value = "选择文件") @RequestParam MultipartFile multiportFile) throws Exception {
        // 获得提交的文件名
        String fileName = multiportFile.getOriginalFilename();
        // 获得文件输入流
        InputStream ins = multiportFile.getInputStream();
        // 获得文件类型
        String contentType = multiportFile.getContentType();
        // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
        ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType);

        //ObjectId objectId2 = gridFsTemplate.store(new FileInputStream(new File("/Users/x5456/Desktop/数据源2")), "数据源2");
        return objectId;
    }


    @ApiOperation(value = "根据文件名称获取文件-test2Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/queryFile2", method = RequestMethod.GET)
    public Object queryFile2(@ApiParam(value = "文件名称") @RequestParam String fileName) {
        GridFSFindIterable result = gridFsTemplate.find(Query.query(GridFsCriteria.whereFilename().is(fileName)));
        GridFSFile gridFSDBFile = result.first();
        return gridFSDBFile.getFilename();
    }

    @ApiOperation(value = "获取所有文件-test2Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/queryAllFile2", method = RequestMethod.GET)
    public Object queryAllFile2() {
        GridFsResource[] resultFiles = gridFsTemplate.getResources("*");
        Map map = new HashMap();
        for (GridFsResource resource : resultFiles) {
            //resource.getInputStream();
            BsonObjectId objectId = (BsonObjectId) resource.getId();
            map.put(objectId.getValue(), resource.getFilename());
        }
        return map;
    }


    @ApiOperation(value = "下载文件-test2Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadFile2", method = RequestMethod.GET)
    public void downloadFile2(@ApiParam(value = "文件_id") @RequestParam String fileId) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        // 查询单个文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSBucket.openDownloadStream(gridFSFile.getObjectId()));
        String fileName = gridFSFile.getFilename().replace(",", "");
        //处理中文文件名乱码
        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            //非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        // 通知浏览器进行文件下载
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        IOUtils.copy(gridFsResource.getInputStream(), response.getOutputStream());
    }


    @ApiOperation(value = "删除文件test2Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/deletefile2", method = RequestMethod.GET)
    public Object deleteFile2(@ApiParam(value = "文件在mongo的id") @RequestParam String fileId) {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        // 查询单个文件
        GridFSFile gfsfile = gridFsTemplate.findOne(query);
        if (gfsfile == null) {
            return "文件不存在！";
        }
        gridFsTemplate.delete(query);
        return "删除成功";
    }



    /*************** 数据源2 文档数据操作 ***************/

    @ApiOperation(value = "保存文档数据到test2Mongo",notes = "数据源2",httpMethod = "GET")
    @RequestMapping(value = "save/document2",method = RequestMethod.GET)
    public void saveDocument2(){
        UserDTO user = new UserDTO();
        user.setId("1");
        user.setUsername("数据源2");
        user.setAge(24);
        ShopDTO shopDTO = new ShopDTO("1","数据源2",21);
        mongoTemplate.save(shopDTO,"shops");
        mongoTemplate.save(user);
    }

    @ApiOperation(value = "单条件查询获取文档数据-test2Mongo-users",notes = "数据源2",httpMethod = "GET")
    @RequestMapping(value = "get/document2/id",method = RequestMethod.GET)
    public Object getDocument2(){
        //id 可以改成其它字段做条件查询
        Query query = new Query(Criteria.where("_id").is("1"));
        List<UserDTO> users = mongoTemplate.find(query, UserDTO.class);
        List<ShopDTO> shops = mongoTemplate.find(query, ShopDTO.class, "shops");

        Map map = new HashMap();
        map.put("users",users);
        map.put("shops",shops);
        return map;
    }

    @ApiOperation(value = "获取集合中所有文档数据-test2Mongo-users",notes = "获取users的所有文档的方式",httpMethod = "GET")
    @RequestMapping(value = "get/all/document2",method = RequestMethod.GET)
    public Object getAllDocument2(){
        List<UserDTO> userDTOS = mongoTemplate.findAll(UserDTO.class);
        List<ShopDTO> shopDTOS = mongoTemplate.findAll(ShopDTO.class, "shops");
        //操作集合对象
        MongoCollection<Document> shops = mongoTemplate.getCollection("shops");

        Map map = new HashMap();
        map.put("userDTOS",userDTOS);
        map.put("shopDTOS",shopDTOS);
        return map;
    }

}
