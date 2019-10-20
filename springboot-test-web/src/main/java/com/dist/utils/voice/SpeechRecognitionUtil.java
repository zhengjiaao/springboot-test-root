package com.dist.utils.voice;

import com.dist.utils.voice.common.ConnUtil;
import com.dist.utils.voice.common.DemoException;
import com.dist.utils.voice.common.TokenHolder;
import com.dist.utils.voice.dto.SpeechRecognitionDto;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**百度语音识别工具类: 语音转文字 ,仅支持语音文件在60s以内
 * @author zhengja@dist.com.cn
 * @data 2019/7/4 11:35
 */
@Component
public class SpeechRecognitionUtil {

    /*
    //  免费版 参数
    {
        URL = "http://vop.baidu.com/server_api"; // 可以改为https
        //  1537 表示识别普通话，使用输入法模型，有标点符号。1536表示识别普通话，使用搜索模型，没有符号。 其它语种参见文档
        DEV_PID = 1537;
        SCOPE = "audio_voice_assistant_get";
    }*/

    /* 付费极速版 参数
    {
        URL =   "http://vop.baidu.com/pro_api"; // 可以改为https
        DEV_PID = 80001;
        SCOPE = "brain_enhanced_asr";
    }
    */

    /* 忽略scope检查，非常旧的应用可能没有
    {
        SCOPE = null;
    }
    */

    public static void main(String[] args) throws Exception {
        SpeechRecognitionUtil demo = new SpeechRecognitionUtil();
        SpeechRecognitionDto speechRecognitionDto = new SpeechRecognitionDto();
        speechRecognitionDto.setFileName("16k_test.pcm");
        // 填写下面信息
        String result = demo.run(speechRecognitionDto);
        System.out.println("识别结束：结果是：");
        System.out.println(result);

        // 如果显示乱码，请打开result.txt查看
        File file = new File("result.txt");
        FileWriter fo = new FileWriter(file);
        fo.write(result);
        fo.close();
        System.out.println("Result also wrote into " + file.getAbsolutePath());
    }


    public String run(SpeechRecognitionDto dto) throws Exception{
        if (dto.getFileName() != null){
            dto.setFormat(dto.getFileName().substring(dto.getFileName().length()-3));
        }
        TokenHolder holder = new TokenHolder(dto.getAppKey(), dto.getSecretKey(), dto.getScope());
        holder.refresh();
        String token = holder.getToken();
        String result = null;
        if (dto.isMethodRaw()) {
            result = runRawPostMethod(token,dto);
        } else {
            result = runJsonPostMethod(token,dto);
        }
        return result;
    }

    private String runRawPostMethod(String token,SpeechRecognitionDto dto) throws Exception{
        String url2 = dto.getUrl() + "?cuid=" + ConnUtil.urlEncode(dto.getCuId()) + "&dev_pid=" + dto.getDevPid() + "&token=" + token;
        String contentTypeStr = "audio/" + dto.getFormat() + "; rate=" + dto.getRate();
        //System.out.println(url2);
        byte[] content = getFileContent(dto.getFileName());
        HttpURLConnection conn = (HttpURLConnection) new URL(url2).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", contentTypeStr);
        //POST 方式（推荐）， 文本小于2048个中文字或者英文数字。
        //GET 方式，拼接后的url总长度不多于1000个字符，不推荐长文本合成使用。
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(content);
        conn.getOutputStream().close();
        System.out.println("url is " + url2);
        System.out.println("header is  " + "Content-Type :" + contentTypeStr);
        String result = ConnUtil.getResponseString(conn);
        return result;
    }

    public String runJsonPostMethod(String token,SpeechRecognitionDto dto) throws Exception {

        byte[] content = getFileContent(dto.getFileName());
        String speech = base64Encode(content);

        JSONObject params = new JSONObject();
        params.put("dev_pid", dto.getDevPid());
        params.put("format", dto.getFormat());
        params.put("rate", dto.getRate());
        params.put("token", token);
        params.put("cuid", dto.getCuId());
        params.put("channel", "1");
        params.put("len", content.length);
        params.put("speech", speech);

        // System.out.println(params.toString());
        HttpURLConnection conn = (HttpURLConnection) new URL(dto.getUrl()).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);
        conn.getOutputStream().write(params.toString().getBytes());
        conn.getOutputStream().close();
        String result = ConnUtil.getResponseString(conn);

        params.put("speech", "base64Encode(getFileContent(FILENAME))");
        System.out.println("url is : " + dto.getUrl());
        System.out.println("params is :" + params.toString());

        return result;
    }

    private byte[] getFileContent(String filename) throws Exception {
        File file = new File(filename);
        if (!file.canRead()) {
            System.err.println("文件不存在或者不可读: " + file.getAbsolutePath());
            throw new DemoException("file cannot read: " + file.getAbsolutePath());
        }
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return ConnUtil.getInputStreamContent(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String base64Encode(byte[] content) {

         Base64.Encoder encoder = Base64.getEncoder(); // JDK 1.8  推荐方法
         String str = encoder.encodeToString(content);

        /*char[] chars = Base64Util.encode(content); // 1.7 及以下，不推荐，请自行跟换相关库
        String str = new String(chars);*/

        return str;
    }
}
