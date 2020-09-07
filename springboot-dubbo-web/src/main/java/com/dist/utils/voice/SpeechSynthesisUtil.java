package com.dist.utils.voice;

import com.dist.utils.voice.common.ConnUtil;
import com.dist.utils.voice.common.TokenHolder;
import com.dist.utils.voice.dto.SpeechSynthesisDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**百度语音合成工具类：支持txt内容比较多的情况，太大速度都会慢，利用分片请求最终合成语音文件
 * @author zhengja@dist.com.cn
 * @data 2019/7/4 9:29
 */
@Component
public class SpeechSynthesisUtil {

    public static void main(String[] args) {
        SpeechSynthesisDto speechSynthesisDto = new SpeechSynthesisDto();
        speechSynthesisDto.setPronunciationPerson(0); // 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
        speechSynthesisDto.setFileFormat(4); //文件格式 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
        speechSynthesisDto.setSpeechRate(5); //取值0-15，默认为7中语速
        speechSynthesisDto.setTone(5); //取值0-15，默认为5中语调
        speechSynthesisDto.setVolume(5); //取值0-9，默认为5中音量
        speechSynthesisDto.setFileName("需要点勇气"); //生成的文件名称
        speechSynthesisDto.setUploadFilePath("D:\\VoiceTest\\需要点勇气.txt"); //txt文件路径
        new SpeechSynthesisUtil().nextStr(speechSynthesisDto);
    }

    public String nextStr(SpeechSynthesisDto speechSynthesisDto) {
        String encoding = "gbk";
        File file = null;
        if (null != speechSynthesisDto.getUploadFilePath()){
            file = new File(speechSynthesisDto.getUploadFilePath());
        }else {
            return "UploadFilePath不能为null";
        }

        byte[] filecontent = new byte[1024];
        //获取下载格式
        String format = getFormat(speechSynthesisDto.getFileFormat());
        //File outfile = new File("D:\\VoiceTest\\result." + format);
        File outfile = new File(speechSynthesisDto.getFileName()+"."+ format);
        try (FileInputStream in = new FileInputStream(file); FileOutputStream os = new FileOutputStream(outfile)) {
            for (; (in.read(filecontent)) != -1; ) {
                final String oneSlice = new String(filecontent, encoding);
                byte[] bs = doprocess(oneSlice, speechSynthesisDto);
                os.write(bs);
            }
            os.flush();
            System.out.println("文件生成位置： " + outfile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] doprocess(String oneSlice,SpeechSynthesisDto speechSynthesisDto) throws Exception {
        final String urlParams = getUrlParams(speechSynthesisDto);
        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(oneSlice)) + urlParams;

        // 反馈请带上此url，浏览器上可以测试
        System.out.println("Url=="+ speechSynthesisDto.getUrl() + "?" + params);

        HttpURLConnection conn = (HttpURLConnection) new URL(speechSynthesisDto.getUrl()).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            return bytes;
        }
        return null;
    }

    private String getUrlParams(SpeechSynthesisDto speechSynthesisDto){
        try {
            TokenHolder holder = new TokenHolder(speechSynthesisDto.getAppKey(), speechSynthesisDto.getSecretKey(), TokenHolder.ASR_SCOPE);
            holder.refresh();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("&spd=" + speechSynthesisDto.getSpeechRate())
                    .append("&pit=" + speechSynthesisDto.getTone())
                    .append("&per=" + speechSynthesisDto.getPronunciationPerson())
                    .append("&vol=" + speechSynthesisDto.getVolume())
                    .append("&cuid=" + speechSynthesisDto.getAppId())
                    .append("&tok=" + holder.getToken())
                    .append("&aue=" + speechSynthesisDto.getFileFormat())
                    .append("&lan=zh&ctp=1");
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
     * @param aue
     * @return
     */
    private String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }

}
