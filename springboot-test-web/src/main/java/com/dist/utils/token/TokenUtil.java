package com.dist.utils.token;

import com.dist.utils.IdUtil;

/**
 * token 工具
 *
 *  @author yinxp@dist.com.cn
 */
public abstract class TokenUtil {

    /**
     * 生产tokenSeq
     * @return
     */
    public static final String tokenSeq() {
        return IdUtil.uuid32();
    }

}
