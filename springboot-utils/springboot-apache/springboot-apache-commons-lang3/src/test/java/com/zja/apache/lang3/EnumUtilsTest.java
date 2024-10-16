package com.zja.apache.lang3;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-16 13:42
 */
public class EnumUtilsTest {

    enum EnumClassTest {
        test1, test2, test3
    }

    @Getter
    enum FeatureTypeEnum {

        POINT("点"),

        LINESTRING("线"),

        POLYGON("面");

        private String desc;

        FeatureTypeEnum(String desc) {
            this.desc = desc;
        }

        public static FeatureTypeEnum getTypeByDesc(String desc) {
            for (FeatureTypeEnum value : FeatureTypeEnum.values()) {
                if (StringUtils.equals(value.desc, desc)) {
                    return value;
                }
            }
            return null;
        }

    }

    @Getter
    enum CommonGroupTypeEnum {
        OWN(0, false, "个人"),
        SHARE(1, true, "全局");

        private final Integer code;

        private final Boolean needAdmin;

        private final String desc;

        CommonGroupTypeEnum(Integer code, boolean needAdmin, String desc) {
            this.code = code;
            this.needAdmin = needAdmin;
            this.desc = desc;
        }

        public static CommonGroupTypeEnum getByCode(int code) {
            for (CommonGroupTypeEnum value : CommonGroupTypeEnum.values()) {
                if (value.code == code) {
                    return value;
                }
            }
            return null;
        }
    }

    @Test
    public void _test1() {
        // 判断
        System.out.println(EnumUtils.isValidEnum(EnumClassTest.class, "test1")); // true
        System.out.println(EnumUtils.isValidEnumIgnoreCase(EnumClassTest.class, "test2")); // true

        // 获取枚举
        System.out.println(EnumUtils.getEnum(EnumClassTest.class, "test1")); // test1
        System.out.println(EnumUtils.getEnumIgnoreCase(EnumClassTest.class, "test1")); // test1
        System.out.println(EnumUtils.getEnum(EnumClassTest.class, "test1", EnumClassTest.test3)); // test1
        System.out.println(EnumUtils.getEnum(EnumClassTest.class, "test5", EnumClassTest.test3)); // test3
        System.out.println(EnumUtils.getEnum(EnumClassTest.class, "test5")); // null

        // 枚举列表
        System.out.println(EnumUtils.getEnumList(EnumClassTest.class)); // [test1, test2, test3]
        System.out.println(EnumUtils.getEnumMap(EnumClassTest.class)); // {test2=test2, test3=test3, test1=test1}
    }

    @Test
    public void _test2() {
        System.out.println(FeatureTypeEnum.getTypeByDesc("点")); // POINT
        System.out.println(FeatureTypeEnum.getTypeByDesc("线")); // LINESTRING
        System.out.println(FeatureTypeEnum.getTypeByDesc("面")); // POLYGON
        System.out.println(FeatureTypeEnum.getTypeByDesc("面面")); // null

        System.out.println(EnumUtils.isValidEnum(FeatureTypeEnum.class, "点")); // false
        System.out.println(EnumUtils.isValidEnumIgnoreCase(FeatureTypeEnum.class, "点")); // false
        System.out.println(EnumUtils.isValidEnum(FeatureTypeEnum.class, "点面面")); // false

        System.out.println(EnumUtils.getEnum(FeatureTypeEnum.class, "点")); // null
        System.out.println(EnumUtils.getEnumIgnoreCase(FeatureTypeEnum.class, "点"));// null
        System.out.println(EnumUtils.getEnum(FeatureTypeEnum.class, "点", FeatureTypeEnum.POLYGON)); // POLYGON
        System.out.println(EnumUtils.getEnum(FeatureTypeEnum.class, "点面面")); // null

        System.out.println(EnumUtils.getEnumList(FeatureTypeEnum.class)); // [POINT, LINESTRING, POLYGON]
        System.out.println(EnumUtils.getEnumMap(FeatureTypeEnum.class)); // {POLYGON=POLYGON, LINESTRING=LINESTRING, POINT=POINT}
    }

    @Test
    public void _test3() {
        System.out.println(CommonGroupTypeEnum.getByCode(0)); // OWN
        System.out.println(CommonGroupTypeEnum.getByCode(1)); // SHARE
        System.out.println(CommonGroupTypeEnum.getByCode(2)); // null
        System.out.println(CommonGroupTypeEnum.getByCode(3)); // null

        System.out.println(EnumUtils.isValidEnum(CommonGroupTypeEnum.class, "0")); // false
        System.out.println(EnumUtils.isValidEnumIgnoreCase(CommonGroupTypeEnum.class, "0")); // false
        System.out.println(EnumUtils.isValidEnum(CommonGroupTypeEnum.class, "OWN")); // true
        System.out.println(EnumUtils.isValidEnumIgnoreCase(CommonGroupTypeEnum.class, "OWN")); // true

        System.out.println(EnumUtils.getEnum(CommonGroupTypeEnum.class, "0")); // null
        System.out.println(EnumUtils.getEnumIgnoreCase(CommonGroupTypeEnum.class, "0")); // null
        System.out.println(EnumUtils.getEnum(CommonGroupTypeEnum.class, "OWN")); // OWN
        System.out.println(EnumUtils.getEnumIgnoreCase(CommonGroupTypeEnum.class, "OWN")); // OWN

        System.out.println(EnumUtils.getEnumList(CommonGroupTypeEnum.class)); // [OWN, SHARE]
        System.out.println(EnumUtils.getEnumMap(CommonGroupTypeEnum.class)); // {OWN=OWN, SHARE=SHARE}
    }

}
