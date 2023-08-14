package com.zja.utils;

import cn.hutool.crypto.digest.MD5;
import com.dist.zja.aspose.AsposeConvertToPDF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-02 10:50
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：文档转换预览
 */
@Slf4j
public class DocConversionPreviewUtil {

    /**
     * 预览前缀：例 http://127.0.0.1:8080/dgp-web/public/file/
     */
    private final String baseURL;

    /**
     * 本地存储绝对路径：例 D：\\Temp\\preview
     */
    private final String localFilePreviewPath;

    /**
     * 路径前缀：mg标识存储mongo文件
     */
    private static final String PATH_PREFIX = "preview";

    /**
     * 普通文档转换为 pdf
     * 支持： Word、Excel、Ppt、Cad、Picture、MultiplePictures等转换成 pdf预览
     */
    private final AsposeConvertToPDF asposeConvertToPDF;

    //----------------------------------------------bean 需要注入才能使用，此类的所有方法

    /**
     * 需要注入bean
     * @param baseURL                基础URL访问路径 例：http://127.0.0.1:80/public/file
     * @param localFilePath         本地存储路径    例: C:\\Temp\\storage
     * @param asposeConvertToPDF    文档转换器
     */
    public DocConversionPreviewUtil(String baseURL, String localFilePath, AsposeConvertToPDF asposeConvertToPDF) {
        checkBeanParams(localFilePath, baseURL);
        this.baseURL = baseURL + "/" + PATH_PREFIX;
        this.localFilePreviewPath = localFilePath + File.separator + PATH_PREFIX;
        this.asposeConvertToPDF = asposeConvertToPDF;
        initLocalFilePreviewPath();
    }


    //---------------------------------------------所有操作方法

    /**
     * 获取文件预览 URL
     * @param sourceFilePath 仅支持： Word、Excel、Ppt、Cad、Picture、MultiplePictures等类型转换成 pdf预览
     * @return 返回 URL
     * @throws IOException
     */
    public String getPdfPreviewURL(String sourceFilePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.error("源文件不存在, sourceFilePath：{}", sourceFilePath);
            throw new RuntimeException("源文件不存在！");
        }

        //计算出转换后的pdf缓存路径
        String fileName = sourceFile.getName();
        String md5 = md5Hex(sourceFile);
        String name = getfileName(fileName);
        String fileUniquePath = localUniqueId(md5) + File.separator + name + ".pdf";
        String pdfPath = localFilePreviewPath + File.separator + fileUniquePath;

