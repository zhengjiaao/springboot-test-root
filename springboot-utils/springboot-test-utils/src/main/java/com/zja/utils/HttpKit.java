package com.zja.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

/**
 * apache http Method
 *
 * @author fangkeliu
 *
 */
public class HttpKit {

	private final static Log logger = LogFactory.getLog(HttpKit.class);

	/**
	 * 默认的编码格式
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * time out 时间
	 */
	private static final int HTTP_CONNECTION_TIMEOUT = 10000;

	/**
	 * http连接超时 单位 ms
	 */
//	private static final int HTTP_SOCKET_TIMEOUT = 10000;
	private static final int HTTP_SOCKET_TIMEOUT = 120000;

	private static BasicCookieStore cookieStore = new BasicCookieStore();

	private static String userSessionId = StringUtils.EMPTY;

	/**
	 * 发送Get 请求
	 *
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		logger.info("url=" + url + ",param=" + params + ",header=" + headers);

		CloseableHttpClient httpClient = getHttpClient();

		RequestBuilder builder = RequestBuilder.get(url);
		if (params != null && !params.isEmpty()) {
			// 有值
			Set<String> keys = params.keySet();
			for (String key : keys) {
				builder.addParameter(key, params.get(key));
			}
		}

		if (headers != null && !headers.isEmpty()) {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				builder.addHeader(key, params.get(key));
			}
		}

		//加用户验证信息
		addUserSessionId(builder);

		CloseableHttpResponse response = null;
		try {
			builder.setCharset(CharsetUtils.get(DEFAULT_CHARSET));
			RequestConfig config = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIMEOUT).setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
			builder.setConfig(config);

			response = httpClient.execute(builder.build());
			handlerStatus(response);

			HttpEntity entity = response.getEntity();
			showCookie();

			return EntityUtils.toString(entity, DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new Exception("http get fail -->"+ ExceptionUtils.getMessage(e),e);
		} finally {
			close(httpClient, response);
		}
	}

	private static void addUserSessionId(RequestBuilder builder) {
		if(StringUtils.isNotBlank(userSessionId)){
			logger.debug("user session id" + userSessionId);
			builder.addHeader("sessionId", userSessionId);
		}
	}

	/**
	 * 简单的GET请求
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		try {
			return get(url, null);
		} catch (Exception e) {
			throw new Exception("http get fail", e);
		}
	}

	/**
	 * 不带Head 的GET请求
	 *
	 * @return 返回类型:
	 * @throws Exception
	 * @description 功能描述: get 请求
	 */
	public static String get(String url, Map<String, String> params) throws Exception {
		try {
			return get(url, params, null);
		} catch (Exception e) {
			throw new Exception("http get fail", e);
		}
	}

	/**
	 * 发送请求
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String post(String url) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		return post(url, params);
	}

	/**
	 * @return 返回类型:
	 * @throws Exception
	 * @description 功能描述: POST 请求
	 */
	public static String post(String url, Map<String, String> params) throws Exception {
		logger.info("url=" + url + ",param=" + params);

		CloseableHttpClient httpClient = getHttpClient();

		RequestBuilder builder = RequestBuilder.post(url);
		if (params != null && !params.isEmpty()) {
			// 有值
			Set<String> keys = params.keySet();
			for (String key : keys) {
				builder.addParameter(key, params.get(key));
			}
		}
		//加用户验证信息
		addUserSessionId(builder);

		CloseableHttpResponse response = null;
		try {
			builder.setCharset(CharsetUtils.get(DEFAULT_CHARSET));
			RequestConfig config = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIMEOUT).setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
			builder.setConfig(config);

			response = httpClient.execute(builder.build());
			handlerStatus(response);
			logger.debug("http post rsult :" + response.getStatusLine().getStatusCode());

			HttpEntity entity = response.getEntity();
			showCookie();
			return EntityUtils.toString(entity, DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new Exception("http post fail -->"+ ExceptionUtils.getMessage(e),e);
		} finally {
			close(httpClient, response);
		}
	}

	/**
	 * @return 返回类型:
	 * @throws Exception
	 * @description 功能描述: POST 请求
	 */
	public static String postJSON(String url, JSONObject json) throws Exception {

		if (url == null || json == null) {
			logger.error("url is empty");
			throw new IllegalArgumentException("url or json can not be null");
		}

		Map<String, String> params = new HashMap<String, String>();

		Iterator<String> ite = json.keySet().iterator();
		String key = StringUtils.EMPTY;
		while (ite.hasNext()) {
			key = ite.next();
			Object obj = json.get(key);
			if (obj instanceof JSONArray) {
				params.put(key, String.valueOf(obj));
			} else {
				params.put(key, String.valueOf(obj));
			}
		}
		return post(url, params);
	}

