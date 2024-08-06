package com.zja.mdb.jackcess;

import com.healthmarketscience.jackcess.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/**
 *
 * @Author: zhengja
 * @Date: 2024-08-05 17:55
 */
public class MDBReaderTest {

    static final String MDB_PATH = "D:\\temp\\mdb";

    @Test
    public void getTableAndColumn_test() {
        // 文件路径，支持多种格式
        String mdbFilePath = Paths.get(MDB_PATH, "1111.mdb").toString();

        try (Database database = DatabaseBuilder.open(new File(mdbFilePath))) {
            Set<String> tableNames = database.getTableNames();
            System.out.println("Table Names: " + tableNames);

            // 遍历所有表
            for (String tableName : tableNames) {
                Table table = database.getTable(tableName);
                System.out.println("Table: " + tableName);

                int columnCount = table.getColumnCount();
                System.out.println("Column Count: " + columnCount);

                List<? extends Column> tableColumns = table.getColumns();

                // 输出字段别名
                for (Column column : tableColumns) {
                    System.out.println("Column Name: " + column.getName());
                    // System.out.println("Column Type: " + column.getType());
                    // System.out.println("Column Length: " + column.getLength());

                    // String fieldAlias = column.getAlias(); // 不存在字段别名
                    // System.out.println("Field Name: " + column.getName() + ", Alias: " + fieldAlias);
                }

                System.out.println("--------------------");

                // 查询表中的所有数据
                for (Row row : table) {
                    for (Column column : table.getColumns()) {
                        System.out.print(column.getName() + ": " +   row.get(column.getName()) + " | ");
                    }
                    System.out.println();
                }

                System.out.println("--------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            String mdbFilePath = Paths.get(MDB_PATH, "1111.mdb").toString();
            // 打开数据库文件
            Database dbFile = DatabaseBuilder.open(new File(mdbFilePath));
            // 获取数据库中的所有表
            dbFile.getTableNames().forEach(tableName -> {
                System.out.println("Table name: " + tableName);
                try {
                    Table table = dbFile.getTable(tableName);
                    printFieldAliases(table);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 关闭数据库
            dbFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printFieldAliases(Table table) throws Exception {
        PropertyMap properties = table.getProperties();
        if (properties != null) {
            System.out.println("  Properties:");
            for (PropertyMap.Property property : properties) {
                System.out.println("Table    " + property.getName() + ": " + property.getValue());
            }
        }

        List<? extends Column> tableColumns = table.getColumns();
        for (Column column : tableColumns) {
            // 输出字段名称
            System.out.println("  Column name: " + column.getName());

            PropertyMap propertyMap = column.getProperties();
            if (propertyMap != null) {
                System.out.println("  Properties:");
                for (PropertyMap.Property property : propertyMap) {
                    System.out.println("Column    " + property.getName() + ": " + property.getValue());
                }
            }
        }
    }
}
