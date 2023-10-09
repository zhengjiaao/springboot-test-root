/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:55
 * @Since:
 */
package com.zja.state.simple;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:55
 */
public class Client {
    public static void main(String[] args) {
        Context context = new Context();

        // 初始状态：待机
        context.request();

        // 切换状态：工作
        State workState = new WorkState();
        context.setState(workState);
        context.request();

        //输出结果：
        //当前处于待机状态
        //当前处于工作状态
    }
}
