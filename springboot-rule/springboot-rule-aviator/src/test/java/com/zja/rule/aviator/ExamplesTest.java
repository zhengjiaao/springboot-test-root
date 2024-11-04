package com.zja.rule.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * 参考：https://www.yuque.com/boyan-avfmj/aviatorscript
 *
 * @Author: zhengja
 * @Date: 2024-11-04 14:45
 */
public class ExamplesTest {

    @Test
    public void test_1() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/hello.av", true);
        exp.execute();
    }

    // 条件语句：if
    @Test
    public void test_2() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/if.av", true);
        exp.execute();
    }

    // 循环语句：for
    @Test
    public void test_3() throws IOException {
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_range1.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_range2.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_range3.av", true);

        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_seq.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_index_kv.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_break.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_return.av", true);
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/for_continue.av", true);

        exp.execute();
    }

    // 循环语句: while
    @Test
    public void test_4() throws IOException {
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/while1.av", true);
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/while2.av", true);

        exp.execute();
    }

    // Statement 语句和值: if、for、block返回的值
    @Test
    public void test_5() throws IOException {
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statement_value.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statement_value_if.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statement_value_for.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statement_value_block.av", true);
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statement_value_block_return.av", true);

        exp.execute();
    }

    // 异常处理
    @Test
    public void test_6() throws IOException {
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/handle_exception.av", true);
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/multi_catch.av", true);

        exp.execute();
    }

    // 函数和闭包
    @Test
    public void test_7() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/function.av", true);
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/function_return.av", true);

        exp.execute();
    }

    // exports 和模块：require 和 load
    @Test
    public void test_8() throws IOException {
        // Expression exp = AviatorEvaluator.getInstance().compileScript("examples/qsort_test.av", true);

        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/module_test.av", true);

        exp.execute();
    }

    // 数组和集合：更多参考：https://www.yuque.com/boyan-avfmj/aviatorscript/zg7bf9
    @Test
    public void test_9() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/tuple.av", true);

        exp.execute();
    }

    // 文件 IO：读写文件
    @Test
    public void test_10() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/file_io.av", true);

        exp.execute();
    }
}
