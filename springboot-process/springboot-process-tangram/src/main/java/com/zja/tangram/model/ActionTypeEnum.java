/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 19:47
 * @Since:
 */
package com.zja.tangram.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 流程任务操作类型
 */
@AllArgsConstructor
@Getter
public enum ActionTypeEnum {

    /**
     * 默认
     */
    DEFAULT("default", "默认"),
    /**
     * 提交
     */
    PASS("pass", "提交"),
    /**
     * 撤回
     */
    WITHDRAW("withdraw", "撤回"),
    /**
     * 拒绝
     */
    REJECT("reject", "拒绝"),
    /**
     * 退回
     */
    RETURN("return", "退回"),
    /**
     * 创建
     */
    START("start", "创建"),
    /**
     * 委派
     */
    DELEGATE("delegate", "委派"),
    /**
     * 转办
     */
    TRANSFER("transfer", "转办"),
    /**
     * 提交
     */
    RESOLVE("resolve", "提交");

    private final String key;
    private final String name;

    @Override
    public String toString() {
        return this.key;
    }

    public static ActionTypeEnum getKey(String name) {
        return Stream.of(values())
                .filter(e -> Objects.equals(name, e.name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未知的操作：" + name));
    }

}
