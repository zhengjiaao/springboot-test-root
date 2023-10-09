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
// LoginState 接口
interface LoginState {
    void enterUsername(String username);
    void enterPassword(String password);
    void login();
}
