/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-19 13:30
 * @Since:
 */
package com.zja.juc.future.auxiliary;

import com.zja.juc.future.entity.Role;

import java.util.Arrays;
import java.util.List;

/**
 * 角色服务
 *
 * @author: zhengja
 * @since: 2023/07/19 13:30
 */
public class RoleService {

    public Role getRole() throws InterruptedException {
        //睡眠 1s
        Thread.sleep(1000);
        System.out.println("getRole()");
        return new Role();
    }

    public Role getRole(String userId) throws InterruptedException {
        //睡眠 1s
        Thread.sleep(1000);
        System.out.println("getRole(userId)");
        return new Role();
    }

    public List<Role> getRoleList() throws InterruptedException {
        //睡眠 2s
        Thread.sleep(2000);
        System.out.println("getRoleList()");
        return Arrays.asList(new Role(), new Role());
    }

}
