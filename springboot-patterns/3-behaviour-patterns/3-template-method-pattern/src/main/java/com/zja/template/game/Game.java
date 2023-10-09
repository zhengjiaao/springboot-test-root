/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:14
 * @Since:
 */
package com.zja.template.game;

/**
 * 抽象类Game，其中包含了一个模板方法play()，它定义了游戏的骨架。
 *
 * @author: zhengja
 * @since: 2023/10/09 13:14
 */
// 抽象类
abstract class Game {
    // 模板方法，定义了游戏的骨架
    public final void play() {
        initialize();
        startGame();
        endGame();
    }

    // 初始化游戏
    abstract void initialize();

    // 开始游戏
    abstract void startGame();

    // 结束游戏
    abstract void endGame();
}