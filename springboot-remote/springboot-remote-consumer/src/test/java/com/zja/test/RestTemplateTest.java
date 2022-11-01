package com.zja.test;

import com.zja.util.RestTemplateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**RestTemplate 远程调用工具类-测试
 * @author zhengja@dist.com.cn
 * @data 2019/6/28 17:16
 */
public class RestTemplateTest {

    /**
     * 测试HTTPS请求访问博客园
     */
    @Test
    public void test() {
        String url = "https://www.cnblogs.com/{id}/p/{pageName}.html";
        String id = "jonban";
        List<String> pages = new ArrayList<>();
        pages.add("rest");
        pages.add("jsoup");
        pages.add("sms");
        pages.add("rememberMe");
        pages.add("properties");
        pages.add("async");

        for (String pageName : pages) {
            ResponseEntity<String> entity = RestTemplateUtils.get(url, String.class, id, pageName);
            System.out.println(entity.getStatusCode());
            System.out.println(entity.getBody());
        }

    }

    /**
     * 测试带请求头参数Headers的GET请求，POST类似
     */
    @Test
    public void testHeaders() {
        String url = "http://127.0.0.1:8080/test/Logan?age=16";
        Map<String, String> headers = new HashMap<>();
        headers.put("appId", "Basic MyAppId");
        ResponseEntity<String> entity = RestTemplateUtils.get(url, headers, String.class);
        System.out.println(entity.getStatusCode());
        System.out.println(entity.getBody());
    }

    /**
     * 测试普通表单参数的POST请求
     */
    @Test
    public void sayHello() {
        String url = "http://127.0.0.1:8080/test/sayHello";
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("name", "Logan");
        requestBody.add("age", 12);
        ResponseEntity<JSONObject> response = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    /**
     * 测试JSON格式请求体Body方式POST请求
     */
    @Test
    public void sayHelloBody() throws JSONException {
        String url = "http://127.0.0.1:8080/test/sayHelloBody";
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Logan");
        requestBody.put("age", 16);
        ResponseEntity<JSONObject> response = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    /**
     * 测试上传文件
     */
    @Test
    public void uploadFile() {
        String url = "http://127.0.0.1:8080/test/uploadFile";
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("uploadPath", "G:\\Temp\\Test");
        requestBody.add("file", new FileSystemResource("G:\\Java\\JavaStyle.xml"));
        requestBody.add("file2", new FileSystemResource("G:\\Java\\jad.exe"));

        ResponseEntity<JSONObject> response = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    public void downloadFile() {
        try {
            String url = "http://127.0.0.1:8080/test/downloadFile?filePath={filePath}&fileName={fileName}";

            String filePath = "G:\\Java";
            String fileName = "JavaStyle.xml";

            ResponseEntity<byte[]> response = RestTemplateUtils.get(url, byte[].class, filePath, fileName);
            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders().getContentType());

            // 如果返回时文本内容，则直接输出
            if ("text/html;charset=UTF-8".equals(response.getHeaders().getContentType().toString())) {
                System.out.println(new String(response.getBody(), "UTF-8"));
            }

            // 输出响应内容到本地文件
            else {
                File file = new File("G:\\Temp\\Test", fileName);
                if (HttpStatus.OK.equals(response.getStatusCode())) {
                    //FileUtils.writeByteArrayToFile(file, response.getBody());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试下载阿里巴巴的fastjson-1.2.56.jar
     */
    @Test
    public void downloadFile2() {
        try {
            String url = "http://central.maven.org/maven2/com/alibaba/fastjson/1.2.56/fastjson-1.2.56.jar";

            ResponseEntity<byte[]> response = RestTemplateUtils.get(url, byte[].class);
            System.out.println(response.getStatusCode());

            // 下载文件路径，可根据本地磁盘位置选择下载路径
            File file = new File("G:\\Temp\\Test\\fastjson-1.2.56.jar");
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                //FileUtils.writeByteArrayToFile(file, response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void tets4(){

        String s1 = "false";
        String s2 = "true";
        String s3 = "fAlSe";
        String s4 = "TrUe";
        String s5 = "true_a";

        System.out.println(Boolean.parseBoolean(s1));
        System.out.println(Boolean.parseBoolean(s2));
        System.out.println(Boolean.parseBoolean(s3));
        System.out.println(Boolean.parseBoolean(s4));
        System.out.println(Boolean.parseBoolean(s5));

    }

}
