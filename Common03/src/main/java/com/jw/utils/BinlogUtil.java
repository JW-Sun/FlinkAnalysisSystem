package com.jw.utils;

import com.alibaba.fastjson.JSONObject;
import com.jw.yewu.Product;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BinlogUtil {

    public static Map<String, String> tableEntityMap = new HashMap<>();

    static {
        tableEntityMap.put("flink-clickhouse-product", "com.jw.yewu.Product");
        tableEntityMap.put("flink-clickhouse-producttype", "com.jw.yewu.ProductType");
        tableEntityMap.put("flink-clickhouse-orderinfo", "com.jw.yewu.OrderInfo");
    }

    /***
     * 将数据插入到HBase中
     * @param tableName
     * @param data
     * @return
     */
    public static void  transferAndInsert(String tableName, String data) throws Exception {

        JSONObject jsonObject = JSONObject.parseObject(data);

        // 获得Id
        String id = jsonObject.getString("id");

        String className = tableEntityMap.get(tableName);

        // flink-clickhouse-product

        // 通过反射来加载这个类，获得类中的具体的方法。
        Class<?> clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> dataMap = new HashMap<>();

        for (Field field : fields) {
            String value = jsonObject.getString(field.getName());
            if (value != null) {
                System.out.println(field.getName() + " " + value);
                dataMap.put(field.getName(), value);
            }
        }
        // 循环结束进行插入操作。
        HBaseUtil.put(tableName, id, "info", dataMap);
    }

    public static void main(String[] args) throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setProductName("ads");
        String s = JSONObject.toJSONString(product);
        transferAndInsert("product", s);
    }

}
