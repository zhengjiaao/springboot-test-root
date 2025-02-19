package com.zja.sensitive.word.houbb.custom;

import com.github.houbb.sensitive.word.api.IWordDeny;

import java.util.Collections;
import java.util.List;

/**
 * 拒绝出现的数据-返回的内容被当做是敏感词
 *
 * @Author: zhengja
 * @Date: 2025-02-19 14:29
 */
public class MyWordDeny implements IWordDeny {

    @Override
    public List<String> deny() {
        return Collections.singletonList("我的自定义敏感词");
    }

}
