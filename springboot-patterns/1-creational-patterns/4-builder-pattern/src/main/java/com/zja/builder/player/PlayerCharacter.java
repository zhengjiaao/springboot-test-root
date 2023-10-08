/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:33
 * @Since:
 */
package com.zja.builder.player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:33
 */
// 产品类 - 玩家角色
@Setter
@Getter
class PlayerCharacter {
    private String name;
    private int level;
    private int health;
    private int mana;
    // ...其他属性和方法

    public PlayerCharacter(String name, int level, int health, int mana) {
        this.name = name;
        this.level = level;
        this.health = health;
        this.mana = mana;
    }

    // ...其他属性的设置方法
}
