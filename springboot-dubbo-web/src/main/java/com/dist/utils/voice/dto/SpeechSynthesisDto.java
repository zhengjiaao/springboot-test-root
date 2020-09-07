package com.dist.utils.voice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/** txt转语音
 * @author zhengja@dist.com.cn
 * @data 2019/7/4 9:30
 */
@ApiModel(value = "语音合成参数")
@Data
public class SpeechSynthesisDto implements Serializable {

    /**AppID
     */
    @ApiModelProperty(value = "填写网页上申请的AppID",notes = "默认16703212")
    @JsonIgnore
    private final String appId = "16703212";

    /**
     *  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
     */
    @ApiModelProperty(value = "申请的appkey",notes = "默认SDI4E7QfxrYmQpvYFTmC4eun")
    @JsonIgnore
    private final String appKey = "SDI4E7QfxrYmQpvYFTmC4eun";

    /**
     * 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
     */
    @ApiModelProperty(value = "申请的APP SECRET",notes = "默认552Mb6qjhefzWfCASGhyMmvpNhYAWesD")
    @JsonIgnore
    private final String secretKey = "552Mb6qjhefzWfCASGhyMmvpNhYAWesD";

    /**
     * 可以使用https
     */
    @ApiModelProperty(value = "默认http://tsn.baidu.com/text2audio",notes = "url")
    @JsonIgnore
    private final String url = "http://tsn.baidu.com/text2audio";

    /**
     * 发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
     */
    @ApiModelProperty(value = "0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为度丫丫",notes = "发音人选择")
    private int pronunciationPerson = 0;

    /**
     * 语速，取值0-15，默认为5中语速
     */
    @ApiModelProperty(value = "取值0-15，默认为7中语速",notes = "语速")
    private int speechRate = 7;

    /**
     * 音调，取值0-15，默认为5中语调
     */
    @ApiModelProperty(value = "取值0-15，默认为5中语调",notes = "音调")
    private int tone = 8;

    /**
     * 音量，取值0-9，默认为5中音量
     */
    @ApiModelProperty(value = "取值0-9，默认为5中音量",notes = "音量")
    private int volume = 5;

    /**
     * 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
     */
    @ApiModelProperty(value = "3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav",notes = "下载的文件格式")
    private int fileFormat = 6;

    /**
     * txt文件路径 例如：D:\\VoiceTest\\say.txt ,内容生成语音
     */
    @ApiModelProperty(value = "txt文件路径",required = true)
    private String uploadFilePath;

    /**
     * 要下载的文件名称
     */
    @ApiModelProperty(value = "生成的文件名称",notes = "默认voice")
    private String fileName="voice";
}
