package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;

/**
 * @Author: zhengja
 * @Date: 2024-10-09 10:51
 */
@LiteflowComponent("D")
public class DCmp {

    @LiteflowMethod(LiteFlowMethodEnum.PROCESS)
    public void process(NodeComponent bindCmp) {
        System.out.println("DCmp executed!");
    }

}