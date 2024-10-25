package com.zja.custom.util.vo;

import lombok.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 14:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    private String id;
    private String parentId;
    private String name;
    private String type;
    private String description;
}
