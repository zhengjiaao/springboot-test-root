/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-13 16:31
 * @Since:
 */
package com.zja.dto;

import lombok.Data;

@Data
//@XmlRootElement(name = "UserXml") // 其它类中引用了此类，XmlRootElement中name不会生效
public class UserXmlDTO {

    private String username;
    private String password;

    public UserXmlDTO() {
    }

    public UserXmlDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
