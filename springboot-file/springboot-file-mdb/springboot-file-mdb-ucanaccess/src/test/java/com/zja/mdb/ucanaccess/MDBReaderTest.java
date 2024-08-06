package com.zja.mdb.ucanaccess;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 *
 * @Author: zhengja
 * @Date: 2024-08-06 9:35
 */
public class MDBReaderTest {

    static final String MDB_PATH = "D:\\temp\\mdb";

    /**
     * 动态从mdb文件中获取所有的表名
     */
    @Test
    public void getTables_test() {
        // 文件路径，支持多种格式
        String mdbFilePath = Paths.get(MDB_PATH, "1111.mdb").toString();
        // String mdbFilePath = "path/to/your/file.mdb";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbURL = "jdbc:ucanaccess://" + mdbFilePath;
            Connection conn = DriverManager.getConnection(dbURL);
            System.out.println(conn);

            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet tables = metadata.getTables(null, null, "%", null);
            while (tables.next()) {
                System.out.println(tables.getString(3));
            }
            tables.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态获取字段名以及每个字段对应的属性值
     */
    @Test
    public void test1() throws Exception {
        String mdbFilePath = Paths.get(MDB_PATH, "1111.mdb").toString();
        String mdbSql = "SELECT * FROM CZJSBSYQ";
        List<Map<String, Object>> list = resolverMdb(mdbFilePath, mdbSql);
        System.out.println(list.size());
    }

    /**
     * 动态获取字段及属性值
     *
     * @param mdbPath mdb文件路径
     * @param mdbSql mdb执行sql
     * @return
     * @throws Exception
     */
    private static List<Map<String, Object>> resolverMdb(String mdbPath, String mdbSql) throws Exception {
        if (mdbPath.isEmpty() || mdbSql.isEmpty()) {
            throw new Exception("mdb文件路径不能为空或者SQL语句不能为空或者返回字段列表不能为空");
        }
        List<Map<String, Object>> mdbEntityList = new ArrayList<>();
        Properties prop = new Properties();
        //设置编码
        prop.put("charSet", "UTF-8");
        //数据地址
        String dbUrl = "jdbc:ucanaccess://" + mdbPath;
        //引入驱动
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver").newInstance();
        try {
            //连接数据库资源
            Connection conn = DriverManager.getConnection(dbUrl, prop);
            //建立查询事务
            Statement statement = conn.createStatement();
            //执行查询
            ResultSet result = statement.executeQuery(mdbSql);

            ResultSetMetaData metaData = result.getMetaData();

            int count = metaData.getColumnCount();
            List<String> mdbColumnList = new ArrayList<String>(count);
            //动态解析字段名
            for (int i = 1; i <= count; i++) {
                mdbColumnList.add(metaData.getColumnName(i));
            }
            //解析执行结果
            Map<String, Object> mdbMapList = new HashMap<>(16);
            while (result.next()) {
                StringBuffer sb = new StringBuffer();
                for (String col : mdbColumnList) {
                    //System.out.println(result.getObject(col));
                    sb.append(col + "==" + result.getObject(col)).append("\t");
                    mdbMapList.put(col, result.getObject(col));
                }
                System.out.println(sb.toString());
                mdbEntityList.add(mdbMapList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回数据
        return mdbEntityList;
    }

    /**
     * 读取字段别名(没获取到别名)
     */
    @Test
    public void getColumnName_test() {

        // String mdbPath = "path/to/your/database.mdb"; // 替换为实际的数据库路径
        String mdbFilePath = Paths.get(MDB_PATH, "1111.mdb").toString();
        String query = "SELECT * FROM GSZTGNQML"; // 替换为实际的表名

        try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + mdbFilePath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + ": ");
                    System.out.print(rs.getString(i));
                    System.out.print(" (别名: " + rs.getMetaData().getColumnLabel(i) + ")"); // todo 貌似，没有读取到别名
                    System.out.println();
                }
                System.out.println("--------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
