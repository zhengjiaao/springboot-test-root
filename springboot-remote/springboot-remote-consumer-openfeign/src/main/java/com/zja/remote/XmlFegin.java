/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-14 14:30
 * @Since:
 */
package com.zja.remote;

import com.zja.dto.ResultXml;
import com.zja.dto.UserXmlDTO;
import feign.RequestLine;

import java.util.List;

/**
 * 测试 XML数据接收
 */
//@Headers("Accept: application/xml")
//@Headers({"Accept: application/atom+xml"})
//@Headers({"Accept: application/json"})
public interface XmlFegin {

    @RequestLine("GET /xml/get/user")
//    @Headers({"Accept: application/atom+xml"})
//    @Headers("Content-Type: application/xml")
    UserXmlDTO getUserXmlDTOV3();

    @RequestLine("GET /xml/get/user")
    List<UserXmlDTO> getUserXmlDTOList();

    @RequestLine("GET /xml/get/ResultXml")
    ResultXml getResultXml();
}
