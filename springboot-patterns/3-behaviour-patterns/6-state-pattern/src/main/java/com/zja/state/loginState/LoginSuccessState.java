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
// Concrete State - 登录成功状态
class LoginSuccessState implements LoginState {
    private LoginContext context;

    public LoginSuccessState(LoginContext context) {
        this.context = context;
    }

    @Override
    public void enterUsername(String username) {
        // 登录成功后，不再处理输入用户名的逻辑
        System.out.println("已登录成功，用户名为：" + username);
    }

    @Override
    public void enterPassword(String password) {
        // 登录成功后，不再处理输入密码的逻辑
        System.out.println("已登录成功，无需再输入密码");
    }

    @Override
    public void login() {
        // 已经登录成功，无需重复登录
        System.out.println("已登录成功，无需重复登录");
    }
}
