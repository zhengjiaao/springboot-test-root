package com.dist.io.File;

import org.junit.Test;

/**
 * 方法的递归调用:方法自己调用自己
 *  适合于:方法中运算的主体不变,但是运行的时候,参与运行的方法参数会变化
 *  注意:递归一定要有出口, 必须可以让程序停下
 *        递归次数不能过多
 *        构造方法,禁止递归
 *
 * @Author: Mr.Zheng
 * @Description:
 */
public class TestDiGui {
    @Test
    public void test(){
        System.out.println(getSum(3));
        System.out.println(getJieCheng(5));
        System.out.println(getFBNQ(12));
    }


    //方法递归,计算斐波那契数列
    public int getFBNQ(int month){
        if(month ==1){
            return 1;
        }if(month ==2)
            return 1;
        return getFBNQ(month-1)+getFBNQ(month-2);
    }

    //计算阶乘 5！
    //5*4*3*2*1
    public int getJieCheng(int n){
        if(n==1)
            return 1;
        return n*getJieCheng(n-1);
    }

    /**
     *计算1+2+3++100和 =5050
     * 计算规律：
     *  n+(n-1)+(n-2)
     *  100+(100-1)+(99-1)+...1
     */
    public int getSum(int n) {
        if (n==1)
            return 1;
        return n+getSum(n-1);
    }
}
