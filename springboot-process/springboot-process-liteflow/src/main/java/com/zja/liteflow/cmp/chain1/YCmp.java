package com.zja.liteflow.cmp.chain1;

import com.yomahub.liteflow.core.NodeBooleanComponent;
import org.springframework.stereotype.Component;

/**
 * @Author: zhengja
 * @Date: 2024-10-08 16:00
 */
@Component("y")
public class YCmp extends NodeBooleanComponent {

 /*   @Override
    public void process() throws Exception {
        // do your business
        System.out.println("y");
    }*/

    @Override
    public boolean processBoolean() throws Exception {
        // do your business
        System.out.println("y");

        return true;
    }
}