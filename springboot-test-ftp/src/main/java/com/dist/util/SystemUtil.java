package com.dist.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Scanner;

public final class SystemUtil/* extends BaseController*/ {

	private static Logger log = LoggerFactory.getLogger(SystemUtil.class);

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
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newDir = new File(newPath.replace('\\', '/').substring(0,
					newPath.replace('\\', '/').lastIndexOf("/")));
			if (!newDir.exists()) {
				newDir.mkdirs();
			}
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				// int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.flush();
				fs.close();
			}
			return true;
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * 将文件流转化为本地文件
	 * @param inputStream
	 * @param localFilePath
	 * @return boolean
	 * @throws IOException 
	 */
	public static boolean inputStreamToFile(InputStream inputStream,String localFilePath) throws IOException {
		localFilePath = localFilePath.replace("\\", "/");
		File newDir = new File(localFilePath.substring(0, localFilePath.lastIndexOf("/")));
		if (!newDir.exists()) {
			newDir.mkdirs();
		}
		FileOutputStream fs= new FileOutputStream(localFilePath);
		try {
			/*FileChannel fc= ((FileInputStream) inputStream).getChannel();  
            log.info("文件大小:"+fc.size());*/
			int bytesum = 0;
			int byteread = 0;
			
			byte[] buffer = new byte[1024];
			long startTime = System.currentTimeMillis();
			while ((byteread = inputStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				fs.write(buffer, 0, byteread);
			}
			fs.flush();
			long endTime = System.currentTimeMillis();
			log.info("将文件流转化为本地文件耗时/毫秒:"+(endTime-startTime));
			return true;
		} catch (Exception e) {
			log.error("将文件流转化为本地文件失败");
			e.printStackTrace();
			return false;
		}finally{
			fs.close();
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 通过CPU序列号，生成机器码
	 * 
	 * @return
	 */
	public static String getMachineCode() {

		try {
			Process process = Runtime.getRuntime().exec(
					new String[] { "wmic", "cpu", "get", "ProcessorId" });
			process.getOutputStream().close();
			Scanner sc = new Scanner(process.getInputStream());
			String property = sc.next();
			String serial = sc.next();
			System.out.println(property + ": " + serial);

			return serial;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

	}

	/**
	 * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
	 * @param value
	 * @return Sting
	 */
	public static String formatFloatNumber(double value) {
		if (value != 0.00) {
			java.text.DecimalFormat df = new java.text.DecimalFormat(
					"########.00");
			return df.format(value);
		} else {
			return "0.00";
		}

	}

}
