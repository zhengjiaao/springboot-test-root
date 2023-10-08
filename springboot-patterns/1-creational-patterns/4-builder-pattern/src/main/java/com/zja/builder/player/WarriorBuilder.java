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
// 具体建造者
class WarriorBuilder implements PlayerBuilder {
    private PlayerCharacter character;

    public WarriorBuilder() {
        character = new PlayerCharacter("Warrior", 1, 100, 50);
    }

    @Override
    public void setName(String name) {
        character.setName(name);
    }

    @Override
    public void setLevel(int level) {
        character.setLevel(level);
    }

    @Override
    public void setHealth(int health) {
        character.setHealth(health);
    }

    @Override
    public void setMana(int mana) {
        character.setMana(mana);
    }

    @Override
    public void equipWeapon(String weapon) {
        // 设置武器属性和效果
    }

    @Override
    public void equipArmor(String armor) {
        // 设置护甲属性和效果
    }

    @Override
    public void equipAccessory(String accessory) {
        // 设置饰品属性和效果
    }

    @Override
    public PlayerCharacter build() {
        return character;
    }
}