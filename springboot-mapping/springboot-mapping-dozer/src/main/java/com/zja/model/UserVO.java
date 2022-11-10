/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-03 10:48
 * @Since:
 */
package com.zja.model;

import lombok.Data;
import org.dozer.Mapping;

@Data
public class UserVO {
    @Mapping("username")
    private String name;
    private String password;
    private Long ctime;
}
