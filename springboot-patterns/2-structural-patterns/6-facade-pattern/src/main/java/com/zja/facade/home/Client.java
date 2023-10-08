/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:47
 * @Since:
 */
package com.zja.facade.home;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:47
 */
public class Client {
    // 客户端只需要通过调用外观类 HomeTheaterFacade 提供的方法来控制家庭影院系统，而不需要直接与各个子系统类交互。
    // 外观类封装了复杂的操作和子系统之间的协调，使得客户端代码更加简洁和易于理解。
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade();

        // 观看电影
        homeTheater.watchMovie("Avengers: Endgame");

        // 结束观影
        homeTheater.endMovie();

        //输出结果：
        //Get ready to watch a movie...
        //Shutting down the home theater...
    }
}
