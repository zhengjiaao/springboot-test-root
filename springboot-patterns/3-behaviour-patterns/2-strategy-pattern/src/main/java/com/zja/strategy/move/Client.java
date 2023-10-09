/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 11:28
 * @Since:
 */
package com.zja.strategy.move;

/**
 * @author: zhengja
 * @since: 2023/10/09 11:28
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        Character character = new Character();

        // 角色使用步行策略
        MoveStrategy walkStrategy = new WalkStrategy();
        character.setMoveStrategy(walkStrategy);
        character.move();

        // 角色使用奔跑策略
        MoveStrategy runStrategy = new RunStrategy();
        character.setMoveStrategy(runStrategy);
        character.move();

        // 角色使用飞行策略
        MoveStrategy flyStrategy = new FlyStrategy();
        character.setMoveStrategy(flyStrategy);
        character.move();
    }
}
