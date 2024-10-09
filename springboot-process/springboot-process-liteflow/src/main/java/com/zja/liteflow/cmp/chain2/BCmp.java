package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;

/**
 * @Author: zhengja
 * @Date: 2024-10-09 10:51
 */
@LiteflowComponent(id = "B", name = "B组件")
public class BCmp {

    @LiteflowMethod(LiteFlowMethodEnum.PROCESS)
    public void process(NodeComponent bindCmp) {
        System.out.println("BCmp executed!");
    }

}