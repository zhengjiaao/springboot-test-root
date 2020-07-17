package com.dist.api;

import feign.Param;
import feign.RequestLine;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-05-12 17:02
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：第三方平台-特力惠-远程接口调用
 */
public interface TelihuiService {

    /**
     * 第三方 特力惠 用户登录验证
     * @param loginName 用户登录名称
     * @param casCookie 认证牌
     * @return true/false
     */
    @RequestLine("GET /rest/v1/get/login?loginName={loginName}&casCookie={casCookie}")
    Object checkTelihuiLogin(@Param(value = "loginName")String loginName,@Param(value = "casCookie")String casCookie);

    @RequestLine("GET /rest/v1/get/login/ex?loginName={loginName}")
    Object checkTelihuiLogin(@Param(value = "loginName")String loginName);

    //获取所有组织机构
    @RequestLine("GET /organization/rest/organizations")
    Object organizations();

    //获取所有部门
    @RequestLine("GET /organization/rest/departments")
    Object departments();

    //获取所有用户
    @RequestLine("GET /organization/rest/users")
    Object users();

    //根据id获取组织机构
    @RequestLine("GET /organization/rest/organizations?id={id}")
    Object organizations(@Param(value = "id") String id);

    //根据id获取部门
    @RequestLine("GET /organization/rest/departments?id={id}")
    Object departments(@Param(value = "id") String id);

    //根据id获取用户
    @RequestLine("GET /organization/rest/users?id={id}")
    Object users(@Param(value = "id") String id);

}
