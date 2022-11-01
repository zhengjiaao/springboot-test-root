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
        boolean existFile = mongoFileUtil.existFile(mgdbId);
        printlnAnyObj("existFile", existFile);

        //获取文件名称
        String fileName = mongoFileUtil.getFileName(mgdbId);
        printlnAnyObj("fileName", fileName);

        //下载文件到本地
        String downloadFile = mongoFileUtil.downloadFile(mgdbId);
        printlnAnyObj("downloadFile", downloadFile);

        //获取文件预览路径
        String fileURL = mongoFileUtil.getFileURL(mgdbId);
        printlnAnyObj("fileURL", fileURL);

        //删除文件
        boolean deleteFile = mongoFileUtil.deleteFile(mgdbId);
        printlnAnyObj("deleteFile", deleteFile);

        //确认文件是否被删除
        boolean existFile2 = mongoFileUtil.existFile(mgdbId);
        printlnAnyObj("existFile2", existFile2);
    }

    @Test
    public void uploadByFileTest() {
        //上传mongo id
//        String mgdbId = IdUtil.mgdbId();
        String mgdbId = "123";
        printlnAnyObj("mgdbId", mgdbId);
        mongoFileUtil.uploadByFile(mgdbId, new File("D:\\Temp\\image\\3840x2160.jpg"));
    }

    @Test
    public void existFileTest() {
        boolean existFile = mongoFileUtil.existFile("123");
        printlnAnyObj("existFile", existFile);
    }

    @Test
    public void getFileNameTest(){
        String fileName = mongoFileUtil.getFileName("123");
        printlnAnyObj("fileName", fileName);
    }

    @Test
    public void downloadFileTest() {
        String downloadFile = mongoFileUtil.downloadFile("123");
        printlnAnyObj("downloadFile", downloadFile);
    }

    @Test
    public void downloadFile2Test() {
        String downloadFile = mongoFileUtil.downloadFile("123","2.jpg");
        printlnAnyObj("downloadFile2", downloadFile);
    }

    @Test
    public void downloadFile3Test() {
        String downloadFile = mongoFileUtil.downloadFile("123","D:\\Temp\\storage","3.jpg");
        printlnAnyObj("downloadFile3", downloadFile);
    }

    @Test
    public void getFileURLTest() {
        String fileURL = mongoFileUtil.getFileURL("123");
        printlnAnyObj("fileURL", fileURL);
    }

    @Test
    public void deleteFileTest() {
        boolean deleteFile = mongoFileUtil.deleteFile("123");
        printlnAnyObj("deleteFile", deleteFile);
    }

    @Test
    public void cleanLocalCacheTest() {
        boolean cleanLocalCache = mongoFileUtil.cleanLocalCache("123");
        printlnAnyObj("cleanLocalCache", cleanLocalCache);
    }

    @Test
    public void cleanLocalAllCacheTest() {
        boolean cleanLocalAllCache = mongoFileUtil.cleanLocalAllCache();
        printlnAnyObj("cleanLocalAllCache", cleanLocalAllCache);
    }

}
