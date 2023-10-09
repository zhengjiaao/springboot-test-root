/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:19
 * @Since:
 */
package com.zja.template.beverage;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:19
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        Beverage coffee = new Coffee();
        coffee.prepareBeverage();

        System.out.println();

        Beverage tea = new Tea();
        tea.prepareBeverage();

        //输出结果：
        //Boiling water...
        //Brewing coffee...
        //Pouring into cup...
        //Adding sugar and milk...
        //
        //Boiling water...
        //Steeping tea...
        //Pouring into cup...
        //Adding lemon...
    }
}
