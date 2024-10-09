package com.zja.liteflow.cmp.chain1;

import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.core.NodeIteratorComponent;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-08 16:36
 */
@Component("g")
public class GCmp extends NodeIteratorComponent {

    @Override
    public Iterator<?> processIterator() throws Exception {
        System.out.println("g");

        List<String> list = ListUtil.toList("jack", "mary", "tom");
        return list.iterator();
    }
}
