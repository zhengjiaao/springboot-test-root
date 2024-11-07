package com.zja.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author: zhengja
 * @since: 2024/04/24 13:11
 */
public class MultipartFileUtil {

    private MultipartFileUtil() {
    }

    public static boolean isEmpty(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    public static boolean isNotEmpty(MultipartFile file) {
        return !isEmpty(file);
    }

    public static void toFile(MultipartFile file, String filePath) throws IOException {
        file.transferTo(new File(filePath));
    }

    public static void toFile(MultipartFile file, Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath);
    }

    public static void toFile(MultipartFile multipartFile, File file) throws IOException {
        multipartFile.transferTo(file);
    }

    public static MultipartFile toMultipartFile(File file) throws IOException {
        return toMultipartFile(file.getAbsolutePath(), "file");
    }

    public static MultipartFile toMultipartFile(String filePath) throws IOException {
        return toMultipartFile(filePath, "file");
    }

    public static MultipartFile toMultipartFile(Path filePath) throws IOException {
        return toMultipartFile(filePath.toString(), "file");
    }

    private MultipartFile toMultipartFile(InputStream is, String fileName) throws IOException {
        FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        IOUtils.copy(is, fileItem.getOutputStream());
        return new CommonsMultipartFile(fileItem);
    }

    public static MultipartFile toMultipartFile(String filePath, String fieldName) throws IOException {
        try {
            File file = new File(filePath);
            FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
            try (FileInputStream is = new FileInputStream(file)) {
                IOUtils.copy(is, fileItem.getOutputStream());
            }
            return new CommonsMultipartFile(fileItem);
        } catch (IOException e) {
            throw new IOException("转换失败 File --> MultipartFile", e);
        }
    }

    public static MultipartFile[] toMultipartFiles(File[] files) throws IOException {
        MultipartFile[] multipartFiles = new MultipartFile[files.length];
        for (int i = 0; i < files.length; i++) {
            multipartFiles[i] = toMultipartFile(files[i]);
        }
        return multipartFiles;
    }

    public static void main(String[] args) throws IOException {
        // 转为 MultipartFile
        MultipartFile multipartFile = toMultipartFile(new File("D:\\temp\\excel\\input.xlsx.pdf"));

        // 转为 File
        toFile(multipartFile, new File("D:\\temp\\excel\\input.xlsx2.pdf"));
    }
}
