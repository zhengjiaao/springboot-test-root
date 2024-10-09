package com.zja.liteflow.cmp.chain2;

import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-09 11:16
 */
@Component("G")
public class GCmp {
    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_ITERATOR, nodeType = NodeTypeEnum.ITERATOR)
    public Iterator<?> processIterator(NodeComponent bindCmp) throws Exception {
        // do your biz
        System.out.println("Gcmp executed!");

        List<String> list = ListUtil.toList("jack", "mary", "tom");
        return list.iterator();
    }
}
