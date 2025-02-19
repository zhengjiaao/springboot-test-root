package com.zja.sensitive.word.houbb.custom;

import com.github.houbb.sensitive.word.api.IWordAllow;

import java.util.Collections;
import java.util.List;

/**
 * 允许的内容-返回的内容不被当做敏感词
 *
 * @Author: zhengja
 * @Date: 2025-02-19 14:28
 */
public class MyWordAllow implements IWordAllow {

    @Override
    public List<String> allow() {
        return Collections.singletonList("五星红旗");
    }

}