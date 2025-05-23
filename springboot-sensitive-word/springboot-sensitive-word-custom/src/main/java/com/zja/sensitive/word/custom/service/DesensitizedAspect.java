package com.zja.sensitive.word.custom.service;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.zja.sensitive.word.custom.ac.SceneManager;
import com.zja.sensitive.word.custom.ac.SensitiveWordFilter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-10 13:39
 */
@Aspect
@Component
@Slf4j
public class DesensitizedAspect {

    private SceneManager sceneManager;

    public DesensitizedAspect(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * 返回通知（@AfterReturning）：在目标方法成功执行之后调用通知
     */
    @AfterReturning(pointcut = "@annotation(desensitization)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Desensitization desensitization, Object result) {
        if (Boolean.FALSE == desensitization.enable()) {
            return;
        }
        // 开始脱敏
        desensitized(result);
    }

    private void desensitized(Object result) {
        if (result instanceof PageData) {
            // 分页结果集
            PageData page = (PageData) result;
            List records = page.getData();
            for (Object record : records) {
                objReplace(record);
            }
        } else if (result instanceof List) {
            // 集合结果集
            List list = (List) result;
            for (Object obj : list) {
                objReplace(obj);
            }
        } else {
            // 对象结果集
            objReplace(result);
        }
    }

    public <T> void objReplace(T t) {
        try {
            Field[] declaredFields = ReflectUtil.getFields(t.getClass());
            for (Field field : declaredFields) {
                DesensitizedString desString = field.getAnnotation(DesensitizedString.class);
                // 处理字符串类型脱敏
                if (desString != null && "class java.lang.String".equals(field.getGenericType().toString())) {
                    Object fieldValue = ReflectUtil.getFieldValue(t, field);
                    if (fieldValue == null || StringUtils.isEmpty(fieldValue.toString())) {
                        continue;
                    }

                    String value = fieldValue.toString(); // 原始值
                    String newValue = ""; // 隐藏值

                    if (desString.type() == DesensitizedType.WORD) {
                        // 自定义敏感词库
                        SensitiveWordFilter filter = sceneManager.getSceneFilter(desString.wordBlackPath(), desString.wordWhitePath());
                        newValue = filter.replaceSensitiveWords(value, '*');
                    } else if (desString.type() == DesensitizedType.HIDE) {
                        // 自定义脱敏范围
                        int valueLength = value.length();
                        int startInclude = desString.hideStartInclude();
                        int endExclude = desString.hideEndExclude();
                        if (valueLength < startInclude) {
                            return;
                        }
                        if (endExclude == -1) {
                            endExclude = value.length();
                        }
                        newValue = StrUtil.hide(value, startInclude, endExclude);
                    } else if (desString.type() == DesensitizedType.ID_CARD) {
                        // 身份证号
                        newValue = DesensitizedUtil.idCardNum(value, desString.idCardNumFront(), desString.idCardNumBack());
                    } else if (desString.type() == DesensitizedType.ADDRESS) {
                        // 地址
                        newValue = DesensitizedUtil.address(value, desString.addressSensitiveSize());
                    } else {
                        // 其它，使用 hutool 默认脱敏规则
                        DesensitizedUtil.DesensitizedType type = DesensitizedUtil.DesensitizedType.valueOf(desString.type().toString());
                        newValue = DesensitizedUtil.desensitized(value, type);
                    }

                    // 设置隐藏值
                    ReflectUtil.setFieldValue(t, field, newValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String desensitizedWord(String text, String wordBlackPath, String wordWhitePath) {
        SensitiveWordFilter filter = sceneManager.getSceneFilter(wordBlackPath, wordWhitePath);
        return filter.replaceSensitiveWords(text, '*');
    }

}
