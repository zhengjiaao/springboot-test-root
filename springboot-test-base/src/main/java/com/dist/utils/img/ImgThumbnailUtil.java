package com.dist.utils.img;

import com.dist.utils.file.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 图片缩略图工具
 * @author yinxp@dist.com.cn
 * @date 2019/5/23
 */
public abstract class ImgThumbnailUtil {

    private static Logger log = LoggerFactory.getLogger(ImgThumbnailUtil.class);

    private static final double DEFAULT_SCALE = 0.5d;
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HIGH = 600;

    private static final String DEFAULT_THUMBNAIL_SUFFIX = "_thumbnail";

    private static final boolean DEFAULT_KEEPASPECTRATIO = false;


    /**
     * 校验图片格式是否能够进行裁剪等操作
     * @param fileSrcPath
     * @return
     */
    private static boolean legalFormat(String fileSrcPath) {
        return true;
    }

    /**
     * 校验图片是否合法
     * @param fileSrcPath
     * @return
     */
    private static boolean legalFile(String fileSrcPath) {
        if (!FileUtil.legalFile(fileSrcPath)) {
            return false;
        }
        if (!legalFormat(fileSrcPath)) {
            return false;
        }
        return true;
    }

    /**
     * 根据相对比例缩放，
     * @param fileSrcPath   文件源路径
     * @return      缩略图路径
     */
    public static String zoomByScale(String fileSrcPath) throws IOException {
        File file = new File(fileSrcPath);
        String fileName = file.getName();
        String fileNameWithoutSuffix = FileUtil.delFileNameSuffix(fileName);
        String thumbnailFileName = fileNameWithoutSuffix + DEFAULT_THUMBNAIL_SUFFIX;
        return zoomByScale(fileSrcPath,thumbnailFileName,DEFAULT_SCALE);
    }

    /**
     * 根据相对比例缩放，
     * @param fileSrcPath   文件源路径
     * @param thumbnailFileName     生成缩略图文件名
     * @return      缩略图路径
     */
    public static String zoomByScale(String fileSrcPath,String thumbnailFileName) throws IOException {
        return zoomByScale(fileSrcPath,thumbnailFileName,DEFAULT_SCALE);
    }

    /**
     * 根据相对比例缩放，
     * @param fileSrcPath   文件源路径
     * @param thumbnailFileName     生成缩略图文件名
     * @param scale     宽/高比例
     * @return      缩略图路径
     */
    public static String zoomByScale(String fileSrcPath,String thumbnailFileName,double scale) throws IOException {
        if (!legalFile(fileSrcPath)) {
            throw new RuntimeException("源文件图片不正确，无法进行缩放");
        }
        File fileSrc = new File(fileSrcPath);
        File fileSrcParentFile = fileSrc.getParentFile();
        String absolutePath = fileSrcParentFile.getAbsolutePath();
        String fileTargetPath = absolutePath + File.separator + thumbnailFileName + "." + FileUtil.getFileSuffix(fileSrc);
        Thumbnails.of(fileSrc).scale(scale).toFile(new File(fileTargetPath));
        return fileTargetPath;
    }


    /**
     * 根据绝对大小缩放，
     * @param fileSrcPath  文件源路径
     * @return   缩略图路径
     */
    public static String zoomBySize(String fileSrcPath) throws IOException {
        File file = new File(fileSrcPath);
        String fileName = file.getName();
        String fileNameWithoutSuffix = FileUtil.delFileNameSuffix(fileName);
        String thumbnailFileName = fileNameWithoutSuffix + DEFAULT_THUMBNAIL_SUFFIX;
        return zoomBySize(fileSrcPath,thumbnailFileName,DEFAULT_WIDTH,DEFAULT_HIGH);
    }

    /**
     * 根据绝对大小缩放，
     * @param fileSrcPath  文件源路径
     * @param thumbnailFileName  生成缩略图文件名
     * @return   缩略图路径
     */
    public static String zoomBySize(String fileSrcPath,String thumbnailFileName) throws IOException {
        return zoomBySize(fileSrcPath,thumbnailFileName,DEFAULT_WIDTH,DEFAULT_HIGH);
    }

    /**
     * 根据绝对大小缩放，
     * @param fileSrcPath  文件源路径
     * @param thumbnailFileName  生成缩略图文件名
     * @param width   宽
     * @param high  高
     * @return   缩略图路径
     */
    public static String zoomBySize(String fileSrcPath,String thumbnailFileName,int width,int high) throws IOException {
        return zoomBySize(fileSrcPath,thumbnailFileName,width,high,DEFAULT_KEEPASPECTRATIO);
    }

    /**
     * 根据绝对大小缩放，
     * @param fileSrcPath  文件源路径
     * @param thumbnailFileName  生成缩略图文件名
     * @param width   宽
     * @param high  高
     * @param keepAspectRatio  是否按照比例缩放
     * @return   缩略图路径
     */
    public static String zoomBySize(String fileSrcPath,String thumbnailFileName,int width,int high,boolean keepAspectRatio) throws IOException {
        if (!legalFormat(fileSrcPath)) {
            throw new RuntimeException("源文件图片格式无法进行缩放");
        }
        File fileSrc = new File(fileSrcPath);
        File fileSrcParentFile = fileSrc.getParentFile();
        String absolutePath = fileSrcParentFile.getAbsolutePath();
        String fileTargetPath = absolutePath + File.separator + thumbnailFileName + "." + FileUtil.getFileSuffix(fileSrc);
        Thumbnails.of(fileSrc).size(width,high).keepAspectRatio(keepAspectRatio).toFile(new File(fileTargetPath));
        return fileTargetPath;
    }

}
