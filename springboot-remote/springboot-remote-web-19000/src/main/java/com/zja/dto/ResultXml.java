/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-14 17:08
 * @Since:
 */
package com.zja.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "ResultXmlDTO") // 若name 和 namespace 不生效，有可能是 jackson-dataformat-xml 依赖引起的
public class ResultXml {

//    @XmlElement//(name = "userXmlDTOTest") // 添加 XmlElement 报错
    private UserXmlDTO userXmlDTO;

    private UserXmlDTO userXml;

    private List<UserXmlDTO> userXmlDTOList;

    private List<UserXmlDTO> userXmlList;

}
