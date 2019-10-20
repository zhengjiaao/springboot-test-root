package com.dist.voice;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.junit.Test;

import java.io.*;

/**语音测试: 简单语音合成技术，没有Ai情感；对中英文混合支持不太好听
 * 优点：支持大型txt文件合成语音文件
 * @author zhengja@dist.com.cn
 * @data 2019/7/3 13:23
 */
public class VoiceTest {

    /**
     * 一个很简单的java代码实现，运行之后就会读出来了
     */
    @Test
    public void readVoice(){
            //用电脑自带的语音读字符串str
            String str = "你好，我是java小新人！请叫我最帅的帅锅";

            ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
            Dispatch sapo = sap.getObject();
            try {
                // 音量 0-100
                sap.setProperty("Volume", new Variant(100));
                // 语音朗读速度 -10 到 +10
                sap.setProperty("Rate", new Variant(0));
                // 执行朗读
                Dispatch.call(sapo, "Speak", new Variant(str));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sapo.safeRelease();
                sap.safeRelease();
            }
    }

    /**
     * 输出音频文件
     */
    @Test
    public void voiceDownload(){
        //指定文件音频输出文件位置
        String output = "D:\\VoiceTest\\test.wav";

        String readFile="D:\\VoiceTest\\say.txt";
        ActiveXComponent ax = null;

        //调用readToString方法将一个txt文档中的数据读取出来变成一个字符串
        String string = readToString(readFile);
        String str="我是java小新人，我要将这段话的音频输出一下";
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");

            //运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            // 音量 0-100
            ax.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            ax.setProperty("Rate", new Variant(-3));

            // 进行朗读
            //Dispatch.call(spVoice, "Speak", new Variant(string));

            //下面是构建文件流把生成语音文件

            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();

            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            //设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            //设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            //调用输出 文件流打开方法，在指定位置输出一个.wav文件
            Dispatch.call(spFileStream, "Open", new Variant(output), new Variant(3), new Variant(true));
            //设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            //设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            //设置朗读速度
            //Dispatch.put(spVoice, "Rate", new Variant(-2));
            Dispatch.put(spVoice, "Rate", new Variant(3));
            //开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(string));

            //关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            ax.safeRelease();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
