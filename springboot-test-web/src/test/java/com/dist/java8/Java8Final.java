package com.dist.java8;

/**lambda 表达式: 变量作用域
 * lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误
 * @author zhengja@dist.com.cn
 * @data 2019/8/6 16:33
 */
public class Java8Final {
    final static String salutation = "Hello! ";

    public static void main(String args[]){
        GreetingService greetService1 = message ->
                System.out.println(salutation + message);
        greetService1.sayMessage("Runoob");


        //直接在 lambda 表达式中访问外层的局部变量：num
        // lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）
        //在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量。
        final int num = 1;
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
        s.convert(2);  // 输出结果为 3
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    //直接在 lambda 表达式中访问外层的局部变量：
    public interface Converter<T1, T2> {
        void convert(int i);
    }
}
