package com.zja.bigFile;

import com.zja.FilePartsUploadApplication;
import com.zja.model.BigFileSplit;
import com.zja.util.BigFileSplitUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: zhengja
 * @since: 2019/9/11 9:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FilePartsUploadApplication.class})
public class BigFileTest {

    @Autowired
    private BigFileSplitUtil bigFileSplitUtil;

    @Test
    public void test() {

        String filePathName= "/107M视频.wmv";

        BigFileSplit split = new BigFileSplit();
        //文件路径
        split.setFilePath(filePathName);
        //每个文件切片后的存储位置，动态的->就是源文件的名称
        split.setDestPath(getFileName(filePathName));
        //每个分块的大小 自定义 20M
        split.setBlockSize(20 * 1024 * 1024);

        long begin = System.currentTimeMillis();
        //源文件分片，返回分片后信息
        BigFileSplit bigFileSplit = bigFileSplitUtil.split(split);

        long end = System.currentTimeMillis();
        System.out.println("分片用时= " + (end - begin));

        //分片后的返回信息
        System.out.println("分片后的返回信息： " + bigFileSplit.toString());


        long begin2 = System.currentTimeMillis();
        //切片文件合成
        bigFileSplitUtil.fileConsolidated("107M视频.wmv", bigFileSplit.getSize(), bigFileSplit.getDestFileNames());

        long end2 = System.currentTimeMillis();
        System.out.println("合成用时= " + (end2 - begin2));

    }

    /**
     * 仅保留文件名不保留后缀
     */
    public static String getFileName(String pathandname) {
        pathandname = pathandname.replace("\\", "/").replace("//", "/");
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        }
        return null;
    }

}
