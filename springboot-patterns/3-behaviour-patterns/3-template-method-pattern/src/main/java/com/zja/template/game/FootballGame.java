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
 * @author: zhengja
 * @since: 2023/10/09 13:14
 */
// 具体类A
class FootballGame extends Game {
    @Override
    void initialize() {
        System.out.println("Football game initialized.");
    }

    @Override
    void startGame() {
        System.out.println("Football game started. Enjoy the match!");
    }

    @Override
    void endGame() {
        System.out.println("Football game ended. Thank you for playing!");
    }
}