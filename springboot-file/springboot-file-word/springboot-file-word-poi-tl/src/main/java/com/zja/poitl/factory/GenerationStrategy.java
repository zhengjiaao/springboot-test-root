package com.zja.poitl.factory;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/04/02 10:01
 */
// 生成策略接口
public interface GenerationStrategy {

    String generate(String wordPath, String data, String template) throws IOException;
}
