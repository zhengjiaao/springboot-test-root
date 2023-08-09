/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-19 13:48
 * @Since:
 */
package com.zja.juc.future.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Random;

/**
 * @author: zhengja
 * @since: 2023/07/19 13:48
 */
@Data
public class Role implements Serializable {
    private String name = "角色" + new Random().nextInt(100);
}
