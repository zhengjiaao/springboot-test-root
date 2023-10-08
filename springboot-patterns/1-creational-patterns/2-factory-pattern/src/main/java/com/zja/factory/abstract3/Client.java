/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:37
 * @Since:
 */
package com.zja.factory.abstract3;

/**
 * 客户端代码可以根据需要选择具体的工厂类，创建对应品牌的屏幕和处理器
 *
 * @author: zhengja
 * @since: 2023/10/08 9:37
 */
public class Client {
    public static void main(String[] args) {
        DeviceFactory huaweiFactory = new HuaweiFactory();
        Screen huaweiScreen = huaweiFactory.createScreen();
        Processor huaweiProcessor = huaweiFactory.createProcessor();

        huaweiScreen.display();      // 输出：华为屏幕显示
        huaweiProcessor.process();   // 输出：华为处理器运行

        DeviceFactory xiaomiFactory = new XiaomiFactory();
        Screen xiaomiScreen = xiaomiFactory.createScreen();
        Processor xiaomiProcessor = xiaomiFactory.createProcessor();

        xiaomiScreen.display();      // 输出：小米屏幕显示
        xiaomiProcessor.process();   // 输出：小米处理器运行
    }
}
