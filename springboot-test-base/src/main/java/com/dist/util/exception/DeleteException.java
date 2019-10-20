package com.dist.util.exception;

/**
 * 删除操作异常
 *
 * @author yinxp@dist.com.cn
 * @date 2018/12/11
 */
public class DeleteException extends Exception{
    private static final String OPERATE_DELETE_IS_ERROR = "删除数据出错";

    public DeleteException() {
        super(OPERATE_DELETE_IS_ERROR);
    }

    public DeleteException(String msg) {
        super(msg);
    }

}
