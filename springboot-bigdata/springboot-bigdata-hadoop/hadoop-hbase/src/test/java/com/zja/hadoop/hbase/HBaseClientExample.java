package com.zja.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2024/02/01 16:37
 */
public class HBaseClientExample {

    private static final String TABLE_NAME = "example_table";
    private static final String CF = "cf";
    private static final String QUALIFIER = "col1";

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        try (Connection connection = ConnectionFactory.createConnection(conf);
             Admin admin = connection.getAdmin()) {

            // 创建表
            createTable(admin);

            // 向表中插入数据
            putData(connection);

            // 从表中读取数据
            getData(connection);

            // 删除表
            deleteTable(admin);
        }
    }

    private static void createTable(Admin admin) throws IOException {
        TableName tableName = TableName.valueOf(TABLE_NAME);
        if (admin.tableExists(tableName)) {
            System.out.println("Table already exists: " + TABLE_NAME);
        } else {
            TableDescriptor tableDescriptor = TableDescriptorBuilder
                    .newBuilder(tableName)
                    .addColumnFamily(ColumnFamilyDescriptorBuilder.of(CF))
                    .build();
            admin.createTable(tableDescriptor);
            System.out.println("Table created: " + TABLE_NAME);
        }
    }

    private static void putData(Connection connection) throws IOException {
        TableName tableName = TableName.valueOf(TABLE_NAME);
        Table table = connection.getTable(tableName);

        Put put = new Put(Bytes.toBytes("row1"));
        put.addColumn(Bytes.toBytes(CF), Bytes.toBytes(QUALIFIER), Bytes.toBytes("value1"));
        table.put(put);

        System.out.println("Data inserted.");
    }

    private static void getData(Connection connection) throws IOException {
        TableName tableName = TableName.valueOf(TABLE_NAME);
        Table table = connection.getTable(tableName);

        Get get = new Get(Bytes.toBytes("row1"));
        Result result = table.get(get);

        Cell cell = result.getColumnLatestCell(Bytes.toBytes(CF), Bytes.toBytes(QUALIFIER));
        byte[] valueBytes = CellUtil.cloneValue(cell);
        String value = Bytes.toString(valueBytes);

        System.out.println("Retrieved data: " + value);
    }

    private static void deleteTable(Admin admin) throws IOException {
        TableName tableName = TableName.valueOf(TABLE_NAME);
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("Table deleted: " + TABLE_NAME);
        } else {
            System.out.println("Table does not exist: " + TABLE_NAME);
        }
    }
}
