/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 15:01
 * @Since:
 */
package com.zja.chain1;

import com.yomahub.liteflow.core.NodeComponent;
import com.zja.entity.User;
import org.springframework.stereotype.Component;

@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() throws Exception {

        //获取参数
        /*String requestParam = this.getRequestData();
        System.out.println(requestParam);*/

        User requestBean = this.getRequestData();
        System.out.println(requestBean);

        //获取Bean对象
//        User user = this.getFirstContextBean();
        User user = this.getContextBean(User.class);
        System.out.println(user);
        //do your business
        System.out.println("a");
    }
}
