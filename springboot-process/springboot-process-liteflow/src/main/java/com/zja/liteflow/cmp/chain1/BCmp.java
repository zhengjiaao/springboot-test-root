/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:01
 * @Since:
 */
package com.zja.liteflow.cmp.chain1;

import com.yomahub.liteflow.core.NodeComponent;
import com.zja.liteflow.context.OrderContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("b")
public class BCmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        Thread.sleep(1000);

        // do your business
        System.out.println("b");
    }

    // 重写isAccess方法，默认为true，可以根据业务做判断，返回true表示可以执行process()，返回false表示不允许执行process()
    @Override
    public boolean isAccess() {
        OrderContext orderContext = this.getContextBean(OrderContext.class);
        return !StringUtils.isEmpty(orderContext.getId());
    }
}
