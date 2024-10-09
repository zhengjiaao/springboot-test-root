package com.zja.liteflow.cmp.chain2;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;

/**
 * 普通组件的声明
 *
 * @Author: zhengja
 * @Date: 2024-10-09 10:50
 */
@LiteflowComponent(id = "A", name = "A组件")
public class ACmp {

    @LiteflowMethod(LiteFlowMethodEnum.PROCESS)
    public void process(NodeComponent bindCmp) {
        System.out.println("ACmp executed!");
    }

    @LiteflowMethod(LiteFlowMethodEnum.IS_ACCESS)
    public boolean isAcmpAccess(NodeComponent bindCmp) {
        return true;
    }

    @LiteflowMethod(LiteFlowMethodEnum.BEFORE_PROCESS)
    public void beforeAcmp(NodeComponent bindCmp) {
        System.out.println("before A");
    }

    @LiteflowMethod(LiteFlowMethodEnum.AFTER_PROCESS)
    public void afterAcmp(NodeComponent bindCmp) {
        System.out.println("after A");
    }

    @LiteflowMethod(LiteFlowMethodEnum.ON_SUCCESS)
    public void onAcmpSuccess(NodeComponent bindCmp) {
        System.out.println("Acmp success");
    }

    @LiteflowMethod(LiteFlowMethodEnum.ON_ERROR)
    public void onAcmpError(NodeComponent bindCmp, Exception e) {
        System.out.println("Acmp error");
    }

    @LiteflowMethod(LiteFlowMethodEnum.IS_END)
    public boolean isAcmpEnd(NodeComponent bindCmp) {
        return false;
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.ROLLBACK)
    public void rollbackA(NodeComponent bindCmp) throws Exception {
        System.out.println("ACmp rollback!");
    }

}