package com.zja.liteflow.cmp.chain3;

import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;

import java.util.Iterator;
import java.util.List;

/**
 * 方法级别的声明 ：在一个bean里定义多个组件，如果你有非常多的组件，又同时想避免类的定义过多的问题。
 *
 * @Author: zhengja
 * @Date: 2024-10-09 11:27
 */
@LiteflowComponent
public class CmpConfig {

    // 普通组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "AA", nodeName = "AA组件")
    public void processA(NodeComponent bindCmp) {
        System.out.println("AA processA!");
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.IS_ACCESS, nodeId = "AA", nodeType = NodeTypeEnum.COMMON)
    public boolean isAccessA(NodeComponent bindCmp){
        System.out.println("AA isAccessA!");
        return true;
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.ON_SUCCESS, nodeId = "AA", nodeType = NodeTypeEnum.COMMON)
    public void onSuccessA(NodeComponent bindCmp){
        System.out.println("AA onSuccessA!");
    }

    // 普通组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "BB", nodeName = "BB组件")
    public void processB(NodeComponent bindCmp) {
        System.out.println("BB processB!");
    }

    // 普通组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "CC", nodeName = "CC组件")
    public void processC(NodeComponent bindCmp) {
        System.out.println("CC processC!");
    }

    // 普通组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "DD", nodeName = "DD组件")
    public void processD(NodeComponent bindCmp) {
        System.out.println("DD processD!");
    }

    // SWITCH组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_SWITCH, nodeId = "EE", nodeName = "EE组件", nodeType = NodeTypeEnum.SWITCH)
    public String processE(NodeComponent bindCmp) {
        System.out.println("EE processE!");

        // return "B"; // id
        return "tag:BT"; // tag
    }

    // 布尔组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_BOOLEAN, nodeId = "XX", nodeName = "XX组件", nodeType = NodeTypeEnum.BOOLEAN)
    public boolean processX(NodeComponent bindCmp) {
        System.out.println("XX processX!");

        return true;
    }

    // FOR组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_FOR, nodeId = "FF", nodeName = "FF组件", nodeType = NodeTypeEnum.FOR)
    public int processF(NodeComponent bindCmp) {
        System.out.println("FF processF!");

        return 3;
    }

    // 迭代组件的定义
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_ITERATOR, nodeId = "GG", nodeName = "GG组件", nodeType = NodeTypeEnum.ITERATOR)
    public Iterator<?> processG(NodeComponent bindCmp) {
        System.out.println("GG processG!");

        List<String> list = ListUtil.toList("jack", "mary", "tom");
        return list.iterator();
    }
}
