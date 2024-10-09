/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:01
 * @Since:
 */
package com.zja.liteflow.cmp.chain1;

import com.yomahub.liteflow.core.NodeForComponent;
import org.springframework.stereotype.Component;

/**
 * 次数循环组件 需继承NodeForComponent
 * <p>
 * 次数循环组件,返回的是一个int值的循环次数。 主要用于FOR...DO...表达式。
 * </p>
 */
@Component("f")
public class FCmp extends NodeForComponent {

    @Override
    public int processFor() throws Exception {
        //这里根据业务去返回for的结果

        System.out.println("f");

        // 多层嵌套循环中获取下标
        // Integer loopIndex = this.getLoopIndex(); // a 组件要取到当前层循环下标
        // Integer preNLoopIndex = this.getPreNLoopIndex(1);  // a 组件要取到第二层循环下标
        // Integer preNLoopIndex2 = this.getPreNLoopIndex(2); // a 组件要取到第一层循环下标

        return 3; // 例如，固定3次
    }
}
