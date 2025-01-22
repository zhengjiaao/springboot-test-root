package com.zja.onlyoffice.fegin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2025-01-17 13:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandArgs implements Serializable {
    // 命令：deleteForgotten
    private String c;
    // 唯一值
    private String key;
}
