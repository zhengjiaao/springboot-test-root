/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 11:27
 * @Since:
 */
package com.zja.strategy.move;

/**
 * @author: zhengja
 * @since: 2023/10/09 11:27
 */
// 上下文类
class Character {
    private MoveStrategy moveStrategy;

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public void move() {
        moveStrategy.move();
    }
}
