/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-14 14:20
 * @Since:
 */
package com.zja.controller;

import com.zja.model.dto.ResultXml;
import com.zja.model.dto.UserXmlDTO;
import com.zja.remote.XmlFegin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("提供远程-XML测试接口")
@RestController
@RequestMapping("/fegin/xml")
public class FeignXmlController {

    @Autowired
    XmlFegin xmlFegin;

    /*@ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/get/user/v1")
    public Object getUserXmlDTOV1() {
        System.out.println(xmlFegin.getUserXmlDTOV1());
        return xmlFegin.getUserXmlDTOV1();
    }

    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/get/user/v2")
    public String getUserXmlDTOV2() {
        System.out.println(xmlFegin.getUserXmlDTOV2());
        return xmlFegin.getUserXmlDTOV2();
    }*/

    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/get/user/v3")
    public UserXmlDTO getUserXmlDTOV3() {
        System.out.println(xmlFegin.getUserXmlDTOV3());
        return xmlFegin.getUserXmlDTOV3();
    }

    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/get/user/list")
    public List<UserXmlDTO> getUserXmlDTOList() {
        return xmlFegin.getUserXmlDTOList();
    }

    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/xml/get/ResultXml")
    public ResultXml getResultXml() {
        return xmlFegin.getResultXml();
    }
}
