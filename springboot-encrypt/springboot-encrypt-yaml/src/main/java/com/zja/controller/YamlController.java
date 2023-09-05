/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-05 10:26
 * @Since:
 */
package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * yaml加密测试
 *
 * @author: zhengja
 * @since: 2023/09/05 10:26
 */
@Validated
@RestController
@RequestMapping("/rest/yaml")
@Api(tags = {"yaml加密测试页面"})
public class YamlController {

    //加密前
    @Value("${before_encryption}")
    private String beforeEncryption;

    //加密后
    @Value("${after_encryption}")
    private String afterEncryption;

    @GetMapping("/query/v1")
    @ApiOperation("加密前-明文")
    public String queryV1() {
        return beforeEncryption;
    }

    @GetMapping("/query/v2")
    @ApiOperation("加密后-密文")
    public String queryV2() {
        return beforeEncryption;
    }

}