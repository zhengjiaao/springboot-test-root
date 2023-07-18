/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-05-17 16:08
 * @Since:
 */
package com.zja.properties3;

import java.io.InputStream;

/**
 * 存储管理
 * @author: zhengja
 * @since: 2023/05/17 16:08
 */
public interface StorageManage {

    void putObject(String objectName, InputStream stream) throws Exception;

    InputStream getInputStream(String path);

}
