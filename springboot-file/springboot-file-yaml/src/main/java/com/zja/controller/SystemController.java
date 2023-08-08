/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 13:22
 * @Since:
 */
package com.zja.controller;

import com.alibaba.fastjson.JSON;
import com.zja.properties1.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhengja
 * @since: 2023/08/08 13:22
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemConfig systemConfig;

    @GetMapping("/config")
    public void getSystemConfig() {
        System.out.println(JSON.toJSONString(systemConfig,true));
    }

}
