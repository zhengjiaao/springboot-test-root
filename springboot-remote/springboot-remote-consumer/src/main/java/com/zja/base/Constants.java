package com.zja.base;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-05-19 11:39
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class Constants {

    /**
     * 第三方 特力惠 组织机构、部门、用户同步 数据同步
     */
    public enum OperationTypeEnum {

        NEW_DATA(1, "新增数据"),
        CHANGE_DATA(2, "修改数据"),
        DELETE_DATA(3, "删除数据");

        private Integer code;
        private String desc;

        OperationTypeEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer code() {
            return code;
        }

        public String desc() {
            return desc;
        }

        public static OperationTypeEnum getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (OperationTypeEnum typeEnum : OperationTypeEnum.values()) {
                if (typeEnum.code.equals(code)) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    /**
     * 第三方 特力惠 组织机构、部门、用户同步 数据同步
     */
    public enum BusinessTypeEnum {

        ORGANIZATION(1, "组织机构"),
        DEPARTMENT(2, "部门"),
        USER(3, "用户");

        private Integer code;
        private String desc;

        BusinessTypeEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer code() {
            return code;
        }

        public String desc() {
            return desc;
        }

        public static BusinessTypeEnum getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (BusinessTypeEnum typeEnum : BusinessTypeEnum.values()) {
                if (typeEnum.code.equals(code)) {
                    return typeEnum;
                }
            }
            return null;
        }
    }
}
