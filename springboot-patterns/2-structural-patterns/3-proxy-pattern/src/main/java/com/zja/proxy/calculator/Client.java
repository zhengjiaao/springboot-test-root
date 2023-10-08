/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:40
 * @Since:
 */
package com.zja.proxy.calculator;

/**
 * 使用代理对象进行计算，并查看日志记录的效果
 *
 * @author: zhengja
 * @since: 2023/10/08 14:40
 */
public class Client {
    public static void main(String[] args) {
        Calculator calculator = new CalculatorImpl();
        Calculator proxy = new CalculatorProxy(calculator);

        int sum = proxy.add(5, 3);
        System.out.println("Sum: " + sum);

        int difference = proxy.subtract(10, 6);
        System.out.println("Difference: " + difference);

        //输出结果：
        //Before add operation
        //After add operation
        //Sum: 8
        //Before subtract operation
        //After subtract operation
        //Difference: 4
    }
}
