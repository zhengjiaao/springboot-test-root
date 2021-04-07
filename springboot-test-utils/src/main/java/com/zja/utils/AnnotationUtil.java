package com.zja.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-01-13 14:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class AnnotationUtil<T> {

    public Class<T> clazz;

    public AnnotationUtil(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * 动态修改对象属性上某个注解的属性值，通过getClazz()方法可以得到修改后的class
     * @param fieldName 对象属性名称
     * @param annotationClass 注解class
     * @param attrName 注解属性名称
     * @param attrValue 注解属性值
     * @return 本工具类实例
     * @throws Exception 异常
     */
    public AnnotationUtil updateAnnoAttrValue(String fieldName, Class<? extends Annotation> annotationClass, String attrName, Object attrValue) throws Exception {
        Field[] declaredFields = this.clazz.getDeclaredFields();
        if(null != declaredFields && declaredFields.length != 0){
            for (int i=0;i<declaredFields.length;i++){
                Field declaredField = declaredFields[i];
                if(fieldName.equals(declaredField.getName())){
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(declaredField.getAnnotation(annotationClass));
                    Field hField = invocationHandler.getClass().getDeclaredField("memberValues");
                    hField.setAccessible(true);
                    Map memberValues  = (Map)hField.get(invocationHandler);
                    memberValues.put(attrName,attrValue);
                    break;
                }
            }
        }
        return this;
    }

/*    @SneakyThrows
    public static void main(String[] args) {
        *//*AnnotationUtil annotationUtil = new AnnotationUtil(FavoriteEntity.class);
        AnnotationUtil annoAttrValue = annotationUtil.updateAnnoAttrValue("content", Column.class, "columnDefinition", "CLOB");
        Class clazz = annoAttrValue.getClazz();
        Column column = (Column) clazz.getAnnotation(Column.class);
        String columnDefinition = column.columnDefinition();
        System.out.println(columnDefinition);*//*


        //获取Bar实例
        FavoriteEntity bar = new FavoriteEntity();
        //获取Bar的val字段
        Field field = FavoriteEntity.class.getDeclaredField("content");
        //获取val字段上的Foo注解实例
        Column column = field.getAnnotation(Column.class);
        ColumnDefinitionOracle definitionOracle = field.getAnnotation(ColumnDefinitionOracle.class);
        //获取 foo 这个代理实例所持有的 InvocationHandler
        InvocationHandler h = Proxy.getInvocationHandler(column);
        // 获取 AnnotationInvocationHandler 的 memberValues 字段
        Field hField = h.getClass().getDeclaredField("memberValues");
        // 因为这个字段事 private final 修饰，所以要打开权限
        hField.setAccessible(true);
        // 获取 memberValues
        Map memberValues = (Map) hField.get(h);
        // 修改 value 属性值
        memberValues.put("columnDefinition", definitionOracle.value());
        // 获取 foo 的 value 属性值
        String columnDefinition = column.columnDefinition();
        boolean nullable = column.nullable();
        System.out.println(columnDefinition); // aaa
        System.out.println(nullable); // false
    }*/
}
