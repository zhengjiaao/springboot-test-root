/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:39
 * @Since:
 */
package com.zja.proxy.calculator;

/**
 * 代理对象 CalculatorProxy 实现了 Calculator 接口，并在调用目标对象的方法之前和之后添加了日志记录
 *
 * @author: zhengja
 * @since: 2023/10/08 14:39
 */
public class CalculatorProxy implements Calculator {
    private Calculator calculator;

    public CalculatorProxy(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public int add(int a, int b) {
        System.out.println("Before add operation");
        int result = calculator.add(a, b);
        System.out.println("After add operation");
        return result;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("Before subtract operation");
        int result = calculator.subtract(a, b);
        System.out.println("After subtract operation");
        return result;
    }
}