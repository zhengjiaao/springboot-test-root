/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:28
 * @Since:
 */
package com.zja.template.dataImporter;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:28
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        DataImporter fileImporter = new FileImporter();
        fileImporter.importData();

        System.out.println();

        DataImporter databaseImporter = new DatabaseImporter();
        databaseImporter.importData();

        //输出结果：
        //Connecting to data source...
        //Extracting data from file...
        //Transforming data...
        //Validating data...
        //Loading data into database...
        //Disconnecting from data source...
        //
        //Connecting to data source...
        //Extracting data from database...
        //Transforming data...
        //Validating data...
        //Loading data into database...
        //Disconnecting from data source...
    }
}
