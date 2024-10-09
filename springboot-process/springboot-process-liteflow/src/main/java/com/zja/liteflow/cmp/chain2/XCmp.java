package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import org.springframework.stereotype.Component;

/**
 * 布尔组件的声明
 *
 * @Author: zhengja
 * @Date: 2024-10-09 11:14
 */
@Component("X")
public class XCmp {

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_BOOLEAN, nodeType = NodeTypeEnum.BOOLEAN)
    public boolean processBoolean(NodeComponent bindCmp) throws Exception {
        // do your biz
        System.out.println("Xcmp executed!");

        return true;
    }
}