	/**
	 * Status 进行处理
	 *
	 * @param response
	 * @throws Exception
	 */
	private static void handlerStatus(CloseableHttpResponse response) throws Exception {
		int statusCode = response.getStatusLine().getStatusCode();
		logger.debug("http get result :" + statusCode);
		if (statusCode == 200) {
			logger.debug("response success, ok!");
		}else if (statusCode == 301) {
			logger.error("user not has ppermission");
		} else if (statusCode == 401) {
			throw new Exception("user is not login");
		} else {
			try {
				String content = EntityUtils.toString(response.getEntity());
				//报500错误的时候，控制台，可以看到错误信息
				logger.info(content);
				logger.debug("error page --> " + content);
			} catch (Exception e) {
				logger.debug("http error " + ExceptionUtils.getRootCauseMessage(e));
			}

			throw new Exception(String.valueOf(statusCode));
		}
	}

	/**
	 * 上传媒体文件
	 *
	 * @param url
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String upload(String url, File file) throws Exception {

		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		try {
			FileBody fileBody = new FileBody(file);
			HttpEntity entity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addPart("files", fileBody).setCharset(CharsetUtils.get(DEFAULT_CHARSET)).build();

			RequestBuilder builder = RequestBuilder.post(url);
			builder.setEntity(entity);

			builder.setCharset(CharsetUtils.get(DEFAULT_CHARSET));
			RequestConfig config = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIMEOUT).setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
			builder.setConfig(config);

			//加用户验证信息
			addUserSessionId(builder);

			response = httpClient.execute(builder.build());

			logger.info("http post rsult :" + response.getStatusLine().getStatusCode());

			HttpEntity responseEntity = response.getEntity();
			showCookie();
			return EntityUtils.toString(responseEntity, DEFAULT_CHARSET);

		} catch (Exception e) {
			throw new Exception("http upload fail", e);
		} finally {
			close(httpClient, response);
		}
	}

	/**
	 * 下载文件
	 *
	 * @param url
	 * @param saveFilePath
	 * @throws Exception
	 */
	public static void download(String url, String saveFilePath) throws Exception {

		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;

		OutputStream out = null;
		InputStream in = null;
		try {
			RequestBuilder builder = RequestBuilder.get(url).setCharset(CharsetUtils.get(DEFAULT_CHARSET));
			//加用户验证信息
			addUserSessionId(builder);

			response = httpClient.execute(builder.build());

			handlerStatus(response);
			HttpEntity entity = response.getEntity();

			File file = new File(saveFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			in = entity.getContent();
			out = new FileOutputStream(file);

			byte[] buffer = new byte[4096];
			int readLength = 0;
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}

			out.flush();
		} catch (Exception e) {
			throw new Exception("http download fail", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			close(httpClient, response);
		}
	}

	/**
	 * 下载文件
	 * @param url
	 * @param saveFilePath
	 * @param params
	 * @throws Exception
	 */
	public static void download(String url, String saveFilePath,Map<String, String> params) throws Exception {

		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;

		OutputStream out = null;
		InputStream in = null;
		try {
			RequestBuilder builder = RequestBuilder.get(url).setCharset(CharsetUtils.get(DEFAULT_CHARSET));
			if (params != null && !params.isEmpty()) {
				// 有值
				Set<String> keys = params.keySet();
				for (String key : keys) {
					builder.addParameter(key, params.get(key));
				}
			}
			//加用户验证信息
			addUserSessionId(builder);

			response = httpClient.execute(builder.build());

			handlerStatus(response);
			HttpEntity entity = response.getEntity();

			File file = new File(saveFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			in = entity.getContent();
			out = new FileOutputStream(file);

			byte[] buffer = new byte[4096];
			int readLength = 0;
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}

			out.flush();
		} catch (Exception e) {
			throw new Exception("http download fail", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			close(httpClient, response);
		}
	}

	/**
	 * 关闭连接
	 *
	 * @param httpClient
	 * @param response
	 */
	private static void close(CloseableHttpClient httpClient, CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		return httpClient;
	}

	private static void showCookie() {
		List<Cookie> cookies = cookieStore.getCookies();
		if (cookies.isEmpty()) {
			logger.info("http get not has cookie");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				logger.debug("- " + cookies.get(i).toString());
			}
		}
	}

	public static void setUserSessionId(String userSessionId) {
		HttpKit.userSessionId = userSessionId;
	}
}
