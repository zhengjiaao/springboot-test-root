package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;

/**
 * @Author: zhengja
 * @Date: 2024-10-09 10:51
 */
@LiteflowComponent(id = "C", name = "C组件")
public class CCmp {

    @LiteflowMethod(LiteFlowMethodEnum.PROCESS)
    public void process(NodeComponent bindCmp) {
        System.out.println("CCmp executed!");
    }

}