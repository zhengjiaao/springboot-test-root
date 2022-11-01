package com.zja.base;

import com.zja.UnittestJunitApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 单元测试基类
 *
 * @author dingchw
 * @desc 统一入口，如此便不用多次使用注解、域的设置以及日志的手动记录
 * @date 2018/11/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UnittestJunitApplication.class})
public class BaseTest {
    private static Logger log = LoggerFactory.getLogger(BaseTest.class);
    private String className;

    @Before
    public void init() {

    }

    protected void setLog(Logger log) {
        this.log = log;
    }

    protected void setClassName(String className) {
        this.className = className;
    }

    protected <T> void sayEntity(T entity, String entityName, String methodName) {
        if (entity != null) {
            log.info("The method(" + methodName + ") in class(" + className + ") success!");
            log.info(entityName + "：" + entity.toString());
        } else {
            log.error("An error in class(" + className + ") of method(" + methodName + ")：entity(" + entityName + ") is null");
        }
    }

    protected <T> void sayList(List<T> list, String listName, String methodName) {
        if (list == null || list.size() <= 0) {
            log.warn("The list(" + listName + ") of method(" + methodName + ") in class(" + className + ") is empty!");
        } else {
            log.info("The method(" + methodName + ") in class(" + className + ") success!");
            for (int i = 0; i < list.size(); i++) {
                log.info(String.valueOf(list.size()));
                log.info(listName + "[" + i + "]： " + list.get(i).toString());
            }
        }
    }

    protected <T> void saySet(Set<T> set, String setName, String methodName) {
        if (set == null || set.size() <= 0) {
            log.warn("The set(" + setName + ") of method(" + methodName + ") in class(" + className + ") is empty!");
        } else {
            log.info("The method(" + methodName + ") in class(" + className + ") success!");
            int i = 0;
            for (T s : set) {
                log.info(setName + "[" + (i++) + "]： " + s.toString());
            }
        }
    }

    protected void sayFlag(Boolean flag, String methodName) {
        if (flag) {
            log.info("The method(" + methodName + ") in class(" + className + ") success!");
        } else {
            log.error("An error in " + className + " of " + methodName + "：flag is false");
        }
    }

    protected void sayStr(String str, String methodName) {
        log.info("The method(" + methodName + ") in class(" + className + ") success!");
        log.info("输出结果：" + str);
    }

    protected <T> void sayMap(Map<String, T> map, String mapName, String methodName) {
        if (map == null || map.size() <= 0) {
            log.warn("The map(" + mapName + ") of method(" + methodName + ") in class(" + className + ") is empty!");
        } else {
            log.info("The method(" + methodName + ") in class(" + className + ") success!");
            int i = 0;
            for (Map.Entry<String, T> m : map.entrySet()) {
                if (m.getValue() == null) {
                    log.info(mapName + "[" + (i++) + "]： key=" + m.getKey() + ", value=null");
                } else {
                    log.info(mapName + "[" + (i++) + "]： key=" + m.getKey() + ", value=" + m.getValue().toString());
                }
            }
        }
    }

    protected void sayException(Exception e, String methodName) {
        log.error("An exception in class(" + className + ") of method(" + methodName + ")!", e);
    }

    protected <T> void sayIterable(Iterable<T> iterable, String iterableName, String methodName) {
        if (iterable == null) {
            log.warn("The iterable(" + iterableName + ") of method(" + methodName + ") in class(" + className + ") is null!");
        } else {
            Iterator iterator = iterable.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                T object = (T) iterator.next();
                log.info(iterableName + "[" + (i++) + "]：" + object.toString());
            }
            if (i == 0) {
                log.warn("The iterable(" + iterableName + ") of method(" + methodName + ") in class(" + className + ") is empty!");
            }
        }
    }
}
