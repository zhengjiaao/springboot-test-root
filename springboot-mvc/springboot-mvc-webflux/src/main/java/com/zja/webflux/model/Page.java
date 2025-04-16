package com.zja.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-04-16 11:22
 */
@Data
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
}