package com.zja.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Locale;
import java.util.Map;

public class FreemarkerUtil {
	/**
	 * 获取模板目录
	 * @param ftlPath ftl文件名称
	 * @return
	 */
	public Template getTemplate(String ftlPath) {
		try {
			// 创建Configuration对象
			Configuration cfg = new Configuration();
			// 指定ftl模板文件目录
			cfg.setServletContextForTemplateLoading(ServletUtils.getContextPath(),"/WEB-INF/templates/");
			// 设置编码格式
			cfg.setEncoding(Locale.getDefault(), "utf-8");

			// 创建Template对象
			Template template = cfg.getTemplate(ftlPath);
			// 设置编码格式
			template.setEncoding("utf-8");
			return template;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过Template可以将模板文件输出到相应的流
	 * @param data 传送的数据
	 * @param ftlPath
	 */
	public void sprint(Map<String, ?> data, String ftlPath) {
		try {
			// 读取模板
			Template temp = this.getTemplate(ftlPath);
			// 准备输出
			temp.process(data, new PrintWriter(System.out));
		} catch (TemplateException e) {
			System.err.println(FreemarkerUtil.class.getCanonicalName() + "：处理Template模版中出现错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(FreemarkerUtil.class.getCanonicalName() + "：输出文件流出现错误");
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过一个文件输出流，就可以写到相应的文件中
	 * @param data 传送的数据
	 * @param ftlPath ftl路径
	 * @param htmlPath html文件名称
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void fprint(Map<String, ?> data, String ftlPath, String htmlPath) {
		Template temp = this.getTemplate(ftlPath);
		// 生成静态页面
		String path = ServletUtils.getContextPath();
		File htmlFile = new File(path + htmlPath);

		// 创建存储目录
		if (!htmlFile.exists()) {
			String dir = htmlFile.getAbsolutePath();
			dir = dir.substring(0, dir.lastIndexOf(File.separator) > -1 ? dir.lastIndexOf(File.separator) : dir.length());
			new File(dir).mkdirs();
		}

		// 将数据写到静态页面
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));
			try {
				temp.process(data, out);
			} catch (Exception e) {
				System.err.println(FreemarkerUtil.class.getCanonicalName() + "：处理Template模版中出现错误");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println(FreemarkerUtil.class.getCanonicalName() + "：生成静态文件出现错误");
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
