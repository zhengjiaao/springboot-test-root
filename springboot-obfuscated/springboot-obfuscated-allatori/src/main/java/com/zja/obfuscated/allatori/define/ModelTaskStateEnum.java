package com.zja.obfuscated.allatori.define;

import cn.hutool.core.util.StrUtil;

/**
 * @author: zhengja
 * @since: 2023/05/16 15:09
 */
public enum ModelTaskStateEnum {

    RUNNING("running", "进行中"),
    SUCCESS("success", "已完成"),
    FAILED("failed", "失败"),
    ABORT("abort","终止");

    public final String code;
    public final String name;

    ModelTaskStateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ModelTaskStateEnum getByCode(String code) {
        for (ModelTaskStateEnum stateEnum : values()) {
            if (stateEnum.code.equals(code)) {
                return stateEnum;
            }
        }

        throw new RuntimeException(StrUtil.format("枚举「{}」没有枚举对象", code));
    }
}
