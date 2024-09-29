package com.zja.hanbian.封装.工具;

import com.zja.hanbian.封装.数据结构.布尔值;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 13:24
 */
public class 验证 {

    /**
     * 根据条件执行不同的操作并返回布尔值。
     * @param 条件 判断的条件
     * @param 真操作 当条件为真时执行的操作
     * @param 假操作 当条件为假时执行的操作
     * @return 返回条件的结果
     */
    public static 布尔值 执行条件操作(布尔值 条件, 操作 真操作, 操作 假操作) {
        if (条件.getValue()) {
            真操作.执行();
            return 布尔值.属于("真");
        } else {
            假操作.执行();
            return 布尔值.属于("假");
        }
    }

    // 定义接口
    public interface 操作 {
        void 执行();
    }
}
