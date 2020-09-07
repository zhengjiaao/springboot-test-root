package com.dist.utils.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Date: 2019-12-03 14:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Schema处理器
 */
@Slf4j
@Component
public class SchemaHandler {

    //private final String SCHEMA_SQL = "classpath:schema.sql";

    @Autowired
    private DataSource datasource;

    @Autowired
    private SpringContextGetter springContextGetter;

    /**
     * 执行判断数据源连接池
     */
    public void execute() throws Exception {
        //druid 判断数据库是mysql/oracle
        /*DruidDataSource druidDataSource = (DruidDataSource) this.datasource;
        Driver driver = druidDataSource.getDriver();
        if (driver instanceof com.mysql.jdbc.Driver) {
            executeSqlFile("mysql");
        }
        if (driver instanceof com.mysql.cj.jdbc.Driver) {
            executeSqlFile("mysql");
        }
        if (driver instanceof oracle.jdbc.OracleDriver){
            executeSqlFile("oracle");
        }
        if (driver instanceof oracle.jdbc.driver.OracleDriver){
            executeSqlFile("oracle");
        }*/

        //判断是c3p0/druid 连接池
        if (datasource instanceof DruidDataSource){
            DruidDataSource druidDataSource = (DruidDataSource) this.datasource;
            judgeDriver(druidDataSource.getDriverClassName());
        }
        if (datasource instanceof ComboPooledDataSource){
            ComboPooledDataSource comboPooledDataSource = (ComboPooledDataSource) this.datasource;
            judgeDriver(comboPooledDataSource.getDriverClass());
        }
    }

    /**
     * 根据驱动判断是mysql/oracle的.sql文件
     * @param driverClassName 驱动名称
     */
    private void judgeDriver(String driverClassName) throws SQLException, IOException {
        if (driverClassName.equals("com.mysql.jdbc.Driver") || driverClassName.equals("com.mysql.cj.jdbc.Driver")){
            executeSqlFile("mysql");
        }
        if (driverClassName.equals("oracle.jdbc.OracleDriver") || driverClassName.equals("oracle.jdbc.driver.OracleDriver")){
            executeSqlFile("oracle");
        }
    }

    /**
     * 执行sql文件
     * @param dbname mysql/oracle
     */
    private void executeSqlFile(String dbname) throws SQLException, IOException {

        File file =  ResourceUtils.getFile("classpath:db"+File.separator+dbname);
        if (!file.exists()){
            log.info("不存在【 "+"classpath:db"+File.separator+dbname+"】文件");
            return;
        }
        File[] files = file.listFiles();
        if (dbname.equals("oracle")){
            for (File f : files){
                String sqlRelativePath = "classpath:db"+File.separator+dbname+File.separator+f.getName();
                Resource resource = springContextGetter.getApplicationContext().getResource(sqlRelativePath);
                //一条sql语句以"$$"结尾区分.执行oralce的存储过程 将'declare countCol number;'当初一条sql执行爆错,因默认以";"结尾是一条sql语句,更改成以"$$"分割作为一条sql语句
                ScriptUtils.executeSqlScript(this.datasource.getConnection(), new EncodedResource(resource,"UTF-8"), false, false, "--", "$$", "/*", "*/");
                System.out.println("执行: "+dbname+"/"+f.getName());
            }
        }
        if (dbname.equals("mysql")){
            for (File f : files){
                String sqlRelativePath = "classpath:db"+File.separator+dbname+File.separator+f.getName();
                Resource resource = springContextGetter.getApplicationContext().getResource(sqlRelativePath);
                //一条sql语句,默认以";"结尾区分
                ScriptUtils.executeSqlScript(this.datasource.getConnection(), new EncodedResource(resource,"UTF-8"));
                System.out.println("执行: "+dbname+"/"+f.getName());
            }
        }
    }
}
