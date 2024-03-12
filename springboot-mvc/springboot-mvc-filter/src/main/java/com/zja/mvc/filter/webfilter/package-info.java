package com.zja.mvc.filter.webfilter;

// todo 不推荐 @ServletComponentScan + @WebFilter 实现过滤器
// todo 原因：
//  1、@WebFilter不能实现排序，也不能搭配 @Order实现排序
//  2、在项目部署过程会遇到问题，无效情况，猜测与部署环境有关