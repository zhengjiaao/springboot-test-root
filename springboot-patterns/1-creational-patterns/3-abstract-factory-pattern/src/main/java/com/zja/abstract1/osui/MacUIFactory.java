/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:01
 * @Since:
 */
package com.zja.abstract1.osui;

import com.zja.abstract1.osui.mac.MacButton;
import com.zja.abstract1.osui.mac.MacTextBox;

/**
 * 实现具体的Mac风格的UI工厂
 *
 * @author: zhengja
 * @since: 2023/10/08 10:01
 */
public class MacUIFactory implements UIFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public TextBox createTextBox() {
        return new MacTextBox();
    }
}
