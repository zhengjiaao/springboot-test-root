package com.zja.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩算法
 * 
 * @author fangkeliu
 * @since 2016-08-12
 * @version 1.0.0
 */
public class Compression {

	private final static String UTF_8 = "utf-8";
	private final static String COMPRESS_CODE = "ISO-8859-1";

	// 压缩
	// public static String compress(String str) throws IOException {
	// if (str == null || str.length() == 0) {
	// return StringUtils.EMPTY;
	// }
	// ByteArrayOutputStream out = new ByteArrayOutputStream();
	// GZIPOutputStream gzip = new GZIPOutputStream(out);
	// gzip.write(str.getBytes());
	// gzip.close();
	//
	// return out.toString(UTF_8);
	// }

	// 解压缩
	// public static String uncompress(String str) throws IOException {
	// if (str == null || str.length() == 0) {
	// return str;
	// }
	// ByteArrayOutputStream out = new ByteArrayOutputStream();
	// ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
	// GZIPInputStream gunzip = new GZIPInputStream(in);
	// byte[] buffer = new byte[256];
	// int n;
	// while ((n = gunzip.read(buffer)) >= 0) {
	// out.write(buffer, 0, n);
	// }
	// return out.toString();
	// }

	/**
	 * 压缩
	 * 
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static String compress(String param) throws IOException {
		if (param == null || param.length() == 0) {
			return null;
		}
		ByteArrayOutputStream byteOut = null;
		GZIPOutputStream gzipOut = null;
		byte[] outPut = null;
		try {
			// 开启数据输出流,关闭无效
			byteOut = new ByteArrayOutputStream();
			// 开启数据压缩流
			gzipOut = new GZIPOutputStream(byteOut);
			// 将字串转换成字节，然后按照ＵＴＦ－８的形式压缩
			gzipOut.write(param.getBytes(UTF_8));
			// 压缩完毕
			gzipOut.finish();
			gzipOut.close();
			// 将压缩好的流转换到byte数组中去
			outPut = byteOut.toByteArray();
			byteOut.flush();
			byteOut.close();
		} finally {
			if (byteOut != null) {
				byteOut.close();
			}
		}
		return new String(outPut, COMPRESS_CODE);
	}

	/**
	 * 解压
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String uncompress(String str) throws IOException {
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream gzip = null;
		byte[] param = str.getBytes(COMPRESS_CODE);
		byte[] b = null;
		try {
			// 创建输出流
			out = new ByteArrayOutputStream();
			// 创建输入流,并把传入的字串参数转码成ISO-8895-1
			in = new ByteArrayInputStream(param);
			// 创建压缩输入流，将大小默认为参数输入流大小
			gzip = new GZIPInputStream(in);
			// 创建byte数组用于接收解压后的流转化成byte数组
			byte[] byteArry = new byte[256];
			int n = -1;
			while ((n = gzip.read(byteArry)) != -1) {
				out.write(byteArry, 0, n);
			}
			// 转换数据
			b = out.toByteArray();
			out.flush();
		} finally {
			// 关闭压缩流资源
			if (out != null)
				out.close();
			if (gzip != null)
				gzip.close();
			if (in != null)
				in.close();
		}
		return new String(b, UTF_8);
	}

}
