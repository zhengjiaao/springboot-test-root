package com.dist.base;

import java.io.File;

/**
 * 系统全局参数设置
 */
public final class GlobalSystemParameters {

	/**
	 * 系统的分隔符
	 */
	public final static String SEPARATOR = File.separator;


	/**
	 * HTTP请求的分隔符
	 */
	public final static String HTTP_SEPARATOR = "/";


	public final static String LOCAL_PATH = System.getProperty("user.dir");


	/**
	 * 临时文件目录名称，以"/"开始
	 */
	public final static String DIR_TEMP = "/temp";
	/**
	 * freemarker报告生成目录，以"/"开始
	 */
	public final static String DIR_FREEMARKER = "/fm";
	/**
	 * 缓存数据目录
	 */
	public final static String DIR_CACHEDATA = "/cache";
	
	/**
	 * 系统默认头像相对路径
	 */
	public static final String DEFAULT_AVATAR = "/avatar/default";
	/**
	 * 上传头像的默认相对路径
	 */
	public static final String UPLOAD_AVATAR = "/avatar";
	/**
	 * 上传临时文件的相对路径
	 */
	public static final String UPLOAD_FILE_TEMP = "/tempfile";
	/**
	 * 本地序列化路径
	 */
	public static final String SERIALIZABLE_PATH = "/temp/serializable";
	/**
	 * 区域序列化标识符guid
	 */
	public static final String SERIALIZABLE_REGION_GUID = "984A7B7E-762C-4C51-8634-29F6FCA09197";
	/**
	 * 标识ecm的目标存储库
	 */
	public static final String ECM_TOS = "ECM_TOS_";
	/**
	 * 标识ecm用户
	 */
	public static final String ECM_USERID = "ECM_USER_";
	/**
	 * 海报路径
	 */
	public static final String POSTER_PATH = "/poster";
	/**
	 * 默认海报路径
	 */
	public static final String DEFAULT_POSTER_PATH = "/poster/default";
	/**
	 * logo路径
	 */
	public static final String LOGO_PATH  = "/logo";
	/**
	 * 系统默认logo路径
	 */
	public static final String DEFAULT_LOGO_PATH = "/logo/default";
	/**
	 * 图片设置的相对路径
	 */
	public static final String IMG_PATH = "/img";
	/**
	 * 系统默认的图片路径
	 */
	public static final String DEFAULT_IMG_PATH = "/img/default";

}
