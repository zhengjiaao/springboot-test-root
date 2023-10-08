/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:34
 * @Since:
 */
package com.zja.builder.player;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:34
 */
// 抽象建造者
interface PlayerBuilder {
    void setName(String name);
    void setLevel(int level);
    void setHealth(int health);
    void setMana(int mana);
    void equipWeapon(String weapon);
    void equipArmor(String armor);
    void equipAccessory(String accessory);
    PlayerCharacter build();
}
