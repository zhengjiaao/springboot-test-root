/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-02 10:45
 * @Since:
 */
package com.zja.apache.commons;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/08/02 10:45
 */
public class FileuploadTests {

    /**
     * 创建 MultipartFile
     */
    @Test
    public void multipartFile_test() throws IOException {
        //temp.zip 文件大小超过 2G
        InputStream input = Files.newInputStream(Paths.get("D:\\temp.zip"));

        // MockMultipartFile 底层 ToByteArray() 内存溢出，设置VM也不能够解决
//        MultipartFile file = new MockMultipartFile("a.zip", "a.zip", MediaType.MULTIPART_FORM_DATA_VALUE, input.);

        FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, "temp1.zip");
        IOUtils.copy(input, fileItem.getOutputStream());
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        System.out.println(multipartFile.getOriginalFilename());

        System.out.println("成功");
    }


}
