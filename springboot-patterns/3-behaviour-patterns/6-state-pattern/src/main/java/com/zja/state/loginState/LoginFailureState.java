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
// Concrete State - 登录失败状态
class LoginFailureState implements LoginState {
    private LoginContext context;

    public LoginFailureState(LoginContext context) {
        this.context = context;
    }

    @Override
    public void enterUsername(String username) {
        // 登录失败后，不再处理输入用户名的逻辑
        System.out.println("登录失败，请重新尝试");
    }

    @Override
    public void enterPassword(String password) {
        // 登录失败后，不再处理输入密码的逻辑
        System.out.println("登录失败，请重新尝试");
    }

    @Override
    public void login() {
        // 登录失败后，可以重新尝试登录
        System.out.println("登录失败，请重新尝试");
    }
}