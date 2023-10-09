/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:19
 * @Since:
 */
package com.zja.template.beverage;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:19
 */
// 具体类A：咖啡
class Coffee extends Beverage {
    @Override
    void brew() {
        System.out.println("Brewing coffee...");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding sugar and milk...");
    }
}