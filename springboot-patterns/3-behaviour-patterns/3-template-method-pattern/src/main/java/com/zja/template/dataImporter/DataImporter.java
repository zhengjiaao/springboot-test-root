/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:27
 * @Since:
 */
package com.zja.template.dataImporter;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:27
 */
// 抽象类
abstract class DataImporter {
    // 模板方法，定义了数据导入的骨架
    public final void importData() {
        connect();
        extractData();
        transformData();
        validateData();
        loadIntoDatabase();
        disconnect();
    }

    // 连接到数据源
    protected void connect() {
        System.out.println("Connecting to data source...");
    }

    // 从数据源提取数据
    protected abstract void extractData();

    // 转换数据
    protected abstract void transformData();

    // 验证数据
    protected abstract void validateData();

    // 将数据加载到数据库
    protected void loadIntoDatabase() {
        System.out.println("Loading data into database...");
    }

    // 断开与数据源的连接
    protected void disconnect() {
        System.out.println("Disconnecting from data source...");
    }
}