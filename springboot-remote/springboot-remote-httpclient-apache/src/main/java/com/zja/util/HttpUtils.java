/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-08 11:24
 * @Since:
 */
package com.zja.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * HttpClient工具类封装
 *
 * @author: zhengja
 * @since: 2023/09/08 11:24
 */
public class HttpUtils {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int CONNECT_TIME_OUT = 60000;
    private static final int SOCKET_TIME_OUT = 15000;

    private static final CloseableHttpClient HTTP_CLIENT;

    private HttpUtils() {
        // 私有构造函数，防止实例化
    }

    static {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(SOCKET_TIME_OUT)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setMaxTotal(100);
        HTTP_CLIENT = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(connectionManager)
                .build();
    }

    public static CloseableHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    public static String doGet(String url) throws IOException {
        return doGet(url, null, CHARSET);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, params, CHARSET);
    }

    public static String doPost(String url) throws IOException {
        return doPost(url, null, CHARSET);
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        return doPost(url, params, CHARSET);
    }

    public static String doPost(String url, String jsonBody) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        HttpPost httpPost = new HttpPost(url);
        //requestBody
        StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        return executeRequest(httpPost);
    }

    public static String doPut(String url, Map<String, String> params) throws IOException {
        return doPut(url, params, CHARSET);
    }

    public static String doDelete(String url) throws IOException {
        return doDelete(url, null, CHARSET);
    }

    public static String doDelete(String url, Map<String, String> params) throws IOException {
        return doDelete(url, params, CHARSET);
    }


    public static String doGet(String url, Map<String, String> params, Charset charset) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        List<NameValuePair> pairs = convertParamsToPairs(params);
        if (!pairs.isEmpty()) {
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpGet);
    }

    public static String doPost(String url, Map<String, String> params, Charset charset) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        HttpPost httpPost = new HttpPost(url);
        //requestForm
        List<NameValuePair> pairs = convertParamsToPairs(params);
        if (!pairs.isEmpty()) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
        }
        return executeRequest(httpPost);
    }

    public static String doPut(String url, Map<String, String> params, Charset charset) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        HttpPut httpPut = new HttpPut(url);
        List<NameValuePair> pairs = convertParamsToPairs(params);
        if (!pairs.isEmpty()) {
            httpPut.setEntity(new UrlEncodedFormEntity(pairs, charset));
        }
        return executeRequest(httpPut);
    }

    public static String doDelete(String url, Map<String, String> params, Charset charset) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        List<NameValuePair> pairs = convertParamsToPairs(params);
        if (!pairs.isEmpty()) {
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        HttpDelete httpDelete = new HttpDelete(url);
        return executeRequest(httpDelete);
    }

    //以下是 doPostUploadFile 上传文件方法
    public static String doPostUploadFile(String url, MultipartFile multipartFile) throws IOException {
        return doPostUploadFile(url, multipartFile, null);
    }

    public static String doPostUploadFile(String url, MultipartFile multipartFile, Map<String, String> params) throws IOException {
        return doPostUploadFile(url, multipartFile, null, params, CHARSET);
    }

    public static String doPostUploadFile(String url, MultipartFile multipartFile, Map<String, String> headers, Map<String, String> params) throws IOException {
        return doPostUploadFile(url, multipartFile, headers, params, CHARSET);
    }

    public static String doPostUploadFile(String url, MultipartFile multipartFile, Map<String, String> headers, Map<String, String> params, Charset charset) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty.");
        }
        if (multipartFile == null) {
            throw new IllegalArgumentException("MultipartFile cannot be null.");
        }

        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(charset);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        String fileName = multipartFile.getOriginalFilename();
        builder.addBinaryBody("file", multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);

        if (!CollectionUtils.isEmpty(params)) {
            ContentType contentType = ContentType.create("multipart/form-data", charset);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    builder.addTextBody(entry.getKey(), entry.getValue(), contentType);
                }
            }
        }
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);
        setHeaders(headers, httpPost);
        return executeRequest(httpPost);
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> files) throws IOException {
        return doPostUploadFiles(url, files, null);
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> files, Map<String, String> params) throws IOException {
        return doPostUploadFiles(url, files, params, CHARSET);
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> files, Map<String, String> params, Charset charset) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty.");
        }
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("List<MultipartFile> cannot be null.");
        }

        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(charset);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (MultipartFile file : files) {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                builder.addBinaryBody("files", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
            }
        }
        if (!CollectionUtils.isEmpty(params)) {
            ContentType contentType = ContentType.create("multipart/form-data", charset);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    builder.addTextBody(entry.getKey(), entry.getValue(), contentType);
                }
            }
        }
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);
        return executeRequest(httpPost);
    }

    //下载文件方法

    public static byte[] doGetDownloadFile(String url) throws IOException {
        return doGetDownloadFile(url, null);
    }

    public static byte[] doGetDownloadFile(String url, Map<String, String> params) throws IOException {
        return downloadFile(url, params, CHARSET, HttpGet.METHOD_NAME);
    }

    public static byte[] doPostDownloadFile(String url) throws IOException {
        return doPostDownloadFile(url, null);
    }

    public static byte[] doPostDownloadFile(String url, Map<String, String> params) throws IOException {
        return downloadFile(url, params, CHARSET, HttpPost.METHOD_NAME);
    }

    public static byte[] downloadFile(String url, Map<String, String> params, Charset charset, String method) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        List<NameValuePair> pairs = convertParamsToPairs(params);
        if (!pairs.isEmpty()) {
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        HttpUriRequest request;
        if (method.equalsIgnoreCase(HttpGet.METHOD_NAME)) {
            request = new HttpGet(url);
        } else if (method.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
            request = new HttpPost(url);
        } else {
            throw new IllegalArgumentException("Invalid HTTP method,Only supported GET、POST method.");
        }

        return executeRequest(request, entity -> {
            try {
                return EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //以下是 private 私有方法

    private static List<NameValuePair> convertParamsToPairs(Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        return pairs;
    }

    private static String executeRequest(HttpUriRequest request) throws IOException {
        try (CloseableHttpClient httpClient = getHttpClient();
             CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                request.abort();
                throw new IOException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            return result;
        }
    }

    private static <T> T executeRequest(HttpUriRequest request, Function<HttpEntity, T> responseHandler) throws IOException {
        try (CloseableHttpClient httpClient = getHttpClient();
             CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                request.abort();
                throw new IOException("HttpClient, error status code: " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            T result = null;
            if (entity != null) {
                result = responseHandler.apply(entity);
            }
            EntityUtils.consume(entity);
            return result;
        }
    }

    private static void setHeaders(Map<String, String> headers, HttpRequestBase httpMethod) {
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
