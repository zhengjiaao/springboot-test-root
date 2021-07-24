package com.zja.utils;

import com.zja.BaseTests;
import com.zja.utils.id.IdUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-22 10:08
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class MongoFileUtilTests extends BaseTests {

    @Resource
    private MongoFileUtil mongoFileUtil;

    @Test
    public void test() {

        //上传mongo id
        String mgdbId = IdUtil.mgdbId();
        printlnAnyObj("mgdbId", mgdbId);

        //上传本地文件到mongo
        mongoFileUtil.uploadByFile(mgdbId, new File("D:\\Temp\\image\\3840x2160.jpg"));

        //检测文件是否存在
        boolean existFile = mongoFileUtil.existFileByPath(mgdbId);
        printlnAnyObj("existFile", existFile);

        //获取文件名称
        String fileName = mongoFileUtil.getFileName(mgdbId);
        printlnAnyObj("fileName", fileName);

        //获取文件预览路径
        String fileURL = mongoFileUtil.getFileURL("60e2c08768125f9b8c65ded5");
        printlnAnyObj("fileURL", fileURL);

        //下载文件到本地
        mongoFileUtil.downloadFileByPath(mgdbId, "D:\\Temp\\image\\c.jpg");

        //删除文件
        boolean deleteFile = mongoFileUtil.deleteFile(mgdbId);
        printlnAnyObj("deleteFile", deleteFile);

        //确认文件是否被删除
        boolean existFile2 = mongoFileUtil.existFileByPath(mgdbId);
        printlnAnyObj("existFile2", existFile2);
    }


    @Test
    public void getUrlTest() throws InterruptedException {
//        printlnAnyObj(ConfigConstants.file.proxyBaseURL());
        Thread.sleep(5000);
    }


 /*   public static void main(String[] args) {

        Map map=new HashMap();
        map.put("a",1);
        map.put("b",2);
        map.put("c",3);

        Map map2=new HashMap();
        map2.put("a",1);
        map2.put("b",2);
        map2.put("c",3);

        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        printlnAnyObj("map",map);
        printlnAnyObj("map2",map2);
        printlnAnyObj("list",list);
    }*/
}
