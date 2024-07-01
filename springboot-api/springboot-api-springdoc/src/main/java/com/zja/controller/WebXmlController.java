/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-13 16:29
 * @Since:
 */
package com.zja.controller;

import com.zja.dto.UserXmlDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @Api("提供远程-XML测试接口")
@Tag(name = "提供远程-XML测试接口", description = "测试示例")
@RestController
@RequestMapping
public class WebXmlController {

    // @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @Operation(summary = "get-无参数", description = "返回xml数据")
    @GetMapping(value = "/xml/get/user", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public UserXmlDTO getUserXmlDTO() {
        return new UserXmlDTO("李四", "123456");
    }

}
