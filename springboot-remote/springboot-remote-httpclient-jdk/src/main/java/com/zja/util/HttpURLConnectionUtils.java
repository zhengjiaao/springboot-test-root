/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-08 16:18
 * @Since:
 */
package com.zja.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2023/09/08 16:18
 */
public class HttpURLConnectionUtils {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 60 * 1000;


    private HttpURLConnectionUtils() {
        // 私有构造函数，防止实例化
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        return executeRequest(url, "GET", params, null, CHARSET);
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        return executeRequest(url, "POST", params, null, CHARSET);
    }

    public static String doPost(String url, String jsonBody) throws IOException {
        return executeRequest(url, "POST", null, jsonBody, CHARSET);
    }

    public static String doPost(String url, Map<String, String> params, String jsonBody) throws IOException {
        return executeRequest(url, "POST", params, jsonBody, CHARSET);
    }

    public static String doPut(String url, Map<String, String> params) throws IOException {
        return executeRequest(url, "PUT", params, null, CHARSET);
    }

    public static String doDelete(String url, Map<String, String> params) throws IOException {
        return executeRequest(url, "DELETE", params, null, CHARSET);
    }


    //上传文件

    public static String doPostUploadFile(String url, MultipartFile multipartFile) throws IOException {
        return doPostUploadFile(url, multipartFile, null);
    }

    public static String doPostUploadFile(String url, MultipartFile multipartFile, Map<String, String> params) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty.");
        }
        if (multipartFile == null) {
            throw new IllegalArgumentException("MultipartFile cannot be null.");
        }

        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoOutput(true);

            String boundary = "===" + System.currentTimeMillis() + "===";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream outputStream = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream())) {

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(multipartFile.getOriginalFilename()).append("\"\r\n");
                writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(multipartFile.getOriginalFilename())).append("\r\n");
                writer.append("\r\n");
                writer.flush();

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                writer.append("\r\n");
                writer.flush();

                if (params != null && !params.isEmpty()) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        writer.append("--").append(boundary).append("\r\n");
                        writer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n");
                        writer.append("\r\n");
                        writer.append(entry.getValue()).append("\r\n");
                        writer.flush();
                    }
                }

                writer.append("--").append(boundary).append("--").append("\r\n");
                writer.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } else {
                throw new IOException("HttpURLConnection, HTTP POST request failed with response code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> multipartFiles) throws IOException {
        return doPostUploadFiles(url, multipartFiles, null);
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> multipartFiles, Map<String, String> params) throws IOException {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty.");
        }
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new IllegalArgumentException("MultipartFiles cannot be null or empty.");
        }

        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoOutput(true);

            String boundary = "===" + System.currentTimeMillis() + "===";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream outputStream = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true)) {

                for (MultipartFile multipartFile : multipartFiles) {
                    writer.append("--").append(boundary).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"files\"; filename=\"").append(multipartFile.getOriginalFilename()).append("\"\r\n");
                    writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(multipartFile.getOriginalFilename())).append("\r\n");
                    writer.append("\r\n");
                    writer.flush();

                    try (BufferedInputStream bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream())) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    writer.append("\r\n");
                    writer.flush();
                }

                if (params != null && !params.isEmpty()) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        writer.append("--").append(boundary).append("\r\n");
                        writer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n");
                        writer.append("\r\n");
                        writer.append(entry.getValue()).append("\r\n");
                        writer.flush();
                    }
                }

                writer.append("--").append(boundary).append("--").append("\r\n");
                writer.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } else {
                throw new IOException("HttpURLConnection, HTTP POST request failed with response code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    //下载文件流

    public static InputStream doGetDownloadFile(String url) throws IOException {
        return doDownloadStream(url, null, "GET");
    }

    public static InputStream doGetDownloadFile(String url, Map<String, String> params) throws IOException {
        return doDownloadStream(url, params, "GET");
    }

    public static InputStream doPostDownloadFile(String url) throws IOException {
        return doDownloadStream(url, null, "POST");
    }

    public static InputStream doPostDownloadFile(String url, Map<String, String> params) throws IOException {
        return doDownloadStream(url, params, "POST");
    }

    public static InputStream doDownloadStream(String url, Map<String, String> params, String requestMethod) throws IOException {
        String queryString = buildQueryString(params);
        if (!queryString.isEmpty()) {
            url += "?" + queryString;
        }

        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
//            connection.setReadTimeout(READ_TIMEOUT);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                return stream;
            } else {
                throw new IOException("HttpURLConnection, HTTP " + requestMethod + " request failed with response code: " + responseCode);
            }
        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//            if (stream != null) {
//                stream.close();
//            }
        }
    }


    //以下是 private 私有方法

    private static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            queryString.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        queryString.deleteCharAt(queryString.length() - 1);
        return queryString.toString();
    }

    private static String executeRequest(String url, String method, Map<String, String> params, String jsonBody, Charset charset) throws IOException {
        String queryString = buildQueryString(params);
        if (!queryString.isEmpty()) {
            url += "?" + queryString;
        }

        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            if (jsonBody != null && !jsonBody.isEmpty()) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] bodyBytes = jsonBody.getBytes(charset);
                    outputStream.write(bodyBytes);
                }
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } else {
                throw new IOException("HttpURLConnection, HTTP " + method + " request failed with response code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}