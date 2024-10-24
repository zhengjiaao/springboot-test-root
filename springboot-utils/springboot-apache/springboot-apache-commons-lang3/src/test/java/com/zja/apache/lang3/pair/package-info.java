package com.zja.apache.lang3.pair;

/*

Pair和Triple无缝解决多值返回问题，编写高效代码

-------------- Pair -----------------------

Pair类是一个抽象类，它有两个子类ImmutablePair和MutablePair

Pair 类是org.apache.commons.lang3库提供的一个简单的键值对容器，用于表示两个相关联的值。其主要作用是将两个值组织在一起，提供一种便捷的方式进行传递和处理。

-------------- Triple -----------------------

Triple是一个抽象类，它有两个子类：可变MutableTriple 以及不可变 ImmutableTriple。

Triple是一个用于表示三元组的抽象类。三元组是由三个元素组成的有序集合，其中每个元素都有特定的位置，分别称为左值（Left）、中间值（Middle）和右值（Right）。
Triple类提供了一种便捷的方式来组织和处理这种具有固定顺序的数据。可以在不创建专门类的情况下轻松返回三个值。
通过Triple，开发者可以更方便地处理包含三个元素的数据，减少了创建和维护多个变量的复杂性，使代码更加简洁。

参考：https://juejin.cn/post/7323205590371942452
 */