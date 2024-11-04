package com.zja.rule.qlexpress;

import com.ql.util.express.*;
import com.ql.util.express.instruction.op.OperatorBase;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @Author: zhengja
 * @Date: 2024-11-04 15:41
 */
public class QLExpressTest {

    // 基本表达式解析
    @Test
    public void SimpleExpression_test_1() throws Exception {
        String express = "2 + 3 * 5";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        Object result = runner.execute(express, context, null, true, false);
        assertEquals(17, result);
    }

    // 自定义函数
    @Test
    public void CustomFunction_test_2() throws Exception {
        String express = "customAdd(2, 3)";
        ExpressRunner runner = new ExpressRunner();

        // 注册自定义函数
        runner.addFunction("customAdd", new OperatorBase() {
            @Override
            public OperateData executeInner(InstructionSetContext instructionSetContext, ArraySwap arraySwap) throws Exception {
                OperateData param1 = arraySwap.get(0);
                OperateData param2 = arraySwap.get(1);

                if (param1 == null || param2 == null) {
                    throw new IllegalArgumentException("参数不能为空");
                }

                int sum = ((Number) param1.getObject(instructionSetContext)).intValue() + ((Number) param2.getObject(instructionSetContext)).intValue();
                return new OperateData(sum, int.class);
            }

            @Override
            public String getName() {
                return "customAdd";
            }
        });

        DefaultContext<String, Object> context = new DefaultContext<>();
        Object result = runner.execute(express, context, null, true, false);
        assertEquals(5, result);
    }

    // 错误处理
    @Test
    @Deprecated
    public void InvalidExpression_test_3() {
        String express = "2 + 3 *";
        // String express = "10 / 0";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();

        // assertThrows(ExpressException.class, () -> {
        //     runner.execute(express, context, null, true, false);
        // });
    }

    // 使用变量
    @Test
    public void VariableExpression_test_4() throws Exception {
        String express = "a + b * c";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 2);
        context.put("b", 3);
        context.put("c", 5);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals(17, result);
    }

    // 集合操作
    @Test
    public void ListExpression_test_5() throws Exception {

        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();

        String express = "list.get(0) + list.get(1)";
        List<Integer> list = Arrays.asList(2, 3, 5);
        context.put("list", list);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals(5, result);

        // 使用NewMap创建Map
        String expressMap = "abc = NewMap(1:1, 2:2); return abc.get(1) + abc.get(2);";
        Object resultMap = runner.execute(expressMap, context, null, false, false);
        System.out.println("NewMap Result: " + resultMap);

        // 使用NewList创建List
        String expressList = "abc = NewList(1, 2, 3); return abc.get(1) + abc.get(2);";
        Object resultList = runner.execute(expressList, context, null, false, false);
        System.out.println("NewList Result: " + resultList);

        // 使用方括号[]创建List
        String expressSquareBrackets = "abc = [1, 2, 3]; return abc[1] + abc[2];";
        Object resultSquareBrackets = runner.execute(expressSquareBrackets, context, null, false, false);
        System.out.println("Square Brackets Result: " + resultSquareBrackets);
    }

    // 条件判断
    @Test
    @Deprecated
    public void IfElseExpression_test_6() throws Exception {
        String express = "if(a > 10, '大于10', '小于等于10')";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 15);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals("大于10", result);
    }

    // 循环
    @Test
    @Deprecated
    public void ForEachExpression_test_7() throws Exception {
        String express = "result = 0; for(i in list) { result += i }; result";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        context.put("list", list);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals(6, result);
    }

    // 日期时间处理
    @Test
    @Deprecated
    public void DateExpression_test_8() throws Exception {
        String express = "date('yyyy-MM-dd', '2023-10-01')";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();

        Object result = runner.execute(express, context, null, true, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse("2023-10-01");
        assertEquals(expectedDate, result);
    }

    // 复杂表达式
    @Test
    @Deprecated
    public void ComplexExpression_test_9() throws Exception {
        String express = "if(a > 10, a * 2, a + 5)";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 8);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals(13, result);
    }

    // 字符串操作
    @Test
    public void StringExpression_test_10() throws Exception {
        String express = "str1 + ' ' + str2";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("str1", "Hello");
        context.put("str2", "World");

        Object result = runner.execute(express, context, null, true, false);
        assertEquals("Hello World", result);
    }

    // 嵌套表达式
    @Test
    @Deprecated
    public void NestedExpression_test_11() throws Exception {
        String express = "if(a > 10, if(b > 5, 'A', 'B'), 'C')";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 15);
        context.put("b", 7);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals("A", result);
    }

    // 数组操作
    @Test
    @Deprecated
    public void ArrayExpression_test_12() throws Exception {
        String express = "array[0] + array[1]";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        int[] array = {1, 2, 3};
        context.put("array", array);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals(3, result);
    }

    // 对象属性访问
    @Test
    public void ObjectExpression_test_13() throws Exception {
        String express = "person.getName() + ' is ' + person.getAge() + ' years old'";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        Person person = new Person("Alice", 30);
        context.put("person", person);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals("Alice is 30 years old", result);
    }

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    // 多条件判断
    @Test
    @Deprecated
    public void MultiConditionExpression_test_14() throws Exception {
        String express = "if(a > 10, 'A', if(a > 5, 'B', 'C'))";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 7);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals("B", result);
    }

    // 自定义类的使用
    @Test
    public void CustomClassExpression_test_15() throws Exception {
        String express = "calculator.add(5, 3) + calculator.subtract(10, 2)";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        Calculator calculator = new Calculator();
        context.put("calculator", calculator);

        Object result = runner.execute(express, context, null, true, false);
        assertEquals(16, result);
    }

    static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }

        public int subtract(int a, int b) {
            return a - b;
        }
    }

}
