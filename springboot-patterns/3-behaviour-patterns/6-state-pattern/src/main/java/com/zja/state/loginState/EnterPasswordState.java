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
// Concrete State - 输入密码状态
class EnterPasswordState implements LoginState {
    private LoginContext context;

    public EnterPasswordState(LoginContext context) {
        this.context = context;
    }

    @Override
    public void enterUsername(String username) {
        // 用户已经输入了用户名，不能再次输入
        System.out.println("用户名已输入：" + username);
    }

    @Override
    public void enterPassword(String password) {
        // 处理输入密码的逻辑
        System.out.println("输入密码：" + password);
    }

    @Override
    public void login() {
        // 用户已经输入了用户名和密码，进行登录操作
        System.out.println("正在进行登录操作...");
        // 登录成功或失败的判断逻辑...
        boolean loginSuccess = true;

        if (loginSuccess) {
            context.setState(new LoginSuccessState(context));
        } else {
            context.setState(new LoginFailureState(context));
        }
    }
}
