package com.dist.controller;

import com.dist.service.FileQuickLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/27 18:44
 */
@RequestMapping(value = "rest/filequickload")
@RestController
@Api(tags = {"FileQuickLoadController"}, description = "大文件快速加载")
public class FileQuickLoadController {

    @Autowired
    FileQuickLoad fileQuickLoad;

    public final static int COUNT = 3;      //分段数

    @ApiOperation(value = "大文件快速加载", httpMethod = "GET")
    @RequestMapping(value = "v1/quickLoadFile", method = RequestMethod.GET)
    public Object quickLoadFile() throws Exception{

        long begin = System.currentTimeMillis();
        String fileName = "D:\\FileTest\\移动电子底图.tpk";
        String filePath = "D:\\FileTest\\移动电子.tpk";
        RandomAccessFile randomFile = null;
        randomFile = new RandomAccessFile(fileName, "r");
        long fileLength = randomFile.length();
        System.out.println("fileLength="+fileLength);

        Map<String,Object> map = new HashMap<>();
        byte[] bytes=null;

        //分块大小
        int blockSize = (int) (fileLength / COUNT);
        for(int i=0; i <= COUNT; i++){
            int startPos = i * blockSize;
            int endPos = (i+1) * blockSize - 1;

            //最后一条线程EndPos = fileLength
            if(i == COUNT){
                endPos = (int) fileLength;
            }
            System.out.println("线程" + i + "读的部分为：" + startPos +"---" + endPos);
            bytes = fileQuickLoad.quickLoad(i, startPos, endPos, filePath);
            //System.out.println("bytes== "+bytes);
        }

        long end = System.currentTimeMillis();
        System.out.println("用时" + (end - begin));

        return null;
    }
}
