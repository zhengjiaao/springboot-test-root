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
// 具体类A：从文件导入数据
class FileImporter extends DataImporter {
    @Override
    protected void extractData() {
        System.out.println("Extracting data from file...");
    }

    @Override
    protected void transformData() {
        System.out.println("Transforming data...");
    }

    @Override
    protected void validateData() {
        System.out.println("Validating data...");
    }
}