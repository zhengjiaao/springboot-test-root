/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:35
 * @Since:
 */
package com.zja.builder.player;

/**
 * 在客户端代码中，我们创建了一个指导者对象，并将特定的建造者对象传递给指导者。
 * 然后，通过指导者调用相应的方法来创建玩家角色对象。
 * 最终，我们可以使用创建的玩家角色对象进行后续操作。
 *
 * @author: zhengja
 * @since: 2023/10/08 10:35
 */
public class Client {
    public static void main(String[] args) {
        PlayerBuilder warriorBuilder = new WarriorBuilder();
        PlayerDirector director = new PlayerDirector(warriorBuilder);
        PlayerCharacter warrior = director.createPlayer();

        // 使用创建的玩家角色
        System.out.println(warrior.getName()); //John
        System.out.println(warrior.getLevel()); //10
        System.out.println(warrior.getHealth()); //200
        System.out.println(warrior.getMana()); //100
        // ...其他操作
    }
}
