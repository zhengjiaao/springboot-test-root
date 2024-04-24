package com.zja.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

/**
 * @author: zhengja
 * @since: 2024/04/24 13:11
 */
public class MultipartFileUtil {

    private MultipartFileUtil() {
    }

    public static void convertMultipartFileToFile(MultipartFile multipartFile, File file) throws IOException {
        multipartFile.transferTo(file);
    }

    public static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        return convertFileToMultipartFile(file.getAbsolutePath(), "file");
    }

    public static MultipartFile convertFileToMultipartFile(String filePath, String fieldName) throws IOException {
        File file = new File(filePath);
        FileItem fileItem = new DiskFileItemFactory().createItem(fieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
        try (FileInputStream is = new FileInputStream(file)) {
            IOUtils.copy(is, fileItem.getOutputStream());
        }
        return new CommonsMultipartFile(fileItem);
    }

    public static MultipartFile[] convertFilesToMultipartFiles(File[] files) throws IOException {
        MultipartFile[] multipartFiles = new MultipartFile[files.length];
        for (int i = 0; i < files.length; i++) {
            multipartFiles[i] = convertFileToMultipartFile(files[i]);
        }
        return multipartFiles;
    }


    public static void main(String[] args) throws IOException {
        // 转为 MultipartFile
        MultipartFile multipartFile = convertFileToMultipartFile(new File("D:\\temp\\excel\\input.xlsx.pdf"));

        // 转为 File
        convertMultipartFileToFile(multipartFile, new File("D:\\temp\\excel\\input.xlsx2.pdf"));
    }
}
