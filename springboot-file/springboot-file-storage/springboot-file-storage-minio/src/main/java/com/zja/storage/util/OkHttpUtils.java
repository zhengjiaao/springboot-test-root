package com.zja.storage.util;

import okhttp3.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhengja
 * @since: 2023/09/11 10:38
 */
public class OkHttpUtils {
    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final String MEDIA_TYPE = "application/json; charset=utf-8";


    private static OkHttpClient client;

    // 构建客户端
    public static OkHttpClient buildClient() {
        if (client == null) {
            synchronized (OkHttpUtils.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return client;
    }

    public static String doGet(String url) throws IOException {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        OkHttpClient client = buildClient();
        String newUrl = buildUrlWithParams(url, params);
        Request request = new Request.Builder()
                .url(newUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String doPost(String url, String jsonBody) throws IOException {
        return doPost(url, jsonBody, null);
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        return doPost(url, null, params);
    }

    public static String doPost(String url, String jsonBody, Map<String, String> params) throws IOException {
        OkHttpClient client = buildClient();
        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody requestBody = RequestBody.create(mediaType, jsonBody != null ? jsonBody : "");
        String newUrl = buildUrlWithParams(url, params);
        Request request = new Request.Builder()
                .url(newUrl)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String doPut(String url, String jsonBody) throws IOException {
        return doPut(url, jsonBody, null);
    }

    public static String doPut(String url, Map<String, String> params) throws IOException {
        return doPut(url, null, params);
    }

    public static String doPut(String url, String jsonBody, Map<String, String> params) throws IOException {
        OkHttpClient client = buildClient();
        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody requestBody = RequestBody.create(mediaType, jsonBody != null ? jsonBody : "");
        String newUrl = buildUrlWithParams(url, params);
        Request request = new Request.Builder()
                .url(newUrl)
                .put(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String doDelete(String url) throws IOException {
        return doDelete(url, null);
    }

    public static String doDelete(String url, Map<String, String> params) throws IOException {
        OkHttpClient client = buildClient();
        String newUrl = buildUrlWithParams(url, params);
        Request request = new Request.Builder()
                .url(newUrl)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String doPostUploadFile(String url, MultipartFile multipartFile) throws IOException {
        return doPostUploadFile(url, multipartFile, null);
    }

    public static String doPostUploadFile(String url, MultipartFile multipartFile, Map<String, String> params) throws IOException {
        return doUploadFile(url, "POST", multipartFile, params);
    }

    public static String doPutUploadFile(String url, MultipartFile multipartFile) throws IOException {
        return doPutUploadFile(url, multipartFile, null);
    }

    public static String doPutUploadFile(String url, MultipartFile multipartFile, Map<String, String> params) throws IOException {
        return doUploadFile(url, "PUT", multipartFile, params);
    }

    private static String doUploadFile(String url, String requestType, MultipartFile multipartFile, Map<String, String> params) throws IOException {
        if (multipartFile == null) {
            throw new IllegalArgumentException("MultipartFile cannot be null.");
        }

        OkHttpClient client = buildClient();
        String newUrl = buildUrlWithParams(url, params);

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", multipartFile.getOriginalFilename(), RequestBody.create(mediaType, multipartFile.getBytes()))
                .build();

        Request request = null;
        if ("PUT".equalsIgnoreCase(requestType)) {
            request = new Request.Builder()
                    .url(newUrl)
                    .put(requestBody)
                    .build();
        } else if ("POST".equalsIgnoreCase(requestType)) {
            request = new Request.Builder()
                    .url(newUrl)
                    .post(requestBody)
                    .build();
        } else {
            throw new IllegalArgumentException("requestType must be POST or PUT.");
        }

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> multipartFiles) throws IOException {
        return doPostUploadFiles(url, multipartFiles, null);
    }

    public static String doPostUploadFiles(String url, List<MultipartFile> multipartFiles, Map<String, String> params) throws IOException {
        return doUploadFiles(url, "POST", multipartFiles, params);
    }

    public static String doPutUploadFiles(String url, List<MultipartFile> multipartFiles) throws IOException {
        return doPutUploadFiles(url, multipartFiles, null);
    }

    public static String doPutUploadFiles(String url, List<MultipartFile> multipartFiles, Map<String, String> params) throws IOException {
        return doUploadFiles(url, "PUT", multipartFiles, params);
    }

    private static String doUploadFiles(String url, String requestType, List<MultipartFile> multipartFiles, Map<String, String> params) throws IOException {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new IllegalArgumentException("MultipartFiles cannot be null or empty.");
        }

        OkHttpClient client = buildClient();
        String newUrl = buildUrlWithParams(url, params);

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 添加文件参数
        for (MultipartFile file : multipartFiles) {
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody fileRequestBody = RequestBody.create(mediaType, file.getBytes());
            requestBodyBuilder.addFormDataPart("files", file.getOriginalFilename(), fileRequestBody);
        }

        RequestBody requestBody = requestBodyBuilder.build();

        Request request = null;
        if ("PUT".equalsIgnoreCase(requestType)) {
            request = new Request.Builder()
                    .url(newUrl)
                    .put(requestBody)
                    .build();
        } else if ("POST".equalsIgnoreCase(requestType)) {
            request = new Request.Builder()
                    .url(newUrl)
                    .post(requestBody)
                    .build();
        } else {
            throw new IllegalArgumentException("requestType must be POST or PUT.");
        }

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void doGetDownloadFile(String url, String savePath) throws IOException {
        OkHttpClient client = buildClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                try (InputStream inputStream = body.byteStream();
                     FileOutputStream fileOutputStream = new FileOutputStream(new File(savePath))) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }

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
        OkHttpClient client = buildClient();
        String newUrl = buildUrlWithParams(url, params);
        Request.Builder requestBuilder = new Request.Builder()
                .url(newUrl);
        if (requestMethod.equalsIgnoreCase("GET")) {
            requestBuilder.get();
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            requestBuilder.post(RequestBody.create(null, new byte[0]));
        } else {
            throw new RuntimeException("Unsupported request type [" + requestMethod + "].");
        }
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body != null) {
            return body.byteStream();
        }
        return null;
    }

    private static String buildUrlWithParams(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("URL not is null.");
        }
        if (params == null || params.isEmpty()) {
            return url;
        }

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return urlBuilder.build().toString();
    }
}
