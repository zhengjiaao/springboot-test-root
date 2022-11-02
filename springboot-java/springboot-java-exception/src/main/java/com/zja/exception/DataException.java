package com.zja.exception;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 15:02
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义异常-当前记录不存在
 */
public class DataException extends Exception {

    private static String UN_RECORDED_EXCEPTION = "当前记录不存在";

    public DataException() {
        super(UN_RECORDED_EXCEPTION);
    }

    public DataException(String msg) {
        super(msg);
    }
}
