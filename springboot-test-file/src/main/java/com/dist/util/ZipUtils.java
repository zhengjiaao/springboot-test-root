package com.dist.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/10 11:14
 */
public class ZipUtils {

    private static final String ZIPFILE_PREFIX = ".zip";

    /**
     * 文件进行压缩
     * @param srcDir 源文件路径
     * @param out    zip文件输出流
     * @param KeepDirStructure  是否保留原有文件路结构 true保留/false所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException{
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[10*1024*1024];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }

                }
            }
        }
    }


    /**压缩源文件夹并删除源文件夹
     *
     * 注意：1、源文件夹下不能有子文件夹，会拒绝访问； 2、不支持压缩单个文件
     *
     * @param zipFolderPath    //文件夹路径
     * @param deleteSourceFile  true删除源文件/false不删除源文件
     * @return    压缩包本地路径
     * @throws Exception
     */
    public static String zipModelMaterial(String zipFolderPath,boolean deleteSourceFile) throws Exception {
        String zipFileAbsolutePath = zipFolderPath + ZIPFILE_PREFIX;
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileAbsolutePath));
        File file = new File(zipFolderPath);
        //file.deleteOnExit();
        File[] files = file.listFiles();
        for (File f : files) {
            FileInputStream zis = new FileInputStream(f);
            ZipEntry zipEntry = new ZipEntry(f.getName());
            zos.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = zis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            zis.close();
            f.delete();
        }
        zos.closeEntry();
        zos.close();
        if (deleteSourceFile){
            //删除压缩路径
            new File(zipFolderPath).delete();
        }
        return zipFileAbsolutePath;
    }

    public static void main(String[] args) throws Exception {
        //压缩源文件夹，并是否删除源文件夹
        zipModelMaterial("D:\\FileTest\\文件分片位置\\107M视频\\0_107M视频.wmv",false);
    }

}
