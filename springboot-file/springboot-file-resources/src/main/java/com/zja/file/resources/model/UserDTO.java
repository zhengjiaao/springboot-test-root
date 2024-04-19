package com.zja.file.resources.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/04/18 16:47
 */
@Data
public class UserDTO implements Serializable {
    private String name;
    private Integer age;
}
