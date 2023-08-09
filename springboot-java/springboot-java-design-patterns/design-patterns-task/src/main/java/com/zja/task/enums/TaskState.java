package com.zja.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 *
 * @author: zhengja
 * @since: 2023/06/14 15:59
 */
@AllArgsConstructor
@Getter
public enum TaskState {
    INIT(1, "初始化"),
    ONGOING(2, "进行中"),
    PAUSED(3, "暂停中"),
    FINISHED(4, "已完成"),
    EXPIRED(5, "已过期");

    private final int code;
    private final String message;
}
