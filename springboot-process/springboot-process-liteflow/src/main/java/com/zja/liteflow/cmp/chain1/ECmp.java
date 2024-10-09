/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:01
 * @Since:
 */
package com.zja.liteflow.cmp.chain1;

import com.yomahub.liteflow.core.NodeSwitchComponent;
import org.springframework.stereotype.Component;

/**
 * 选择节点e需要继承NodeSwitchComponent, 选择节点可以用于SWITCH关键字中。
 */
@Component("e")
public class ECmp extends NodeSwitchComponent {

    @Override
    public String processSwitch() throws Exception {
        // do your business
        System.out.println("e");

        // return "b"; // id
        return "tag:bt"; // tag

    }
}
