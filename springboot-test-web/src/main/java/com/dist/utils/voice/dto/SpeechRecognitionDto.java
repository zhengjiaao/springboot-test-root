package com.dist.utils.voice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 语音转txt
 * 需要识别的文件，推荐使用 .pcm  仅支持语音文件在60s以内
 * @author zhengja@dist.com.cn
 * @data 2019/7/4 14:07
 */
@Data
@ApiModel(value = "百度语音识别")
public class SpeechRecognitionDto {

    /**AppID
     */
    @ApiModelProperty(value = "填写网页上申请的AppID",notes = "默认16703212")
    @JsonIgnore
    private final String cuId = "16703212";

    /**
     *  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
     */
    @ApiModelProperty(value = "申请的appkey",notes = "默认SDI4E7QfxrYmQpvYFTmC4eun")
    @JsonIgnore
    private  String appKey = "SDI4E7QfxrYmQpvYFTmC4eun";

    /**
     * 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
     */
    @ApiModelProperty(value = "申请的APP SECRET",notes = "默认552Mb6qjhefzWfCASGhyMmvpNhYAWesD")
    @JsonIgnore
    private  String secretKey = "552Mb6qjhefzWfCASGhyMmvpNhYAWesD";

    @ApiModelProperty(value = "采样率固定值")
    @JsonIgnore
    private final int rate = 16000;

    /**免费版 参数
     *可以改为https
     */
    @ApiModelProperty(value = "免费版 url")
    @JsonIgnore
    private final String url = "http://vop.baidu.com/server_api";

    @ApiModelProperty(value = "免费版 scope")
    @JsonIgnore
    private final String scope = "audio_voice_assistant_get";

    @ApiModelProperty(value = "fasle 默认以json方式上传音频文件")
    private boolean methodRaw = false;

    /**
     * 1537 表示识别普通话，使用输入法模型，有标点符号。1536表示识别普通话，使用搜索模型，没有符号。 其它语种参见文档
     */
    @ApiModelProperty(value = "dev_pid")
    private final int devPid = 1537;

    /**推荐使用 .pcm
     *private final String FILENAME = "16k_test.pcm";
     */
    @ApiModelProperty(value = "需要识别的文件,只支持pcm/wav/amr")
    private  String fileName;

    @ApiModelProperty(value = "文件格式, 只支持pcm/wav/amr")
    @JsonIgnore
    private  String format;



}
