package com.zja.entity;

/**
 * 关系类型
 *
 * 所有关系标签常量,使用了interface成员变量是常量的特性，比class使用static final String XX="xx"简洁
 */
public interface RelsType {
    //拥有
    String OWN = "OWN";
    //有
    String HAVE = "HAVE";
}
