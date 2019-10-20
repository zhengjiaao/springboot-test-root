package com.dist.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**所有接口最好使用postman工具测试(有些接口必须用postman工具测试)，swagger测试文件上传和查看等有会有问题
 * @author zhengja@dist.com.cn
 * @data 2019/9/2 14:14
 */
@RestController
@RequestMapping(value = "gridfs/mongo")
@Api(tags = {"GridFsTemplateController"}, description = "GridFsTemplate-存取文件-双数据源")
public class GridFsTemplateController extends BaseController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    @Qualifier(value = "secondGridFsTemplate")
    private GridFsTemplate gridFsTemplate2;

    @Autowired
    @Qualifier(value = "secondGridFSBucket")
    private GridFSBucket gridFSBucket2;

    /***************************************** 数据源1的测试 ***********************************************/

    //注意：单文件上传可以用swagger测试，多文件上传要用postman工具测试
    @ApiOperation(value = "单文件上传-test1Mongo-gridFsTemplate", notes = "文件默认是上传到数据中的fs.files和fs.chunks中,files是用来存储文件的信息，文件名，md5,文件大小，还有刚刚的metadata,上传时间等等数据", httpMethod = "POST")
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
    @ApiOperation(value = "多文件批量上传-test1Mongo", notes = "文件默认是上传到数据中的fs.files和fs.chunks中,files是用来存储文件的信息，文件名，md5,文件大小，还有刚刚的metadata,上传时间等等数据", httpMethod = "POST")
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

    @ApiOperation(value = "根据文件名称获取文件-test1Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/queryFile1", method = RequestMethod.GET)
    public void queryFile1(@ApiParam(value = "文件名称") @RequestParam String fileName) {
        // TODO: 2019/2/16 真实使用的时候需要判断集合的大小
        GridFSFindIterable result = gridFsTemplate.find(Query.query(GridFsCriteria.whereFilename().is(fileName)));
        GridFSFile gridFSFile = result.first();
        System.out.println("getFilename=" + gridFSFile.getFilename());
    }

    @ApiOperation(value = "根据_id获取文件-test1Mongo", httpMethod = "GET")
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

    @ApiOperation(value = "获取所有文件-test1Mongo", httpMethod = "GET")
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

    @ApiOperation(value = "下载文件-test1Mongo", httpMethod = "GET")
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

    @ApiOperation(value = "删除文件test1Mongo", httpMethod = "GET")
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

    @ApiOperation(value = "删除所有文件test1Mongo", httpMethod = "GET")
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


    @ApiOperation(value = "获取文件中的数据-test1Mongo", httpMethod = "GET")
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
    @ApiOperation(value = "根据名称获取文件并在线查看-test1Mongo",notes = "查看Mongo中的文件")
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


    /*****************************************数据源1的测试 结束***********************************************/


    /*****************************************以下是 数据源2的测试***********************************************/


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
        ObjectId objectId = gridFsTemplate2.store(ins, fileName, contentType);

        //ObjectId objectId2 = gridFsTemplate.store(new FileInputStream(new File("/Users/x5456/Desktop/数据源2")), "数据源2");
        return objectId;
    }


    @ApiOperation(value = "根据文件名称获取文件-test2Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/queryFile2", method = RequestMethod.GET)
    public Object queryFile2(@ApiParam(value = "文件名称") @RequestParam String fileName) {
        GridFSFindIterable result = gridFsTemplate2.find(Query.query(GridFsCriteria.whereFilename().is(fileName)));
        GridFSFile gridFSDBFile = result.first();
        return gridFSDBFile.getFilename();
    }

    @ApiOperation(value = "获取所有文件-test2Mongo", httpMethod = "GET")
    @RequestMapping(value = "v1/queryAllFile2", method = RequestMethod.GET)
    public Object queryAllFile2() {
        GridFsResource[] resultFiles = gridFsTemplate2.getResources("*");
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
        GridFSFile gridFSFile = gridFsTemplate2.findOne(query);
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
        GridFSFile gfsfile = gridFsTemplate2.findOne(query);
        if (gfsfile == null) {
            return "文件不存在！";
        }
        gridFsTemplate.delete(query);
        return "删除成功";
    }


}
