package com.dist.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * List<T>集合克隆工具
 */
public class CollectionUtil {

	private static Logger logger = LoggerFactory.getLogger(CollectionUtil.class);

	private CollectionUtil() {
		super();
	}

	/**
	 *对list进行深拷贝 需要集合中的元素可以被序列化
	 * @param src
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> deepCopy(List<T> src) {
		Assert.notNull(src, "深复制参数不能为空");
		if (!(src instanceof Serializable)) {
			return Collections.emptyList();
		}
		ByteArrayOutputStream byteOut = null;
		ObjectOutputStream out = null;
		ByteArrayInputStream byteIn = null;
		ObjectInputStream in = null;
		try {
			byteOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			in = new ObjectInputStream(byteIn);
			return (List<T>) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			logger.error(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (byteIn != null) {
				try {
					byteIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (byteOut != null) {
				try {
					byteOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Collections.emptyList();
	}

}
