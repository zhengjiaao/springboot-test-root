/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-12-07 13:04
 * @Since:
 */
package com.zja.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("生成验证码格式")
public class PngCaptcha implements Serializable {
    @ApiModelProperty(value = "验证码随机字符长度 默 4 可选的")
    private int len = 4;
    @ApiModelProperty(value = "验证码显示宽度 默 130px 可选的")
    private int width = 130;
    @ApiModelProperty(value = "验证码显示高度 默 48px 可选的")
    private int height = 48;
    @ApiModelProperty(value = "验证码类型 默 2 , 其中 1-字母数字混合,2-纯数字,3-纯字母,4-纯大写字母,5-纯小写字母,6-数字大写字母 可选的")
    private int charType = 2;
}
