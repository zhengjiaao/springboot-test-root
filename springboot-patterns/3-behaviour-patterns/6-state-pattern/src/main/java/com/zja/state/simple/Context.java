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
// Context - 上下文类
class Context {
    private State currentState;

    public Context() {
        currentState = new StandbyState(); // 初始状态设置为待机状态
    }

    public void setState(State state) {
        currentState = state;
    }

    public void request() {
        currentState.handle();
    }
}