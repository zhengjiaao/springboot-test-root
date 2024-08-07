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
                System.out.println("Table: " + tableName); // GSZTGNQML

                // 输出表和字段的属性
                printTableAndColumnProperty(table);

                // 查询表中的所有数据
                for (Row row : table) {
                    for (Column column : table.getColumns()) {
                        System.out.print(column.getName() + ": " + row.get(column.getName()) + " | ");
                    }
                    System.out.println();
                }

                System.out.println("--------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                try {
                    Table table = dbFile.getTable(tableName);
                    printTableAndColumnProperty(table);
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

    private static void printTableAndColumnProperty(Table table) throws Exception {
        PropertyMap properties = table.getProperties();
        // 输出表名称
        System.out.println("Table name: " + table.getName());
        System.out.println("Table Column Count: " + table.getColumnCount());

        if (properties != null) {
            // 输出表属性
            for (PropertyMap.Property property : properties) {
                System.out.println("Table Properties " + property.getName() + ": " + property.getValue());
            }
        }

        List<? extends Column> tableColumns = table.getColumns();
        for (Column column : tableColumns) {
            // 输出字段名称
            System.out.println("Column name: " + column.getName());

            PropertyMap propertyMap = column.getProperties();
            if (propertyMap != null) {
                // 输出字段属性
                for (PropertyMap.Property property : propertyMap) {
                    System.out.println("Column Properties " + property.getName() + ": " + property.getValue());
                }
            }
        }
    }
}
