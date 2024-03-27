package com.zja.juc.countDownLatch.service;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2024/03/19 13:18
 */
@Data
public class User implements Serializable {

    private Long id;
    private String name;
    private String address;
}
