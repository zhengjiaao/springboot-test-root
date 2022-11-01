package com.zja.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * 删除文件或目录
 */
public class DeleteFileUtil {

	private final static Log log = LogFactory.getLog(DeleteFileUtil.class);

	/**
	 * 删除文件，可以是单个文件或文件夹
	 *
	 * @param filePath 待删除的文件路径
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean delete(String filePath) {
		try {
			if (filePath != null && StringUtils.isNotEmpty(filePath)) {
				File file = new File(filePath);
				if (file.exists()) {
					if (file.isFile()) {
						return deleteFile(filePath);
					} else {
						return deleteDirectory(filePath);
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除失败：" + filePath);
		}
		return false;
	}

	/**
	 * 删除单个文件
	 *
	 * @param filePath 待删除文件的文件路径
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	private static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		// 如果文件路径对应的文件存在，并且是一个文件，则直接删除。
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				log.error("删除文件失败：" + file.getPath());
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件，只删除文件夹
	 * @param filePath 待删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	private static boolean deleteDirectory(String filePath) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符。
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		File dirFile = new File(filePath);
		// 如果dir对应的文件不存在，或者不是一个文件夹，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			log.error("删除目录失败：" + filePath + "目录不存在！");
			deleteFile(filePath);
			return false;
		}
		boolean flag = true;
		// 删除文件夹下所有文件（包括子目录）
		File[] files = dirFile.listFiles();
		for (File file : files) {
			if (file.isFile()) {// 删除子文件
				flag = deleteFile(file.getAbsolutePath());
				if (!flag) {
					break;
				}
			} else if (file.isDirectory()) {// 删除子目录
				flag = deleteDirectory(file.getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			log.error("删除目录失败：" + dirFile.getPath());
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件，如果是文件并且只有一个，则把这个目录删除
	 * @param filePath
	 */
	public static void removeDirectory(String filePath) {
		if (StringUtils.isNotBlank(filePath)) {
			File file = new File(filePath);
			// 目录下只有一个文件，则把这个目录删除
			if (file != null && file.isFile()) {
				if (file.getParentFile().listFiles() != null && file.getParentFile().listFiles().length <= 1) {
					filePath = file.getParent();
				}
			}
		}
		delete(filePath);
	}
}
