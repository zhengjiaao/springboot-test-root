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
// 具体类B
class ChessGame extends Game {
    @Override
    void initialize() {
        System.out.println("Chess game initialized.");
    }

    @Override
    void startGame() {
        System.out.println("Chess game started. Good luck!");
    }

    @Override
    void endGame() {
        System.out.println("Chess game ended. Well played!");
    }
}