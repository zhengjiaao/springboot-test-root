package com.zja.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * 创建文件和目录
 */
public class CreateFileUtil {

	protected static final Log log = LogFactory.getLog(CreateFileUtil.class);

	/**
	 * 创建单个文件
	 * 
	 * @param fileName 目标文件名
	 * @return 创建成功，返回true，否则返回false
	 */
	public static boolean createFile(String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				return false;
			}
			if (fileName.endsWith(File.separator)) {
				log.error("创建文件失败：" + fileName + "目标文件不能为目录！");
				return false;
			}
			// 判断目标文件所在的目录是否存在
			if (!file.getParentFile().exists()) {
				// 如果目标文件所在的文件夹不存在，则创建父文件夹
				if (!file.getParentFile().mkdirs()) {
					log.error("创建目标文件所在的目录失败！");
					return false;
				}
			}
			// 创建目标文件
			if (file.createNewFile()) {
				return true;
			} else {
				log.error("创建文件失败：" + fileName);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("创建文件失败：" + fileName);
			return false;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param dirName 目标目录名
	 * @return 目录创建成功放回true，否则返回false
	 */
	public static boolean createDir(String dirName) {
		try {
			File dir = new File(dirName);
			if (dir.exists()) {
				return true;
			}
			if (!dirName.endsWith(File.separator)) {
				dirName = dirName + File.separator;
			}
			// 创建目标目录
			if (dir.mkdirs()) {
				return true;
			} else {
				log.error("创建目录失败：" + dirName);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("创建目录失败：" + dirName);
			return false;
		}
	}
}
