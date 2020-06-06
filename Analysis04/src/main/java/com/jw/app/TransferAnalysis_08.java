package com.jw.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jw.app.map.ChannelInfoMap;
import com.jw.app.map.HourTransferMap;
import com.jw.app.reduce.ChannelInfoReduce;
import com.jw.app.sink.ChannelInfoSink;
import com.jw.entity.ChannelInfo;
import com.jw.utils.BinlogUtil;
import com.jw.utils.KafkaUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

public class TransferAnalysis_08 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.159.102:9092, 192.168.159.103:9092, 192.168.159.104:9092");
        properties.put("group.id", "dataInfoTest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("auto.offset.reset", "latest");

        DataStreamSource<String> source = env.addSource(new FlinkKafkaConsumer010<String>(
                "example",
                new SimpleStringSchema(),
                properties
        ).setStartFromLatest());

        env.enableCheckpointing(5000);

        DataStream<String> filterSource = source.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String value) throws Exception {
                /*{"data":[{"id":"1","productTypeName":"a","productTypeLevel":"1","parentId":"1"}],
                "database":"flink_clickhouse_analysis",
                "es":1591268000000,
                "id":2,
                "isDdl":false,
                "mysqlType":{"id":"int(20)","productTypeName":"varchar(255)","productTypeLevel":"int(12)","parentId":"int(20)"},"old":null,"sql":"","sqlType":{"id":4,"productTypeName":12,"productTypeLevel":4,"parentId":4},"table":"productType","ts":1591268001584,"type":"INSERT"}*/
                JSONObject jsonObject = JSONObject.parseObject(value);
                String type = jsonObject.getString("type");
                if ("INSERT".equals(type)) {
                    return true;
                }
                return false;
            }
        });

        DataStream<String> map = filterSource.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                JSONObject jsonObject = JSONObject.parseObject(value);

                /* 这里的tableName添加个prefix用于标记 */
                String tableName = "flink-clickhouse-" + jsonObject.getString("table").toLowerCase();
                JSONArray data = jsonObject.getJSONArray("data");
                JSONObject jsonString = data.getJSONObject(0);

                JSONObject res = new JSONObject();
                res.put("tableName", tableName);
                res.put("data", jsonString);

                // 通过反射将数据保存到HBase中
                BinlogUtil.transferAndInsert(tableName, jsonString.toJSONString());

                // 拼装的结果进行返回
                String s = res.toJSONString();
                return s;
            }
        });

        map.addSink(new SinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) throws Exception {
                JSONObject jsonObject = JSONObject.parseObject(value);
                String tableName = jsonObject.getString("tableName");
                String data = jsonObject.getString("data");
                KafkaUtil.sendData(tableName, data);
            }
        });


        env.execute("TransferAnalysis_08");
    }

}
