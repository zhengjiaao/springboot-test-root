/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-29 15:54
 * @Since:
 */
package com.zja.dto;

import lombok.Data;

/**
 *
 * @author: zhengja
 * @since: 2023/03/29 15:54
 */
@Data
public class LoginUserDTO {
    private String name;
    private String password;

    private String verifyCode;

}
