package com.zja.redis.listener;

// redis 监听器实现（key过期事件）

// todo 需要注意点：Redis实例必须正确配置并启用了键空间通知（keyspace notifications）,在Redis配置文件中添加或修改配置项：notify-keyspace-events Ex