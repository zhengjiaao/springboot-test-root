/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-14 17:42
 * @Since:
 */
package com.zja.dto.oa;

import lombok.Data;

import java.util.List;

/**
 * 部门
 */
@Data
//@XmlRootElement(name = "data")
public class OAOrg {

    //部门id
    private String id;
    //部门父id
    private String fid;
    //部门名称
    private String orgName;
    //部门全名
    private String fullName;

    //部门层级
    private String orgLevel;
    //例："机构"
    private String orgLevelText;

    //地址
    private String address;
    //邮政编码
    private String zipCode;

    //部门下的子部门列表 待确定
//    private List<OAOrg> orgList;

    //部门下的用户列表
    private List<OAUser> userList;

}
