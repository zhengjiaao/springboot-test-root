/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-23 14:40
 * @Since:
 */
package com.zja.postgis;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.jdbc.JDBCDataStoreFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author: zhengja
 * @since: 2023/10/23 14:40
 */
public class PostGISExample {
    public static void main(String[] args) {
        // Connection parameters
        String host = "localhost";
        int port = 5432;
        String database = "mydatabase";
        String schema = "public";
        String user = "myuser";
        String password = "mypassword";

        // Create connection parameters
        Map<String, Object> params = new HashMap<>();
        params.put(JDBCDataStoreFactory.DBTYPE.key, "postgis");
        params.put(JDBCDataStoreFactory.HOST.key, host);
        params.put(JDBCDataStoreFactory.PORT.key, port);
        params.put(JDBCDataStoreFactory.DATABASE.key, database);
        params.put(JDBCDataStoreFactory.SCHEMA.key, schema);
        params.put(JDBCDataStoreFactory.USER.key, user);
        params.put(JDBCDataStoreFactory.PASSWD.key, password);

        try {
            // Connect to the PostGIS database
            DataStore dataStore = DataStoreFinder.getDataStore(params);

            // Get a feature source and perform operations
            SimpleFeatureSource featureSource = dataStore.getFeatureSource("my_table");
            // ... 执行读取或写入操作 ...

            // Close the data store
            dataStore.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
