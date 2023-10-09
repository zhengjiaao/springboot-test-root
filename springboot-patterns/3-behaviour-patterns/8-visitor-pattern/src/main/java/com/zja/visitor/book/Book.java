/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:32
 * @Since:
 */
package com.zja.visitor.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Element {
    private String name;
    private double price;
    private double weight;

    // 省略构造函数和其他方法

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}