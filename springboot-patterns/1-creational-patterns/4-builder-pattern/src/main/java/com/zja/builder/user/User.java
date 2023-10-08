/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:49
 * @Since:
 */
package com.zja.builder.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String password;
    private int age;
}
