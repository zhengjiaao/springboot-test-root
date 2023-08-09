/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-19 13:48
 * @Since:
 */
package com.zja.juc.future.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Random;

/**
 * @author: zhengja
 * @since: 2023/07/19 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private String userId = "id" + new Random().nextInt(100);
    private String userName = "用户" + new Random().nextInt(10);
}
