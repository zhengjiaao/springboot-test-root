package com.zja.utils;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ServletUtils {
	private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);
	
	
	public static  String getContextPath(){
		
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//这种方式获取的路径，其中的空格会被使用“%20”代替 替换为空格即可
		path=path.replaceAll("%20", " ");

	    File file = new File(path);
	    file = file.getParentFile().getParentFile();
	    
	    return file.getAbsolutePath();
	    
	}
	
	/**
	 * 设置让浏览器弹出下载对话框的Header,不同浏览器使用不同的编码方式.
	 * 
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
		final String CONTENT_DISPOSITION = "Content-Disposition";
		
		try {
			String agent = request.getHeader("User-Agent");
			String encodedfileName = null;
	        if (null != agent) {  
	        	agent = agent.toLowerCase();  
	            if (agent.contains("firefox") || agent.contains("chrome") || agent.contains("safari")) {  
	    			encodedfileName = "filename=\"" + new String(fileName.getBytes(), "ISO8859-1") + "\"";
	            } else if (agent.contains("msie")) {  
	            	encodedfileName = "filename=\"" + URLEncoder.encode(fileName,"UTF-8") + "\"";
	            } else if (agent.contains("opera")) {  
	            	encodedfileName = "filename*=UTF-8\"" + fileName + "\"";
	            } else {
	            	encodedfileName = "filename=\"" + URLEncoder.encode(fileName,"UTF-8") + "\"";
	            }
	        }
			
	        response.setHeader(CONTENT_DISPOSITION, "attachment; " + encodedfileName);
	        response.setContentType("application/octet-stream");
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param fileName
	 * @param filePath
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response,String fileName,String filePath,boolean delete){
		
		File file =new File(filePath);
		if(!file.exists()){
			logger.error("file not find ,the file is{}",filePath);
			return;
		}
		
		//设置文件头
		setFileDownloadHeader(request,response,fileName);
		//下载文件
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(response.getOutputStream());
			inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
			byte[] bts = new byte[4*1024];
			int i =-1;
			while((i = inputStream.read(bts) )>-1){
				outputStream.write(bts,0,i);
			}
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				try {
					if(outputStream != null){
						inputStream.close();
						outputStream.close();
					}
					if(inputStream != null){
					}
					if(delete){
						FileUtils.forceDelete(new File(filePath));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param fileName
	 * @param inputStream
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response,String fileName,InputStream inputStream){
		
		if(inputStream == null){
			logger.error("file not find ,the file is{}");
			return;
		}
		//设置文件头
		setFileDownloadHeader(request,response,fileName);
		//下载文件
		OutputStream outputStream = null;
		
		try {
			outputStream = new BufferedOutputStream(response.getOutputStream());
			byte[] bts = new byte[4*1024];
			int i =-1;
			while((i = inputStream.read(bts) )>-1){
				outputStream.write(bts,0,i);
			}
			outputStream.flush();
		} catch (IOException e) {
			logger.error("download file error {}",fileName,ExceptionUtils.getRootCauseMessage(e));
		}finally{
				try {
					if(outputStream != null){
						inputStream.close();
						outputStream.close();
					}
					if(inputStream != null){
					}
					 
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while ((paramNames != null) && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if ((values == null) || (values.length == 0)) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
	 * 
	 * @see #getParametersStartingWith
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		if ((params == null) || (params.size() == 0)) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}
	
	/**
	 * 输出 map 到 response
	 * @param response
	 * @param map
	 */
	public static void printObject(HttpServletResponse response,Map<String,Object> map){
		
		JSONObject json = JSONObject.fromObject(map);
		String jsonString = json.toString();
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(jsonString);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) out.close();
		}
	}
	
	/**
	 * 输出 json 到 response
	 * @param response
	 * @param json
	 */
	public static void printJson(HttpServletResponse response,JSONObject json){
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(json.toString());
			out.flush();
		} catch (IOException e) {
			logger.error("io exception",e);
		} finally {
			if(out != null) 
				out.close();
		}
	}
	
	/**
	 * 得到域名
	 * @param request
	 * @return
	 */
	public static String getHttpBaseDomain(HttpServletRequest request){
		
		String port = String.valueOf(request.getServerPort());
		if("80".equals(port)){
			return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
			
		}else{
			return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}
	}
}
