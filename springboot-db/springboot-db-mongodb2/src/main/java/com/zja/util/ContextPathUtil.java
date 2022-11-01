package com.zja.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 项目地址工具
 */
public abstract class ContextPathUtil {

    /**
     * 获取上下文物理路径
     * @param relativePath
     * @return
     */
    public static String getContextPath(String relativePath,HttpServletRequest request){

        if (null == request) {
            return null;
        }
        String path = request.getServletContext().getRealPath(relativePath);
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }
}
