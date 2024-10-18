package com.zja.sensitive.word.service;

import java.lang.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-10 10:46
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DesensitizedString {

    /**
     * 脱敏类型
     */
    DesensitizedType type();

    /**
     * 当 type=HIDE 时,可自定义字符串脱敏起始位置(包含)
     */
    int hideStartInclude() default 0;

    /**
     * 当 type=HIDE 时,可自定义字符串脱敏结束位置(不包含) ,-1代表字符串长度
     */
    int hideEndExclude() default -1;

    /**
     * 当 type=ADDRESS 时,可自定义地址脱敏长度
     */
    int addressSensitiveSize() default 8;

    /**
     * 当 type=ID_CARD 时,可自定义身份证号前面脱敏长度
     */
    int idCardNumFront() default 1;

    /**
     * 当 type=ID_CARD 时,可自定义身份证号后面脱敏长度
     */
    int idCardNumBack() default 2;

    /**
     * 当 type=WORD 时,敏感词库路径(黑名单)
     */
    String wordBlackPath() default "word_blacklist.txt";

    /**
     * 当 type=WORD 时,敏感词白名单路径(白名单)，推荐采用移除黑名单方式
     */
    @Deprecated
    String wordWhitePath() default "word_whitelist.txt";
}
