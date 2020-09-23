package com.zja.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @description 从jar包中读取配置文件信息
 * @author King_wangyao
 * @date 2012-12-02
 * @version 1.0.0
 */
public class JarReader {

	protected static final Log log = LogFactory.getLog(JarReader.class);
	
	/**
	 * 从jar包中读取配置文件信息
	 * @param jarPath jar目录路径
	 * @param configFileName 需要获取的文件目录结构
	 * @param propertiesName 需要得到的节点值
	 * @return
	 */
	public static String getConfigInfo(String jarPath, String configFileName, String propertiesName) {

		try {
			JarFile jarFile = new JarFile(jarPath);
			JarEntry entry = jarFile.getJarEntry(configFileName);
			InputStream input = jarFile.getInputStream(entry);
			Properties p = new Properties();
			p.load(input);
			Set<?> set = p.keySet();
			for (Object name : set) {
				if (StringUtils.isNotEmpty(((String) name).trim()) && StringUtils.isNotEmpty(propertiesName.trim()) && propertiesName.trim().equalsIgnoreCase(((String) name).trim())) {
					return p.getProperty((String) name);
				}
			}
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String jarPath = "F:\\ucm\\ucm_update\\com.uaes.ucm_1.0.19.jar";
		String configFileName = "META-INF/MANIFEST.MF";
		String propertiesName = "Bundle-Version";
		String value = JarReader.getConfigInfo(jarPath, configFileName, propertiesName);
		log.info("Bundle-Version = "+value);
	}
}
