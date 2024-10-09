package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;

/**
 * 选择组件的声明
 *
 * @Author: zhengja
 * @Date: 2024-10-09 11:13
 */
@LiteflowComponent("E")
public class ECmp {

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_SWITCH, nodeType = NodeTypeEnum.SWITCH)
    public String processSwitch(NodeComponent bindCmp) throws Exception {
        System.out.println("Ecmp executed!");

        // return "B"; // id
        return "tag:BT"; // tag
    }
}