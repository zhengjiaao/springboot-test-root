/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:01
 * @Since:
 */
package com.zja.liteflow.cmp.chain1;

import com.yomahub.liteflow.core.NodeBooleanComponent;
import org.springframework.stereotype.Component;

/**
 * 布尔组件需继承NodeBooleanComponent
 * <p>
 * 布尔组件可用于以下关键字中：
 * IF...ELIF...ELSE，可以参考条件编排这一章。
 * WHILE...DO...，可以参考循环编排这一章。
 * FOR...DO...BREAK,WHILE...DO...BREAK,ITERATOR...DO...BREAK，可以参考循环编排这一章。
 * </p>
 */
@Component("w")
public class WCmp extends NodeBooleanComponent {

    @Override
    public boolean processBoolean() throws Exception {
        // do your business
        System.out.println("w");

        return true;
    }
}
