package com.dist.util.exception;

/**
 * @author tangyh
 */
public class OrderException extends Exception {
    public static final String OBJECT_NOT_FOUND = "源对象未找到";
    public static final String TARGET_NOT_FOUND = "目标位置未找到";
    public static final String INVALID_MOVE = "无效移动";
    public static final String POSITION_OUT_OF_BOUNDS = "排序字段越界";
    public static final String LOGICAL_INFINITE_LOOP = "逻辑死循环";

    public OrderException() {
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public static OrderException invalidMoveException(){
        return new OrderException(INVALID_MOVE);
    }

    public static OrderException objectNotFoundException(){
        return new OrderException(OBJECT_NOT_FOUND);
    }

    public static OrderException targetNotFoundException(){
        return new OrderException(TARGET_NOT_FOUND);
    }

    public static OrderException positionOutOfBoundsException(){
        return new OrderException(POSITION_OUT_OF_BOUNDS);
    }

    public static OrderException logicalInfiniteLoopException(){
        return new OrderException(LOGICAL_INFINITE_LOOP);
    }
}
