package com.dist.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 文件工具
 * @author xupp
 * @date 2018/12/21
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 复制单个文件
     *
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try ( InputStream inStream = new FileInputStream(oldPath);
              FileOutputStream fs = new FileOutputStream(newPath);){

            int byteread = 0;
            File oldfile = new File(oldPath);
            File newDir = new File(newPath.replace('\\', '/').substring(0,
                    newPath.replace('\\', '/').lastIndexOf('/')));
            if (!newDir.exists()) {
                newDir.mkdirs();
            }
            if (oldfile.exists()) { // 文件存在时
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
            }
            return true;
        } catch (Exception e) {
            logger.error("复制单个文件操作出错");
            return false;
        }
    }

}
