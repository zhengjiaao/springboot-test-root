/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:58
 * @Since:
 */
package com.zja.abstract1.osui.mac;

import com.zja.abstract1.osui.TextBox;

/**Mac风格的UI组件
 * @author: zhengja
 * @since: 2023/10/08 9:58
 */
public class MacTextBox implements TextBox {
    @Override
    public void render() {
        System.out.println("渲染Mac风格的文本框");
    }
}