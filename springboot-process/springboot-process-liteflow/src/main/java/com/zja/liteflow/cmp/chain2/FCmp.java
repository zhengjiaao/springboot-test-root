package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import org.springframework.stereotype.Component;

/**
 * 数值循环组件的声明
 *
 * @Author: zhengja
 * @Date: 2024-10-09 11:15
 */
@Component("F")
public class FCmp {

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_FOR, nodeType = NodeTypeEnum.FOR)
    public int processFor(NodeComponent bindCmp) throws Exception {
        // do your biz
        System.out.println("Fcmp executed!");
        return 3;
    }

}
