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
 * @author: zhengja
 * @since: 2023/10/08 10:35
 */
// 指导者
class PlayerDirector {
    private PlayerBuilder builder;

    public PlayerDirector(PlayerBuilder builder) {
        this.builder = builder;
    }

    public PlayerCharacter createPlayer() {
        builder.setName("John");
        builder.setLevel(10);
        builder.setHealth(200);
        builder.setMana(100);
        builder.equipWeapon("Sword");
        builder.equipArmor("Plate Mail");
        builder.equipAccessory("Ring of Strength");
        return builder.build();
    }
}
