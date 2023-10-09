/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:15
 * @Since:
 */
package com.zja.template.game;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:15
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        Game game1 = new FootballGame();
        game1.play();

        System.out.println();

        Game game2 = new ChessGame();
        game2.play();


        //输出结果：
        //Football game initialized.
        //Football game started. Enjoy the match!
        //Football game ended. Thank you for playing!
        //
        //Chess game initialized.
        //Chess game started. Good luck!
        //Chess game ended. Well played!
    }
}
