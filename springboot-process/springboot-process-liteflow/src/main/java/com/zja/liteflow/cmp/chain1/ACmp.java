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
import com.zja.liteflow.context.UserContext;
import org.springframework.stereotype.Component;

/**
 * 普通组件节点需要继承NodeComponent，可用于THEN和WHEN关键字中。
 */
@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {
        // 获取参数（String）
        // String requestParam = this.getRequestData();
        // System.out.println(requestParam);

        // 获取流程初始参数（Bean）
        OrderContext requestBean = this.getRequestData();
        System.out.println(requestBean);

        // 获取上下文数据(示例)
        // UserContext userContext = this.getContextBean(UserContext.class);
        // OrderContext orderContext = this.getContextBean("orderContext");
        // OrderContext context = this.getFirstContextBean();
        OrderContext context = this.getContextBean(OrderContext.class);
        System.out.println(context);

        // do your business
        System.out.println("a");

        // 模拟异常
        // throw new RuntimeException("a error");
    }
}
