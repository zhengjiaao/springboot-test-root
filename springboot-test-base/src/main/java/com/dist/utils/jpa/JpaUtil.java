package com.dist.utils.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @desc 使用原生sql进行复杂查询时，返回List<Object[]>数据
 * 需要解析该数据，封装进对应的实体中
 * 例如：List<Object[]> objects = this.regionRepository.queryByParentRegionCodeV1(parentRegionCode);
 * List<RegionDTO> regions = JpaUtil.convertObjectArrayToBean(objects,RegionDTO.class);
 * @author dingchw
 * @date 2019/4/18.
 */
public class JpaUtil {
    private final static Logger LOG = LoggerFactory.getLogger(JpaUtil.class);

    /**
     * 将Object[]转换成实体bean
     * @param sourceObjList
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> List<T> convertObjectArrayToBean(List<Object[]> sourceObjList, Class targetType){
        List<T> resultList = new ArrayList<>();
        Constructor[] constructorArray = targetType.getConstructors();
        //分析出所有方法
        //解析实体类，获取构造方法
//        List<Class> fieldsTypeList = Arrays.stream(fields).map(Field::getType)
//                .collect(Collectors.toList());
        //获取原始数组的各类型，构造方法要和原始数据类型一一对应
        //
        if(null == sourceObjList || sourceObjList.size() == 0){
            return resultList;
        }
        Object[] fieldObject = sourceObjList.get(0);
        Constructor validConstructor = null;
        for(Constructor constructor : constructorArray){
            Parameter[] constructorParameterArray = constructor.getParameters();
            if(fieldObject.length == constructorParameterArray.length){
                validConstructor = constructor;
            }
        }
        if(null == validConstructor){
            try {
                throw new Exception("invalid Constructor for class [" + targetType.getName() + "]");
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        List<Class> fieldsTypeList = Arrays.asList(validConstructor.getParameterTypes());
        sourceObjList.forEach(item->{
            try {
                Class[] fieldTypeClasses = new Class[fieldsTypeList.size()];
                fieldsTypeList.toArray(fieldTypeClasses);
                Constructor<T> constructor = targetType.getConstructor(fieldTypeClasses);
                T t = constructor.newInstance(item);
                resultList.add(t);
            } catch (NoSuchMethodException e) {
                LOG.info("请检查类的构造方法是否满足转换规则...");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return resultList;
    }
}
