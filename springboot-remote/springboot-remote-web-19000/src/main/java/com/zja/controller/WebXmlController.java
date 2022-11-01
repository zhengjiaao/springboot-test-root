/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-13 16:29
 * @Since:
 */
package com.zja.controller;

import com.zja.dto.ResultXml;
import com.zja.dto.UserXmlDTO;
import com.zja.dto.oa.ResultXmlBean;
import com.zja.dto.oa.OAOrgUser;
import com.zja.dto.oa.OAUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api("提供远程-XML测试接口")
@RestController
@RequestMapping
public class WebXmlController {

    /**
     * http://127.0.0.1:8080/springboot-test-remoteservice/xml/get/user
     */
    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/xml/get/user", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public UserXmlDTO getUserXmlDTO() {
        return new UserXmlDTO("李四", "123456");
    }

    /**
     * http://127.0.0.1:8080/springboot-test-remoteservice/xml/get/user/list
     */
    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/xml/get/user/list", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public List<UserXmlDTO> getUserXmlDTOList() {
        return Arrays.asList(
                new UserXmlDTO("李四", "111"),
                new UserXmlDTO("李四2", "222")
        );
    }

    /**
     * http://127.0.0.1:8080/springboot-test-remoteservice/xml/get/ResultXml
     */
    @ApiOperation(value = "get-无参数", notes = "返回xml数据")
    @GetMapping(value = "/xml/get/ResultXml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
//    @GetMapping(value = "/xml/get/ResultXml", produces = MediaType.APPLICATION_XML_VALUE) //报错
    public ResultXml getResultXml() {
        ResultXml resultXml = new ResultXml();
        resultXml.setUserXmlDTO(new UserXmlDTO("李四1", "2"));

        resultXml.setUserXml(new UserXmlDTO("李四2", "2"));

        List<UserXmlDTO> userXmlDTOList = new ArrayList<>();
        userXmlDTOList.add(new UserXmlDTO("张三1", "111"));
        userXmlDTOList.add(new UserXmlDTO("张三2", "222"));
        resultXml.setUserXmlDTOList(userXmlDTOList);

        List<UserXmlDTO> userXmlList = new ArrayList<>();
        userXmlList.add(new UserXmlDTO("张三1", "111"));
        resultXml.setUserXmlList(userXmlList);

        return resultXml;
    }

    @ApiOperation(value = "get-OAResult", notes = "返回xml数据")
    @GetMapping(value = "/xml/get/OAResult", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public ResultXmlBean getOAResult() {

        OAOrgUser oaOrgUser = new OAOrgUser();
        oaOrgUser.setId("123");
        oaOrgUser.setOrgName("测试部门");

        List<OAUser> userList = new ArrayList<>();
        userList.add(new OAUser("1", "lisi"));
        userList.add(new OAUser("2", "zhangsan"));
        oaOrgUser.setUserList(userList);

        List<OAOrgUser> orgUserList = new ArrayList<>();
        orgUserList.add(oaOrgUser);
        orgUserList.add(oaOrgUser);

        ResultXmlBean resultXmlBean = new ResultXmlBean();
        resultXmlBean.setCode("");
        resultXmlBean.setType("");
        resultXmlBean.setMessage("");
        resultXmlBean.setToken("");
        resultXmlBean.setData(orgUserList);

        return resultXmlBean;
    }

}
