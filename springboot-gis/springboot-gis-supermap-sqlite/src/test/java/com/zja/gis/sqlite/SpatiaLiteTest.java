package com.zja.gis.sqlite;

import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Properties;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-22 13:09
 */
@Deprecated // todo 弃用，sqlite3 无法启用 SpatiaLite扩展库
public class SpatiaLiteTest {

    static final String url = "jdbc:sqlite:D:\\temp\\udbx\\上海润和总部园.udbx";
    static final String mod_spatialite_path = "D:\\LocalDevEnv\\Middleware\\database\\sqlite-tools-dll-win-x64-3460000\\mod_spatialite";

    /**
     * 简单读取UDBX数据库
     */
    @Test
    public void test_1() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // Properties properties = new Properties();
        // properties.setProperty("enable_shared_cache", "true");
        // //开启扩展
        // properties.setProperty("enable_load_extension", "true");
        // //开启空间拓展
        // properties.setProperty("enable_spatialite", "true");

        // System.setProperty("mod_spatialite_path", mod_spatialite_path);

        try {
            // 加载SQLite JDBC驱动程序
            Class.forName("org.sqlite.JDBC");

            // 连接UDBX数据库
            // connection = DriverManager.getConnection("jdbc:sqlite:数据库文件路径.udbx");
            connection = DriverManager.getConnection(url);
            // connection.prepareStatement("SELECT load_extension('mod_spatialite')");

            // 创建Statement对象
            statement = connection.createStatement();
            // statement.execute("SELECT load_extension('mod_spatialite')"); // 扩展库 目录找不到

            // 执行SQL查询
            String sql = "SELECT * FROM 上海润和总部园";
            resultSet = statement.executeQuery(sql);

            // 处理查询结果
            while (resultSet.next()) {
                // 读取属性数据
                String column1 = resultSet.getString("name");
                int column2 = resultSet.getInt("type");
                String SmGeometry = resultSet.getString("SmGeometry");
                // Geometry SmGeometry = (Geometry)resultSet.getObject("SmGeometry", Geometry.class);
                // ...

                System.out.println("Column 1: " + column1);
                System.out.println("Column 2: " + column2);
                System.out.println("SmGeometry 3: " + SmGeometry);
                // ...
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 简单读取UDBX数据库
     */
    @Test
    public void test_2() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 加载SQLite JDBC驱动程序
            Class.forName("org.sqlite.JDBC");

            // System.setProperty("java.library.path", "D:\\LocalDevEnv\\Middleware\\database\\sqlite-tools-dll-win-x64-3460000\\mod_spatialite");
            // System.setProperty("java.library.path", "C:\\Windows\\System32\\mod_spatialite");

            // 连接UDBX数据库
            // connection = DriverManager.getConnection("jdbc:sqlite:数据库文件路径.udbx");
            // connection = DriverManager.getConnection(url);

            // Properties prop = new Properties();
            // prop.setProperty("enable_load_extension", "true");
            // connection = DriverManager.getConnection(url,prop);

            SQLiteConfig config = new SQLiteConfig();
            config.enableLoadExtension(true);
            connection = config.createConnection(url);

            // 创建Statement对象
            statement = connection.createStatement();
            statement.execute("SELECT load_extension('mod_spatialite')");

            // 执行SQL查询
            String sql = "SELECT name, ST_AsText(smgeometry) AS smgeometry FROM '上海润和总部园'";
            resultSet = statement.executeQuery(sql);

            // 处理查询结果
            while (resultSet.next()) {
                // 读取属性数据
                String column1 = resultSet.getString("name");
                String SmGeometry = resultSet.getString("smgeometry");
                // Geometry SmGeometry = (Geometry)resultSet.getObject("SmGeometry", Geometry.class);
                // ...

                System.out.println("Column 1: " + column1);
                System.out.println("SmGeometry 3: " + SmGeometry);
                // ...
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
