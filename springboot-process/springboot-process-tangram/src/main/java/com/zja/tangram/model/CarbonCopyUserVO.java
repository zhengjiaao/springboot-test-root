/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:47
 * @Since:
 */
package com.zja.tangram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 抄送用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarbonCopyUserVO implements Serializable {
    private String variableKey;
    private String userId;
    private String userName;
    private String loginName;
}
