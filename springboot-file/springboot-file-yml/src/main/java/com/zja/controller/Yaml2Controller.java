package com.zja.controller;

import com.alibaba.fastjson.JSON;
import com.zja.config.BizConfig;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-17 10:49
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RestController
@Api(tags = {"Yaml2Controller"}, description = "yml配置管理")
public class Yaml2Controller {

    @Autowired
    BizConfig bizConfig;

    @Autowired
    ContextRefresher contextRefresher;

    @GetMapping("getYmlInfoPrev")
    public HashMap<String,Object> getYmlInfo(
            @RequestParam(value = "ymlFilename",required = false) String ymlFilename
    ) throws IOException {
        HashMap<String,Object> hashMap=new HashMap<>();
        //此方法可以获取到到classes目录的绝对路径，此路径的上一级为模块名。再上一级为项目根目录名即项目名
        //ClassUtils.getDefaultClassLoader().getResource("").getPath()
        //获取相对地址前缀的位置(例如)下面的位置是项目位置，再下面那个是模块名
        Integer modelePrefixPosition= ClassUtils.getDefaultClassLoader().getResource("")
                .getPath().split("/").length-3;
        Integer projectPrefixPosition=modelePrefixPosition-1;
        //将路径根据/号切割成数组，再根据上面的位置信息以及文件名组成地址返回给前端
        String[] filenamePrefix=ClassUtils.getDefaultClassLoader().getResource("")
                .getPath().split("/");
        //路径获取完成，开始获取yml文件名然后组合它们存进列表
        //获取resources目录下所有后缀为yml的文件名,例如(application.yml)
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:*.yml");

        //获取文件完整路径Arrays.stream(resources).findFirst().get().getURL();
        //获取文件路径，相比于上面那个少了自身名字以及file: ClassUtils.getDefaultClassLoader().getResource("").getPath();


        List<String> ymlFilenameListPath=new ArrayList<>();
        for (Resource resource:resources) {
            //返回yml文件的相对地址
            ymlFilenameListPath.add(filenamePrefix[projectPrefixPosition]+"/"+filenamePrefix[modelePrefixPosition]+"/"+resource.getFilename());
        }
        hashMap.put("ymlFilenamePath",ymlFilenameListPath);

        //开始获取yml文件的内容
        Yaml yaml = new Yaml();
        for (Resource resource:resources) {
            String ymlFilenameprev= resource.getFilename();
            Resource ymlResource =new ClassPathResource(ymlFilenameprev);
            //非空判断
            if (null!=yaml.load(ymlResource.getInputStream())&&ymlFilename.equals(ymlFilenameprev)) {
                System.out.println(ymlFilename+"--"+ymlFilenameprev+"--"+yaml.load(ymlResource.getInputStream()));
                hashMap.put(ymlFilenameprev.split("\\.")[0], yaml.load(ymlResource.getInputStream()));
            }
        }
        hashMap.put("msg","修改信息已提交");
        //需求变更，需要单独返回模块名和文件名

        return hashMap;
    }


    /**
     * 修改信息有两种情况，第一种是修改classes目录下的文件这种情况不会
     * @param ymlInfoAfterChangingContent   前端传入修改后的json数据
     * @param ymlFilename                   前端传入需修改的文件名(需要带.yml后缀)
     * @return
     * @throws IOException
     */
    @PostMapping("setYmlInfo1")
    public HashMap<String,String> setYmlInfo(
            @RequestParam(value = "ymlInfoAfterChangingContent") String ymlInfoAfterChangingContent,//使用@Requestbody接收json数据
            @RequestParam(value = "ymlFilename") String ymlFilename) throws IOException, JSONException {
        System.out.println("开始处理");
        HashMap<String,String> hashMap=new HashMap<>();
        //获取resources目录下所有后缀为yml的文件名,例如(application.yml)
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:*.yml");
        String filePathOrigin=String.valueOf(Arrays.stream(resources).findFirst().get().getURL());
        Integer startPosition=filePathOrigin.indexOf("/");
        Integer endPosition=filePathOrigin.indexOf("application");
        //System.out.println(filePathOrigin.substring(startPosition+1,endPosition)+ymlFilename);
        System.out.println(filePathOrigin.substring(startPosition+1,endPosition));
        File file=new File(filePathOrigin.substring(startPosition+1,endPosition)+ymlFilename.substring(ymlFilename.indexOf("application")));
        FileWriter fileWriter = new FileWriter(file);
        //将传入的数据以json格式写进yml
        fileWriter.write(ymlInfoAfterChangingContent.trim());
        fileWriter.flush();
        fileWriter.close();
        hashMap.put("msg","success");
        //修改完成之后刷新容器
        refresh();
        refresh();
        return hashMap;
    }

    /*
    @GetMapping("usertest")
    public HashMap<String,Object> usertest() throws IOException {
        User user=userContainer.getUser();
        System.out.println(user.getAge());
        System.out.println(user.getId());
        HashMap<String,Object> hashMap=new HashMap<>();
        System.out.println();
        Map totalInfo,speciaficInfo,thirdInfo,fourthInfo;
        Yaml yaml = new Yaml();
        File file = new File(System.getProperty("user.dir")+"\\dubboconsumer\\src\\main\\resources\\application.yml");

        //也可以将值转换为Map
        totalInfo = (Map) yaml.load(new FileInputStream(file));
        //通过map我们取值就可以了.
        speciaficInfo = (Map) totalInfo.get("msg");
        thirdInfo=(Map)speciaficInfo.get("user");
        thirdInfo.put("id",777);
        hashMap.put("m1",speciaficInfo);
        System.out.println(totalInfo);
        System.out.println(totalInfo.toString());
        return hashMap;
    }

     */
    /**
     * 显示信息
     * @return
     */
    @GetMapping(path = "/show")
    public String show() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("biz", JSON.toJSONString(bizConfig));
        return res.toString();
    }

    /**
     * 刷新配置
     * @return
     */
    @PostMapping(path = "/refresh")
    public String refresh() throws JSONException {
        new Thread(() -> contextRefresher.refresh()).start();
        return show();
    }
}
