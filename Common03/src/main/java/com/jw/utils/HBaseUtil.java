package com.jw.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HBaseUtil {

    private static Admin admin;
    private static Connection connection;

    static {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.rootdir", "hdfs://192.168.159.102:9000/hbase");
        configuration.set("hbase.zookeeper.quorum", "192.168.159.102");
        configuration.set("hbase.client.scanner.timeout.period", "600000");
        configuration.set("hbase.rpc.timeout", "600000");
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 创建表
     * @param
     */
    public static void createTable(String table, String familyColumnName) {
        HTableDescriptor tableDescriptor = new HTableDescriptor(table);
        HColumnDescriptor columnDescriptor = new HColumnDescriptor(familyColumnName);
        tableDescriptor.addFamily(columnDescriptor);
        try {
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("create table");
    }

    /***
     * 添加
     * @param tableName
     * @param rowKey
     * @param familyColumnName
     * @param data
     * @throws IOException
     */
    public static void put(String tableName, String rowKey,
                           String familyColumnName, Map<String, Object> data) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        byte[] rowKeyByte = rowKey.getBytes();

        if (MapUtils.isNotEmpty(data)) {
            Set<Map.Entry<String, Object>> set = data.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                Put put = new Put(rowKeyByte);

                String key = entry.getKey();
                Object value = entry.getValue();
                put.addColumn(familyColumnName.getBytes(), key.getBytes(), String.valueOf(value).getBytes());

                table.put(put);
            }

            table.close();
            System.out.println("Put ok");
        }
    }

    /***
     * 获得HBase数据
     * @param tableName
     * @param rowKey
     * @param familyColName
     * @param col
     * @return
     */
    public static String get(String tableName, String rowKey, String familyColName, String col) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        byte[] rowKeyBytes = Bytes.toBytes(rowKey);
        Get get = new Get(rowKeyBytes);
        Result result = table.get(get);
        byte[] value = result.getValue(familyColName.getBytes(), col.getBytes());
        if (value == null) {
            return null;
        }
        return new String(value);
    }


    public static void insert(String tableName, String rowKey, String familyColName, String col, String data) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(rowKey.getBytes());
        put.addColumn(familyColName.getBytes(), col.getBytes(), data.getBytes());
        table.put(put);
    }

    public static void main(String[] args) throws IOException {
        // createTable("test_youfan", "time");

//        Map<String, Object> map = new HashMap<>();
//        map.put("col1", 1);
//        map.put("col2", 2);
//        put("test_youfan", "1", "time", map);

//        String s = get("test_youfan", "1", "time", "col1");
//        System.out.println(s);

        // insert("test_youfan", "3", "time", "col3", "333");

        Map<String, Object> map = new HashMap<>();
        map.put("col1", 1);
        map.put("col2", 2);
        put("flink-clickhouse-product", "1", "info", map);
    }
}
