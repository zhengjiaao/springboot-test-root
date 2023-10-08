/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:35
 * @Since:
 */
package com.zja.factory.abstract3;

/**
 * @author: zhengja
 * @since: 2023/10/08 9:35
 */
// 小米处理器产品
class XiaomiProcessor implements Processor {
    @Override
    public void process() {
        System.out.println("小米处理器运行");
    }
}
