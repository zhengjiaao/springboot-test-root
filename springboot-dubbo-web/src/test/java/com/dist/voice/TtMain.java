package com.dist.voice;

import com.dist.common.ConnUtil;
import com.dist.common.TokenHolder;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**百度语音合成并下载：解决不能合成1024字节以上的文档
 * 支持txt文件内容比较多的情况，利用分片解决
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp
 * Date: 2019-07-03 18:32
 */
public class TtMain {

    /**
     *  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
     */
    private final String appKey = "SDI4E7QfxrYmQpvYFTmC4eun";

    /**
     * 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
     */
    private final String secretKey = "552Mb6qjhefzWfCASGhyMmvpNhYAWesD";

    /**
     * 发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
     */
    private static final int per = 4;

    /**
     * 语速，取值0-15，默认为5中语速
     */
    private static final int spd = 7;

    /**
     * 音调，取值0-15，默认为5中语调
     */
    private static final int pit = 8;

    /**
     * 音量，取值0-9，默认为5中音量
     */
    private static final int vol = 5;

    /**
     * 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
     */
    private static final int aue = 6;

    /**
     * 可以使用https
     */
    public static final String url = "http://tsn.baidu.com/text2audio";

    /**AppID
     */
    private static String cuid = "16703212";

    @Test
    public void test() {
        String readFile = "D:\\VoiceTest\\say.txt";
        nextStr(readFile);
    }

    public String nextStr(String fileName) {
        String encoding = "gbk";
        File file = new File(fileName);

        byte[] filecontent = new byte[1024];
        String format = getFormat(aue);
        File outfile = new File("D:\\VoiceTest\\result." + format);
        try (FileInputStream in = new FileInputStream(file); FileOutputStream os = new FileOutputStream(outfile)) {
            for (; (in.read(filecontent)) != -1; ) {
                final String pie = new String(filecontent, encoding);
                byte[] bs = doprocess(pie);
                os.write(bs);
            }
            os.flush();
            System.out.println("文件生成位置： " + outfile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] doprocess(String pie) throws Exception {
        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(pie)) + ps;

        // 反馈请带上此url，浏览器上可以测试
        System.out.println(url + "?" + params);

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
            return bytes;
        }
        return null;
    }

    private String token = null;
    String ps = null;

    {
        try {
            TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
            holder.refresh();
            token = holder.getToken();
            StringBuilder sb = new StringBuilder();
            sb.append("&spd=" + spd)
                    .append("&pit=" + pit)
                    .append("&per=" + per)
                    .append("&vol=" + vol)
                    .append("&cuid=" + cuid)
                    .append("&tok=" + token)
                    .append("&aue=" + aue)
                    .append("&lan=zh&ctp=1");
            ps = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
