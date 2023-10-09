/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 15:06
 * @Since:
 */
package com.zja.state.loginState;

/**
 * @author: zhengja
 * @since: 2023/10/09 15:06
 */
//客户端示例代码
public class Client {
    public static void main(String[] args) {
        LoginContext context = new LoginContext();

        // 输入用户名
        context.enterUsername("user123");

        // 输入密码
        context.enterPassword("password");

        // 进行登录操作
        context.login();

        //输出结果：
        //输入用户名：user123
        //请先输入用户名
        //请先输入用户名
    }
}
