/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:31
 * @Since:
 */
package com.zja.adapter.asList;

import java.util.Arrays;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:31
 */
public class Client {
    public static void main(String[] args) {
        String[] array = {"Apple", "Banana", "Orange"};

        // 使用asList()方法将数组适配为列表,适配器隐藏了底层的转换细节。
        // 使得开发者能够以一种简单且直接的方式进行数组到列表的转换，而无需手动编写转换代码。
        List<String> list = Arrays.asList(array);

        System.out.println(list);
    }
}
