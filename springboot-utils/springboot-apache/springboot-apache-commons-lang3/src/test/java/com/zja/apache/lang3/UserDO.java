package com.zja.apache.lang3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 10:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO implements Serializable {
    private String id;
    private String name;
    private Integer age;
}
