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
@XmlRootElement(name = "ResultXml")
public class ResultXml {

    private UserXmlDTO userXmlDTO;

    private List<UserXmlDTO> userXmlDTOList;

}
