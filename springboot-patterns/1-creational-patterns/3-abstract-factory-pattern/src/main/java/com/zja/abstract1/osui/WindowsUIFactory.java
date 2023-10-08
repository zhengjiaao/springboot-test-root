/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:01
 * @Since:
 */
package com.zja.abstract1.osui;

import com.zja.abstract1.osui.win.WindowsButton;
import com.zja.abstract1.osui.win.WindowsTextBox;

/**
 * 实现具体的Windows风格的UI工厂
 *
 * @author: zhengja
 * @since: 2023/10/08 10:01
 */
public class WindowsUIFactory implements UIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public TextBox createTextBox() {
        return new WindowsTextBox();
    }
}