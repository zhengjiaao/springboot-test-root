package com.zja.rule.groovy;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

/**
 * @Author: zhengja
 * @Date: 2025-01-14 15:26
 */
public class GroovyScriptEngineTest {

    @Test
    public void test_1() throws IOException, ScriptException, ResourceException {
        // 创建 Binding 对象，用于存储变量
        Binding binding = new Binding();
        binding.setVariable("age", 20);

        // 获取 resources 目录的 URL
        URL resourceUrl = getClass().getClassLoader().getResource("script.groovy");
        if (resourceUrl == null) {
            throw new IOException("script.groovy not found in resources");
        }

        // 创建 GroovyScriptEngine 实例，使用 resources 目录的路径
        GroovyScriptEngine gse = new GroovyScriptEngine(new URL[]{resourceUrl});

        // 加载并执行脚本
        Script script = gse.createScript("script.groovy", binding);
        Object result = script.run();

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: Adult
    }

    @Test
    public void test_2() throws IOException, ResourceException {
        // 创建 Binding 对象，用于存储变量
        Binding binding = new Binding();
        binding.setVariable("age", 15);

        // 获取 resources 目录的 URL
        URL resourceUrl = getClass().getClassLoader().getResource("script.groovy");
        if (resourceUrl == null) {
            throw new IOException("script.groovy not found in resources");
        }

        // 创建 GroovyScriptEngine 实例，使用 resources 目录的路径
        GroovyScriptEngine gse = new GroovyScriptEngine(new URL[]{resourceUrl});

        try {
            // 加载并执行脚本
            Script script = gse.createScript("script.groovy", binding);
            Object result = script.run();

            // 输出结果
            System.out.println("Result: " + result);  // 输出: Result: Minor
        } catch (ScriptException e) {
            // 捕获异常，并输出错误信息
            System.out.println("Error: " + e.getMessage());
        }
    }
}
