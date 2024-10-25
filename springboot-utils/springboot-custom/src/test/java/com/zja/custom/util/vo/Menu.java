package com.zja.custom.util.vo;

import lombok.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 16:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private Integer id;
    private Integer parentId;
    private String name;
    private String type;
}
