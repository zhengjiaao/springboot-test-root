/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-13 16:29
 * @Since:
 */
package com.zja.web;

import com.zja.dto.UserXmlDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("提供远程-XML测试接口")
@RestController
@RequestMapping
public class WebXmlController {

    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/xml/get/user", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public UserXmlDTO getUserXmlDTO() {
        return new UserXmlDTO("李四", "123456");
    }

}
