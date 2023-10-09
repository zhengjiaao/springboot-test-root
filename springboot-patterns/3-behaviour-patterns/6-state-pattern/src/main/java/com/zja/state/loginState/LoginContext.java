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
 * LoginContext类作为上下文类，负责管理用户登录的状态，并提供了对外的方法.
 * 例如enterUsername()、enterPassword()和login()，用于触发当前状态所对应的行为。
 *
 * @author: zhengja
 * @since: 2023/10/09 15:06
 */
// LoginContext - 上下文类
class LoginContext {
    private LoginState currentState;

    public LoginContext() {
        currentState = new EnterUsernameState(this); // 初始状态设置为输入用户名状态
    }

    public void setState(LoginState state) {
        currentState = state;
    }

    public void enterUsername(String username) {
        currentState.enterUsername(username);
    }

    public void enterPassword(String password) {
        currentState.enterPassword(password);
    }

    public void login() {
        currentState.login();
    }
}