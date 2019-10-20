package com.dist.voice;

import com.dist.common.ConnUtil;
import com.dist.common.DemoException;
import com.dist.common.TokenHolder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 百度语音合成并下载：缺点不能合成1024字节以上的文档，适合少于1024字节的语音合成
 */
public class TtsMain {

    public static void main(String[] args) throws Exception{
        (new TtsMain()).run();
    }

    //  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
    private final String appKey = "SDI4E7QfxrYmQpvYFTmC4eun";

    // 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
    private final String secretKey = "552Mb6qjhefzWfCASGhyMmvpNhYAWesD";

    // text 的内容为"欢迎使用百度语音合成"的urlencode,utf-8 编码
    // 可以百度搜索"urlencode"
    private final String text = "《我们都是追梦人》\r\n" +
            "作曲 : 常石磊，\r\n" +
            "作词 : 王平久，编曲 : 柒玖、于昊，\r\n" +
            "每个身影 同阳光奔跑，\r\n" +
            "我们挥洒汗水 回眸微笑，\r\n" +
            "一起努力 争做春天的骄傲，\r\n" +
            "懂得了梦想，越追越有味道，\r\n" +
            "我们都是追梦人，千山万水 奔向天地跑道，\r\n" +
            "你追我赶 风起云涌春潮，海阔天空 敞开温暖怀抱，我们都是追梦人，在今天 勇敢向未来报到，\r\n" +
            "当明天 幸福向我们问好，最美的风景是拥抱，\r\n" +
            "啦……啦……啦……，\r\n" +
            "每次奋斗 拼来了荣耀，\r\n" +
            "我们乘风破浪 举目高眺，";

    // 发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
    private final int per = 4;
    // 语速，取值0-15，默认为5中语速
    private final int spd = 7;
    // 音调，取值0-15，默认为5中语调
    private final int pit = 8;
    // 音量，取值0-9，默认为5中音量
    private final int vol = 5;

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private final int aue = 6;

    public final String url = "http://tsn.baidu.com/text2audio"; // 可以使用https

    private String cuid = "16703212";

    //readFile是我们的txt文档，writeFile是输出的MP3格式
    public String readFile = "D:\\VoiceTest\\say.txt";

    private void run() throws Exception, DemoException {
        TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
        holder.refresh();
        String token = holder.getToken();

        //调用readToString方法将一个txt文档中的数据读取出来变成一个字符串
        String text2 = readToString(readFile);

        // 此处2次urlencode， 确保特殊字符被正确编码
        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(text));
        params += "&per=" + per;
        params += "&spd=" + spd;
        params += "&pit=" + pit;
        params += "&vol=" + vol;
        params += "&cuid=" + cuid;
        params += "&tok=" + token;
        params += "&aue=" + aue;
        params += "&lan=zh&ctp=1";
        System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.close();
        String contentType = conn.getContentType();
        if (contentType.contains("audio/")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            String format = getFormat(aue);
            File file = new File("result." + format); // 打开mp3文件即可播放
            // System.out.println( file.getAbsolutePath());
            FileOutputStream os = new FileOutputStream(file, true);
            os.write(bytes);
            os.close();
            System.out.println("audio file write to " + file.getAbsolutePath());
        } else {
            System.err.println("ERROR: content-type= " + contentType);
            String res = ConnUtil.getResponseString(conn);
            System.err.println(res);
        }
    }

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    private String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }

    //这个方法就是根据输入的文件路径，读取该文件内容返回一个很长的字符串，由于txt是gbk编码，所以我们变成字符串的时候也要用gbk
    //其实就是最基本的流操作
    public static String readToString(String fileName) {
        String encoding = "gbk";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
}