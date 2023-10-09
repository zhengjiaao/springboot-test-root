/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:53
 * @Since:
 */
package com.zja.visitor.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bird implements AnimalElement {
    // 实现具体的鸟类
    private String name;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}