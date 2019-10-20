package com.dist.utils;

import com.dist.utils.annotation.Convert2Date;
import com.dist.utils.annotation.Convert2Entity;
import com.dist.utils.annotation.NotConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 取对象属性值、属性的getter方法名称和对象
 *
 * @author ShenYuTing
 *
 */

public class BeanUtils {
    @Autowired
    private static MessageSource messageSource;

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fielName) {
        Field fiel = getDeclaredField(object, fielName);
        if (fiel == null) {
            throwsfun(object, fielName);
        }
        makeAccessible(fiel);
        Object result = null;
        try {
            result = fiel.get(object);
        } catch (IllegalAccessException e) {
            throwsNoHappendException(e);
        }
        return result;
    }

    /**
     * checkstyle重复代码
     *
     * @author heshun
     * @param e
     */
    private static void throwsNoHappendException(IllegalAccessException e) {
        throw new RuntimeException("never happend exception!", e);
    }

    /**
     * checkstyle重复代码
     *
     * @author heshun
     * @param object
     * @param fieldName
     */
    private static void throwsfun(Object object, String fieldName) {
        throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throwsfun(object, fieldName);
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throwsNoHappendException(e);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
        Assert.notNull(object);
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("rawtypes")
    protected static Field getDeclaredField(final Class clazz, final String fieldName) {
        Assert.notNull(clazz);
        Assert.hasText(fieldName);
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                // TODO: A method should have only one exit point, and that
                // should be the last statement in the method
                return superClass.getDeclaredField(fieldName);
                // TODO: A method should have only one exit point, and that
                // should be the last statement in the method
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 强制转换fileld可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 获取参数属性的getter方法
     *
     * @param bean
     *            对象
     * @param propName
     *            对象中的属性
     * @return
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Object getSimpleProperty(Object bean, String propName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        try {
            return bean.getClass().getMethod(getReadMethod(propName)).invoke(bean);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            //            String booleanPropertyName = StringUtil.toUpperFirstChar(propName);
            return bean.getClass().getMethod(propName).invoke(bean);
        }
    }

    /**
     * 使用驼峰标识将getter在前与参数进行合并
     *
     * @param name
     *            参数名称
     * @return
     */
    private static String getReadMethod(String name) {
        return "get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    /**
     * 对象深克隆。<p>
     * 当对象中含有其他对象时，调用此方法进行对象的复制。
     * @param src 任意对象
     * @return 返回克隆后的新对象
     */
    public static Object deepClone(Object src) {
        if (src != null) {
            // 定义字节数组输出流
            ByteArrayOutputStream baos = null;
            // 定义对象输出流
            ObjectOutputStream oos = null;
            // 定义字节数组输入流
            ByteArrayInputStream bais = null;
            // 定义对象输入流
            ObjectInputStream ois = null;
            try {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                // 将对象写入内存中
                oos.writeObject(src);
                bais = new ByteArrayInputStream(baos.toByteArray());
                ois = new ObjectInputStream(bais);
                // 从内存中读出对象
                return ois.readObject();
            } catch (IOException e) {
                messageSource.getMessage("util.IOExceptionMsg", null, null);
            } catch (ClassNotFoundException e) {
                messageSource.getMessage("util.ClassNotFoundException", null, null);
            } finally {
                close(baos, oos, bais, ois);
            }
        }
        return src;
    }

    /**
     * 关闭多个流
     *
     * @param closeables  所需要关闭的多个流
     */
    private static void close(Closeable... closeables) {
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            messageSource.getMessage("util.IOException", null, null);
        }
    }

    /**
     * 将vo对象转换成domain对象，仅仅转换基本类型；复杂类型，需要人工重写后进行转换<br>
     * 要求vo与domain的属性名称一致时，类型必须一致，否则就会出错。
     *
     * @param src
     *            视图对象
     * @param target
     *            entity，new一个，在modify时，应该传入find的对象。
     * @param <T>
     *            aa
     * @return 转换成功的对象
     */
    public static <T> T convert(Object src, T target) {
        if (src == null || target == null) {
            throw new NullPointerException("vo与entity均不能为空");
        }
        for (Field vof : getAllFields(new HashSet<Field>(), src.getClass())) {
            for (Field ef : getAllFields(new HashSet<Field>(), target.getClass())) {
                if (vof.getName().equals(ef.getName())) {
                    if (!vof.getClass().equals(ef.getClass())) {
                        throw new IllegalArgumentException("vo与entity的属性名称相同，类型不同！");
                    }
                    vof.setAccessible(true);
                    ef.setAccessible(true);
                    try {

                        // 如果DTO标注了转换成date的注解，就转换dto的string为日期对象。
                        if (ef.isAnnotationPresent(Convert2Date.class)) {
                            // 获取注解的值
                            String format = ef.getAnnotation(Convert2Date.class).format();
                            Date srcDate = (Date) vof.get(src);

                            String dateStr = DateUtil.toStrWithFormat(srcDate, format);
                            ef.set(target, dateStr);
                        } else if (vof.isAnnotationPresent(Convert2Date.class)) {
                            // 如果添加了标记为 转换成date的注解，那么就进行转换
                            // 获取注解的值
                            String format = vof.getAnnotation(Convert2Date.class).format();
                            Date date = DateUtil.strToDate((String) vof.get(src), format);
                            ef.set(target, date);
                        } else if (vof.isAnnotationPresent(Convert2Entity.class) && vof.get(src) != null) {
                            Class<?> entityClazz = vof.getAnnotation(Convert2Entity.class).entityClass();
                            // 判断该属性是否为long
                            if (!vof.getType().equals(Long.class)) {
                                throw new IllegalAccessException(" '" + vof.getName() + "' field type must be Long!");
                            }
                            Object object = entityClazz.newInstance();

                            // 获取id属性
                            for (Field field : getAllFields(new HashSet<Field>(), entityClazz)) {
                                if (field.getName().equals("id")) {
                                    field.setAccessible(true);
                                    field.set(object, vof.get(src));
                                }
                            }
                            ef.set(target, object);
                        } else if (!vof.isAnnotationPresent(NotConvert.class)) {
                            ef.set(target, vof.get(src));
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return target;
    }

    /**
     * 获取该类所有的属性
     *
     * @param fields
     *            属性的集合
     * @param clazz
     *            类
     * @param <T>
     *            泛型
     * @return 属性的集合
     */
    public static <T> Set<Field> getAllFields(Set<Field> fields, Class<T> clazz) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            fields.addAll(getAllFields(fields, clazz.getSuperclass()));
        }
        return fields;
    }
}
