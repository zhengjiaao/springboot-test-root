package com.dist.constant;

/**
 * Created by Administrator on 2019/1/7.
 * 用于保存部署环境或者系统内的一些常量 比如操作系统 ，位数 等等
 * 使用这个是为了 避免需要用到的时候 出现冗余代码
 * @author  xupp
 * @date 2019/1/7
 */
public class EnvironmentContants {


    //操作系统名称 (小写)
    public static final String SYSTEM_OS_NAME = System.getProperty("os.name").toLowerCase();

    //操作系统位数
    public static final String SYSTEM_OS_ARCH = System.getProperty("os.arch").toLowerCase();


    //系统自带的一些属性
    public static final String SYSTEM_LINUX_TRANSSOFT_NAME="linux.trans.soft";

    public static final String SYSTEM_CAD_TRANS_EXEPATH ="cad.trans.exePath";
    public static final String SYSTEM_CAD_TRANS_IMGHEIGHT ="cad.trans.imgHeight";
    public static final String SYSTEM_CAD_TRANS_IMGWIDTH ="cad.trans.imgWidth";
    public static final String SYSTEM_CAD_TRANS_IMGSUFFIX ="cad.trans.imgSuffix";






}
