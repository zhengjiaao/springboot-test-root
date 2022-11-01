/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-14 17:41
 * @Since:
 */
package com.zja.dto.oa;

import lombok.Data;

/**
 * 用户
 */
@Data
//@XmlRootElement(name = "userList")
public class OAUser {

    //用户id
    private String id;
    //用户名
    private String userName;
    //登录名
    private String logName;
    //密码
    private String password;

    //状态1=有效
    private String status;
    //状态文本 有效
    private String statusText;

    //排序 256
    private Integer showOrder;

    public OAUser() {
    }

    public OAUser(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
