package com.zja.gis.sqlite;

import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-22 10:07
 */
public class UDBXReaderTest {

    static final String url = "jdbc:sqlite:D:\\temp\\udbx\\上海润和总部园.udbx";

    /**
     * 简单读取UDBX数据库
     */
    @Test
    public void test_1() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 加载SQLite JDBC驱动程序
            Class.forName("org.sqlite.JDBC");

            // 连接UDBX数据库
            // connection = DriverManager.getConnection("jdbc:sqlite:数据库文件路径.udbx");
            connection = DriverManager.getConnection(url);

            // 创建Statement对象
            statement = connection.createStatement();

            // 执行SQL查询
            String sql = "SELECT * FROM 上海润和总部园";
            resultSet = statement.executeQuery(sql);

            // 处理查询结果
            while (resultSet.next()) {
                // 读取属性数据
                String column1 = resultSet.getString("name");
                int column2 = resultSet.getInt("type");
                // ...

                System.out.println("Column 1: " + column1);
                System.out.println("Column 2: " + column2);
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
     * 带有条件的查询
     */
    @Test
    public void test_2() {
        try {
            // 连接到SQLite数据库
            Connection connection = DriverManager.getConnection(url);

            // 创建PreparedStatement对象
            String query = "SELECT * FROM 上海润和总部园 WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // 设置查询参数
            preparedStatement.setString(1, "张江润和总部园");

            // 执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            // 遍历结果集...
            while (resultSet.next()) {
                // 读取属性数据
                String column1 = resultSet.getString("name");
                int column2 = resultSet.getInt("type");
                // ...

                System.out.println("Column 1: " + column1);
                System.out.println("Column 2: " + column2);
                // ...
            }

            // 关闭连接...

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据分析操作
     */
    @Test
    public void test_3() {
        try {
            // 连接到SQLite数据库
            Connection connection = DriverManager.getConnection(url);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            // 执行数据分析操作，例如计算平均值
            String query = "SELECT AVG(column1) FROM 上海润和总部园";
            ResultSet resultSet = statement.executeQuery(query);

            // 读取结果
            if (resultSet.next()) {
                double averageValue = resultSet.getDouble(1);
                System.out.println("Average Value: " + averageValue);
            }

            // 关闭连接
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
