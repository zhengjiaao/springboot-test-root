package com.dist.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 * http请求的帮助类，用于获取参数
 * @author weifj
 * @version 1.0，2016/06/01，weifj，添加获取当前请求。
 */
@SuppressWarnings("rawtypes")
public class HttpRequestHelper {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestHelper.class);

	/**
	 * 从request中获取上传的文件。
	 * @author HeShun
	 * @param request 请求
	 * @return
	 * @throws FileUploadException
	 */
	public static FileItem getUploadFile(HttpServletRequest request) throws FileUploadException {

		int ourMaxMemorySize = Integer.MAX_VALUE;
		int ourMaxRequestSize = Integer.MAX_VALUE;

		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// The code below is directly taken from the jakarta fileupload
		// common classes
		// All informations, and download, available here :
		// http://jakarta.apache.org/commons/fileupload/
		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set factory constraints
		factory.setSizeThreshold(ourMaxMemorySize);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(ourMaxRequestSize);
		// Parse the request
		if (!request.getContentType().startsWith("multipart/form-data")) {
			throw new RuntimeException("不能解析上传的文件，请检查form是否设置【multipart/form-data】");
		} else {
			List /* FileItem */ items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			FileItem fileItem;
			while (iter.hasNext()) {
				fileItem = (FileItem) iter.next();
				//一个文件
				return fileItem;
			}
		}
		return null;
	}

	/**
	 * 获取当前请求，mvc
	 * @return
	 */
	public static HttpServletRequest getCurrentRequest() {

		logger.info(">>>获取当前请求......");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return request;

	}

	/**
	 * 获取上下文物理路径
	 * @return
	 */
	public static String getContextPath() {

		HttpServletRequest request = getCurrentRequest();
		if (null == request) {
			logger.warn("请求的request为空");
			return "";
		}
		return request.getServletContext().getRealPath("/");

	}

	/**
	 * 获取请求的父级URL
	 * @param request
	 * @return
	 */
	public static String getBaseURL(HttpServletRequest request) {
		if (null == request) {
			return "";
		}
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	/**
	 * 获取请求的父级URL
	 * @return
	 */
	public static String getBaseURL() {
		HttpServletRequest request = getCurrentRequest();
		if (null == request) {
			return "";
		}
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	/**
	 * 获取客户端IP地址  
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				//根据网卡取本机配置的IP   
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {

		String urlNameString = url + "?" + param;
		return sendGet(urlNameString);
	}

	/**
	 * @param url 完整的url
	 * @return 带空格分割的字符串
	 */
	public static String sendGet(String url) {

		StringBuilder buf = new StringBuilder();
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			/*	for (String key : map.keySet()) {
					System.out.println(key + "--->" + map.get(key));
				}*/
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				buf.append(line + " ");
			}
		} catch (Exception e) {
			logger.error(">>>发送GET请求出现异常：" + e.getMessage(), e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				logger.error(">>>" + ex.getMessage(), ex);
			}
		}
		return buf.toString().trim();
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，RequestBody
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(">>>发送POST请求出现异常：" + e.getMessage(), e);
		}
		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error(">>>" + ex.getMessage(), ex);
			}
		}
		return result;
	}

	/**
	* 向指定URL发送GET方法的请求
	* 
	* @param url
	*            发送请求的URL
	* @param param
	*            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	* @param logger 记录日志。
	* @return URL 所代表远程资源的响应结果
	*/
	public static String sendGet(String url, String param, Logger logger) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = null;
			if (param == null) {
				urlNameString = url;
			} else {
				urlNameString = url + "?" + param;
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(">>>发送GET请求出现异常：" + e.getMessage(), e);
			/*if (logger != null) {
				logger.error(String.format("Get url:[%s],param:[%s] failed。", url, param), e);
			}*/
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				logger.error(">>>" + ex.getMessage(), ex);
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，RequestBody
	 * @param logger 记录日志。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, Logger logger) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/json");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(">>>发送POST请求出现异常：" + e.getMessage(), e);
			/*if (logger != null) {
				logger.error(String.format("Post url:[%s],param:[%s] failed。", url, param), e);
			}*/
		}
		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error(">>>" + ex.getMessage(), ex);
			}
		}
		return result;
	}

	static HostnameVerifier hv = new HostnameVerifier() {
		@Override
		public boolean verify(String urlHostName, SSLSession session) {
			System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}
	};

	/**
	 * 创建一个支持https协议的客户端
	 * @return
	 * @throws Exception
	 */
	public static CloseableHttpClient createHttpsClient() throws Exception {
		X509TrustManager x509mgr = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] xcs, String string) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] xcs, String string) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { x509mgr }, new java.security.SecureRandom());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hv);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}
	/**
	 * 发送post请求，支持https协议
	 * @param url
	 * @param params
	 * @return
	 */
	public static String sendPost(String url, List<BasicNameValuePair> params) {
		
		CloseableHttpClient httpclient = null;
		try {
		httpclient = url.startsWith("https")? createHttpsClient() : HttpClients.createDefault();
		
		 HttpPost httpPost = new HttpPost(url);
		 httpPost.setEntity(new UrlEncodedFormEntity(params));
		  CloseableHttpResponse response = httpclient.execute(httpPost);
		  HttpEntity entity = response.getEntity();
		  
		  return  EntityUtils.toString(entity, "utf-8");
		  
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	/**
	 * 发送post请求，支持https协议
	 * @param url
	 * @param params
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> params) {

		CloseableHttpClient httpclient = null;
		try {
			List<BasicNameValuePair> basicNameValuePairList = new ArrayList<BasicNameValuePair>();
		
			Set<Entry<String, String>> set = params.entrySet();
			for(Entry entry : set) {
				basicNameValuePairList.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
			}

			httpclient = url.startsWith("https") ? createHttpsClient() : HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(basicNameValuePairList));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity, "utf-8");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}
