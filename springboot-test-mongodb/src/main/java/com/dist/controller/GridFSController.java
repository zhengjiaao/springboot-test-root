package com.dist.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**所有接口最好使用postman工具测试(有些接口必须用postman工具测试),swagger测试文件上传和查看等有会有问题
 * @author zhengja@dist.com.cn
 * @data 2019/6/19 16:33
 */
@Api(tags = {"GridFSController"},description = "Mongo文件操作-用Postman工具测试")
@RequestMapping(value = "rest/gridfs")
@RestController
public class GridFSController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Resource
    private GridFSBucket gridFSBucket;

    //大文件上传测试：用Postman工具测试
    @ApiOperation(value = "保存单文件到-testMongoLibrary库",notes = "将文件存到Mongo")
    @RequestMapping(value = "v1/gridfs/save/file",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveGridfs(@RequestParam(value = "file", required = true) MultipartFile file){

        System.out.println("Saving file..");
        DBObject metaData = new BasicDBObject();
        metaData.put("createdDate", new Date());

        String fileName = UUID.randomUUID().toString();

        System.out.println("File Name: " + fileName);

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            gridFsTemplate.store(inputStream, fileName, "image", metaData);
            System.out.println("File saved: " + fileName);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            throw new RuntimeException("System Exception while handling request");
        }
        System.out.println("File return: " + fileName);
        return fileName;
    }

    //注意：多文件上传要用postman工具测试
    @ApiOperation(value = "多文件批量上传-testMongoLibrary库", notes = "文件默认是上传到数据中的fs.files和fs.chunks中,files是用来存储文件的信息，文件名，md5,文件大小，还有刚刚的metadata,上传时间等等数据", httpMethod = "POST")
    @RequestMapping(value = "v1/gridfs/save/Manyfile", method = RequestMethod.POST)
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
                ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType);

                // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
               // ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType, metadata);

                String fileInfo = "文件名称:\n"+fileName+"\n"+"fileId:"+"\n"+objectId.toString()+"\n"+"文件具体信息:\n"+objectId;

                map.put("fileInfo" + count,fileInfo);

                count++;
            }
        }
        return map;
    }

    @ApiOperation(value = "根据名称获取文件并在线查看-testMongoLibrary库",notes = "查看Mongo中的文件")
    @RequestMapping(value = "v1/gridfs/get/file", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
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

    @ApiOperation(value = "根据名称删除文件-testMongoLibrary库",notes = "删除Mongo中的文件")
    @RequestMapping(value = "v1/gridfs/delete/file", method = RequestMethod.DELETE)
    public void deleteGridfs(@RequestParam(value = "fileName", required = true) String fileName) {
        System.out.println("Deleting file.." + fileName);
        gridFsTemplate.delete(new Query().addCriteria(Criteria.where("filename").is(fileName)));
        System.out.println("File deleted " + fileName);
    }

    private GridFsResource convertGridFSFile2Resource(GridFSFile gridFsFile) {
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFsFile.getObjectId());
        return new GridFsResource(gridFsFile, gridFSDownloadStream);
    }

    @ApiOperation(value = "根据名称下载文件-testMongoLibrary库",notes = "下载Mongo文件")
    @RequestMapping(value = "v1/gridfs/download/file", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public int downloadGridfs(@RequestParam(value = "fileName", required = true) String fileName,
                         HttpServletResponse response) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(fileName)));
        if (gridFSFile==null){
            System.out.println("File not found" + fileName);
            throw new RuntimeException("No file with name: " + fileName);
        }
        GridFsResource gridFsResource = convertGridFSFile2Resource(gridFSFile);
        OutputStream sos = response.getOutputStream();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return IOUtils.copy(gridFsResource.getInputStream(),response.getOutputStream());
    }

}
