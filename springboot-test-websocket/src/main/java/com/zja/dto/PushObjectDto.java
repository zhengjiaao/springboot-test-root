package com.zja.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/5 15:25
 */
@ApiModel(value = "推送信息")
@Data
public class PushObjectDto {
    @ApiModelProperty(value = "推送人",required = true)
    private String pusher;
    @ApiModelProperty(value = "接收人",required = true)
    private String receiver;
    @ApiModelProperty(value = "推送的消息",required = true)
    private String pushMessage;
}
