/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:02
 * @Since:
 */
package com.zja.abstract1.osui;

/**
 * 客户端代码根据当前操作系统选择合适的UI工厂，并使用工厂创建对应的UI组件
 *
 * @author: zhengja
 * @since: 2023/10/08 10:02
 */
public class Client {
    public static void main(String[] args) {
        String os = "Windows";  // 假设当前操作系统为Windows

        UIFactory factory;
        if (os.equals("Windows")) {
            factory = new WindowsUIFactory();
        } else {
            factory = new MacUIFactory();
        }

        Button button = factory.createButton();
        TextBox textBox = factory.createTextBox();

        button.render();  // 输出：渲染Windows风格的按钮
        textBox.render();  // 输出：渲染Windows风格的文本框
    }
}
