/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-14 18:32
 * @Since:
 */
package com.zja.dto.oa;

import lombok.Data;

import java.util.List;

@Data
//@XmlRootElement(name = "data")
public class OAOrgUser {

    //部门id
    private String id;
    //部门名称
    private String orgName;
    //部门下的用户列表
//    @XmlAnyElement
//    @XmlAnyAttribute
    private List<OAUser> userList;
}
