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
// 具体策略类C
class FlyStrategy implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Flying...");
    }
}