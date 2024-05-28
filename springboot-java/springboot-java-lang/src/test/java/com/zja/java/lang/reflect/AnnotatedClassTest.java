package com.zja.java.lang.reflect;

import org.dozer.util.ReflectionUtils;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: zhengja
 * @Date: 2024-05-28 10:15
 */
public class AnnotatedClassTest {

    // 自定义注解
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface MyAnnotation {
        String value();

        int param() default 0;
    }

    // 使用自定义注解的类
    @MyAnnotation(value = "Hello", param = 42)
    public static class AnnotatedClass {
        @MyAnnotation(value = "World")
        public void annotatedMethod() {
            // 方法实现
        }
    }

    // 使用反射访问注解
    public static void processAnnotations() {
        try {
            // 获取 AnnotatedClass 的 Class 对象
            Class<?> clazz = AnnotatedClass.class;

            // 获取类上的注解
            MyAnnotation classAnnotation = clazz.getAnnotation(MyAnnotation.class);
            System.out.println("Class Annotation: value = " + classAnnotation.value() + ", param = " + classAnnotation.param());

            // 获取方法上的注解
            Method method = clazz.getDeclaredMethod("annotatedMethod");
            MyAnnotation methodAnnotation = method.getAnnotation(MyAnnotation.class);
            System.out.println("Method Annotation: value = " + methodAnnotation.value() + ", param = " + methodAnnotation.param());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    // 使用反射来获取和操作自定义注解
    public static void main(String[] args) {
        processAnnotations();
    }

}
