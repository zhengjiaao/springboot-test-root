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
// Concrete State - 输入用户名状态
class EnterUsernameState implements LoginState {
    private LoginContext context;

    public EnterUsernameState(LoginContext context) {
        this.context = context;
    }

    @Override
    public void enterUsername(String username) {
        // 处理输入用户名的逻辑
        System.out.println("输入用户名：" + username);
    }

    @Override
    public void enterPassword(String password) {
        // 用户尚未输入用户名，不能输入密码
        System.out.println("请先输入用户名");
    }

    @Override
    public void login() {
        // 用户尚未输入用户名，不能进行登录
        System.out.println("请先输入用户名");
    }
}
