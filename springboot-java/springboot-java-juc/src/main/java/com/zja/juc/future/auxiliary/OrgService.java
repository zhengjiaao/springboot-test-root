/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-19 13:30
 * @Since:
 */
package com.zja.juc.future.auxiliary;

import com.zja.juc.future.entity.Org;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * 组织机构服务
 *
 * @author: zhengja
 * @since: 2023/07/19 13:30
 */
public class OrgService {

    @SneakyThrows
    public Org getOrg() {
        //睡眠 1s
        Thread.sleep(1000);
        System.out.println("getOrg()");
        return new Org();
    }

    @SneakyThrows
    public Org getOrg(String userId) {
        //睡眠 1s
        Thread.sleep(1000);
        System.out.println("getOrg(userId)");
        return new Org();
    }

    public List<Org> getOrgList() throws InterruptedException {
        //睡眠 2s
        Thread.sleep(2000);
        System.out.println("getOrgList()");
        return Arrays.asList(new Org(), new Org());
    }
}
