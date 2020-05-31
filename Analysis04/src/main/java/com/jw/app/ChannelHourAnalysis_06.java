package com.jw.app;

import com.jw.app.map.ChannelInfoMap;
import com.jw.app.map.HourTransferMap;
import com.jw.app.map.MinuteTransferMap;
import com.jw.app.reduce.ChannelInfoReduce;
import com.jw.app.sink.ChannelInfoSink;
import com.jw.entity.ChannelInfo;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

public class ChannelHourAnalysis_06 {
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
                "dataInfo",
                new SimpleStringSchema(),
                properties
        ));

        env.enableCheckpointing(5000);

        DataStream<String> transferMap = source.map(new HourTransferMap());

        DataStream<ChannelInfo> map = transferMap.map(new ChannelInfoMap());

        // 分组
        DataStream<ChannelInfo> reduce = map
                .keyBy(new KeySelector<ChannelInfo, String>() {
                    @Override
                    public String getKey(ChannelInfo value) throws Exception {
                        return value.getGroupByField();
                    }
                })
                .timeWindow(Time.minutes(5))
                .reduce(new ChannelInfoReduce());

        reduce.addSink(new ChannelInfoSink());

        source.print();

        env.execute("ChannelHourAnalysis_06");
    }
}