        String previewURL = baseURL + "/" + localUniqueId(md5) + "/" + name + ".pdf";
        if (new File(pdfPath).exists()) {
            return previewURL;
        } else {
            toPdfBySourceFilePath(sourceFilePath, pdfPath);
        }
        return previewURL;
    }

    /**
     * 默认路径转换
     * @param sourceFilePath
     * @return
     * @throws IOException
     */
    public String toPdf(String sourceFilePath) throws IOException {
        return toPdf(sourceFilePath, null);
    }

    /**
     * 自定义路径存储转换
     * @param sourceFilePath
     * @param pdfFilePath
     * @return
     * @throws IOException
     */
    public String toPdf(String sourceFilePath, String pdfFilePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.error("源文件不存在, sourceFilePath：{}", sourceFilePath);
            throw new RuntimeException("源文件不存在！");
        }

        if (!StringUtils.isEmpty(pdfFilePath)) {
            return toPdfBySourceFilePath(sourceFilePath, pdfFilePath);
        }

        //计算出转换后的pdf缓存路径
        String fileName = sourceFile.getName();
        String md5 = md5Hex(sourceFile);
        String name = getfileName(fileName);
        String pdfPath = localFilePreviewPath + File.separator + localUniqueId(md5) + File.separator + name + ".pdf";
        return toPdfBySourceFilePath(sourceFilePath, pdfPath);
    }

    /**
     * 转为 pdf
     * @param sourceFilePath 仅支持： Word、Excel、Ppt、Cad、Picture、MultiplePictures等类型转换成 pdf预览
     * @param pdfFilePath pdf 文件路径
     * @return 返回 pdf 文件路径
     * @throws IOException
     */
    private String toPdfBySourceFilePath(String sourceFilePath, String pdfFilePath) throws IOException {

        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.error("源文件不存在, sourceFilePath：{}", sourceFilePath);
            throw new RuntimeException("源文件不存在！");
        }

        //判断pdf是否存在
        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
        if (!pdfFile.getParentFile().exists()) {
            pdfFile.getParentFile().mkdirs();
        }

        //按文件类型转换为 pdf
        String fileType = getFileType(sourceFilePath);
        if ("pdf".equals(fileType)) {
            Files.copy(sourceFile.toPath(), pdfFile.toPath());
        } else if (isWordType(fileType)) {
            asposeConvertToPDF.toPdfByWord(sourceFilePath, pdfFilePath);
        } else if (isExcelType(fileType)) {
            asposeConvertToPDF.toPdfByExcel(sourceFilePath, pdfFilePath, false);
        } else if (isSlidesType(fileType)) {
            asposeConvertToPDF.toPdfByPpt(sourceFilePath, pdfFilePath);
        } else if (isCADType(fileType)) {
            asposeConvertToPDF.toPdfByCad(sourceFilePath, pdfFilePath);
        } else if (isPicture(fileType)) {
            asposeConvertToPDF.toPdfByPicture(sourceFilePath, pdfFilePath);
        } else {
            log.error("不支持此类型文件转换为pdf , sourceFilePath：{}", sourceFilePath);
            throw new RuntimeException("不支持此类型文件转换为pdf");
        }
        return pdfFilePath;
    }

    /**
     * 清理本地所有缓存, 不会删除源文件
     * 删除 localFilePreviewPath 目录下的所有资源 例 C:\\Temp\\preview
     * @return true 清理成功
     */
    public boolean cleanLocalAllCache() {
        try {
            return deleteLocalDir(localFilePreviewPath);
        } catch (Exception e) {
            log.error("清理所有本地缓存失败:{}", e.getMessage());
        }
        return false;
    }


    //------------------------------------------- 内部方法

    /**
     * 删除目录（同时级联删除目录下所有资源文件）
     * @param localDirPath 本地目录路径 例：C:\\Temp\\mg
     * @return true 删除成功
     */
    private static boolean deleteLocalDir(String localDirPath) {
        File f = new File(localDirPath);
        if (!f.exists()) {
            return true;
        }
        if (f.isDirectory()) {
            File[] listFiles = f.listFiles();
            for (File fs : listFiles) {
                deleteLocalDir(fs.toString());
            }
        }
        return f.delete();
    }

    /**
     * 计算32位MD5摘要值，并转为16进制字符串
     *
     * @param file 被摘要文件
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(File file) {
        return new MD5().digestHex(file);
    }

    /**
     * 校验bean 注入的参数
     * @param localFilePath
     * @param baseURL
     */
    private static void checkBeanParams(String localFilePath, String baseURL) {
        if (StringUtils.isEmpty(localFilePath)) {
            throw new RuntimeException("[localFilePath] not is null！");
        }
        if (StringUtils.isEmpty(baseURL)) {
            throw new RuntimeException("[baseURL] not is null！");
        }
    }

    /**
     * 初始化 本地缓存预览路径
     */
    private void initLocalFilePreviewPath() {
        if (!new File(localFilePreviewPath).exists()) {
            try {
                Files.createDirectories(Paths.get(localFilePreviewPath));
            } catch (IOException e) {
                log.error("初始化Preview本地缓存路径失败：{}", localFilePreviewPath);
                e.printStackTrace();
            }
        }
    }

    /**
     * 本地缓存存储文件的唯一ID
     * @param path
     * @return 返回 本地缓存存储文件的唯一ID
     */
    private static String localUniqueId(String path) {
        //只保留后6位，避免名称相同的文件会覆盖
        String uniqueId = "";
        if (path.length() > 6) {
            uniqueId = path.substring(path.length() - 6);
        } else {
            uniqueId = path;
        }
        return uniqueId;
    }

    /**
     * 获取文件类型
     * @param fileName
     * @return 返回文件扩展名不带 “.”
     */
    private String getFileType(String fileName) {
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return suffixName;
    }

    /**
     * 获取文件名称   不带后缀
     * @param fileName
     * @return 例如 test
     */
    private static String getfileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    //判断是 doc、docx
    private boolean isWordType(String fileType) {
        if ("doc".equals(fileType) || "docx".equals(fileType)) {
            return true;
        }
        return false;
    }

    //判断是 xls、xlsx
    private boolean isExcelType(String fileType) {
        if ("xls".equals(fileType) || "xlsx".equals(fileType)) {
            return true;
        }
        return false;
    }

    //判断是 ppt/pptx
    private boolean isSlidesType(String fileType) {
        if ("ppt".equals(fileType) || "pptx".equals(fileType)) {
            return true;
        }
        return false;
    }

    //判断是 dwg/plt/dxf/dwf
    private boolean isCADType(String fileType) {
        if ("dwg".equals(fileType) || "plt".equals(fileType) || "dxf".equals(fileType) || "dwf".equals(fileType)) {
            return true;
        }
        return false;
    }

    //判断是 png、jpg
    private boolean isPicture(String fileType) {
        if ("png".equals(fileType) || "jpg".equals(fileType)) {
            return true;
        }
        return false;
    }
}
