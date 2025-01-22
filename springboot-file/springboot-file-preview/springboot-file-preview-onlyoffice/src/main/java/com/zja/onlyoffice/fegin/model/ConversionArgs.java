package com.zja.onlyoffice.fegin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2025-01-17 13:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionArgs implements Serializable {
    private boolean async;
    private String filetype;
    private String key;
    private String outputtype; // 示例: pdf 将docx格式转换为pdf格式
    // private String password; // 可选地，默认为空，例如：用于将受密码保护的文件从docx格式转换为pdf格式
    private String title;
    private String url; // 示例：http://127.0.0.1:8082/words/sample.docx
}
