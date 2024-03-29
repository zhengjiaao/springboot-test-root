package com.zja.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2019/9/9 15:37
 */
@Data
@Component
@ConfigurationProperties(prefix = "my-attributes")
public class MyAttributes {

    private String value;
    private String[] valueArray;
    private List<String> valueList;
    private HashMap<String, String> valueMap;
    private List<Map<String, String>> valueMapList;
    private List<User> valueUserList;
    private User valueUser;
}
