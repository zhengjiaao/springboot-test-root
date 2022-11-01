package com.zja.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * 用ZIP压缩和解压缩文件或目录
 */
public class CompressUtil {

	private final static Log log = LogFactory.getLog(CompressUtil.class);

	/**
	 * 压缩文件或者目录
	 * @param baseDirName		压缩的根目录
	 * @param fileName			根目录下待压缩的文件或文件夹名，
	 * 星号*表示压缩根目录下的全部文件。
	 * @param targetFileName	目标ZIP文件
	 */
	public static void zipFile(String baseDirName, String targetFileName) {
		log.info("文件：" + baseDirName + "压缩到：" + targetFileName);
		//检测根目录是否存在
		if (baseDirName == null) {
			log.info("压缩失败，根目录不存在");
			return;
		}
		File file = new File(baseDirName);
		if (!file.exists()) {
			log.info("压缩失败，文件不存在：" + baseDirName);
			return;
		}

		String baseDirPath = file.getAbsolutePath();
		//目标文件
		File targetFile = new File(targetFileName);

		try {
			//创建一个zip输出流来压缩数据并写入到zip文件
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFile));
			if (file.isFile()) {
				CompressUtil.fileToZip(baseDirPath, file, out);
			} else {
				CompressUtil.dirToZip(baseDirPath, file, out);
			}
			out.close();
		} catch (IOException e) {
			log.info("压缩失败：" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
	 * @param zipFileName	待解压缩的ZIP文件名
	 * @param targetBaseDirName	目标目录
	 */
	public static void upzipFile(String zipFileName, String targetBaseDirName) {
		log.info("文件：" + zipFileName + "解压到：" + targetBaseDirName);
		if (!targetBaseDirName.endsWith(File.separator)) {
			targetBaseDirName += File.separator;
		}
		try {
			// 根据ZIP文件创建ZipFile对象
			ZipFile zipFile = new ZipFile(zipFileName, System.getProperty("sun.jnu.encoding"));
			ZipEntry entry = null;
			// 获取ZIP文件里所有的entry
			Enumeration<?> entrys = zipFile.getEntries();
			// 遍历所有entry
			while (entrys.hasMoreElements()) {
				entry = (ZipEntry) entrys.nextElement();
				// 获得entry的名字
				String targetFileName = targetBaseDirName + entry.getName();

				if (entry.isDirectory()) {
					// 如果entry是一个目录，则创建目录
					new File(targetFileName).mkdirs();
					continue;
				} else {
					// 如果entry是一个文件，则创建父目录
					new File(targetFileName).getParentFile().mkdirs();
				}
				OutputStream os = new FileOutputStream(new File(targetFileName));
				InputStream is = zipFile.getInputStream(entry);
				IOUtils.copy(is, os);
				// 关闭流
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(os);
			}
			if (zipFile != null) {
				zipFile.close();
			}
		} catch (IOException err) {
			err.printStackTrace();
			log.error("解压缩文件失败: " + err);
		}
	}

	/**
	 * 将目录压缩到ZIP输出流。
	 */
	private static void dirToZip(String baseDirPath, File dir, ZipOutputStream out) {
		if (dir.isDirectory()) {
			//列出dir目录下所有文件
			File[] files = dir.listFiles();
			// 如果是空文件夹
			if (files.length == 0) {
				ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));
				//	存储目录信息
				try {
					out.putNextEntry(entry);
					out.closeEntry();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			for (int i = 0 ; i< files.length ; i++) {
				File file = files[i];
				if (file.isFile()) {
					//如果是文件，调用fileToZip方法
					CompressUtil.fileToZip(baseDirPath, file, out);
				} else {
					//如果是目录，递归调用
					CompressUtil.dirToZip(baseDirPath, file, out);
				}
			}
		}
	}

	/**
	 * 将文件压缩到ZIP输出流
	 * 最后一个参数表示为是下载project，还是下载mpst
	 */
	private static void fileToZip(String baseDirPath, File file, ZipOutputStream out) {
		FileInputStream in = null;
		ZipEntry entry = null;
		//	创建复制缓冲区
		byte[] buffer = new byte[1024 * 5];
		int bytes_read;
		if (file.isFile()) {
			try {
				// 创建一个文件输入流
				in = new FileInputStream(file);
				//	做一个ZipEntry
				entry = new ZipEntry(getEntryName(baseDirPath, file));
				//	存储项信息到压缩文件
				out.putNextEntry(entry);
				//	复制字节到压缩文件
				while ((bytes_read = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytes_read);
				}
				out.closeEntry();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取待压缩文件在ZIP文件中entry的名字。即相对于跟目录的相对路径名
	 * @param baseDirPath
	 * @param file
	 * @return
	 */
	private static String getEntryName(String baseDirPath, File file) {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath += File.separator;
		}
		String filePath = file.getAbsolutePath();
		// 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储。
		if (file.isDirectory()) {
			filePath += "/";
		}
		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}
}
