package com.jw.app.advertising;

import com.jw.app.map.AdvertisingMap;
import com.jw.app.map.ChannelInfoMap;
import com.jw.app.map.MinuteTransferMap;
import com.jw.app.reduce.ChannelInfoReduce;
import com.jw.app.reduce.advertising.AdvertisingInfoReduce;
import com.jw.app.sink.ChannelInfoSink;
import com.jw.app.sink.advertising.AdvertisingInfoSink;
import com.jw.entity.AdvertisingInfo;
import com.jw.entity.ChannelInfo;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

public class AdvertisingMinuteAnalysis_08 {

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

        DataStream<String> transferMap = source.map(new MinuteTransferMap());

        DataStream<AdvertisingInfo> map = transferMap.map(new AdvertisingMap());

        // 分组
        DataStream<AdvertisingInfo> reduce = map
                .keyBy(new KeySelector<AdvertisingInfo, String>() {
                    @Override
                    public String getKey(AdvertisingInfo value) throws Exception {
                        return value.getGroupByFieldString();
                    }
                })
                .timeWindow(Time.minutes(5))
                .reduce(new AdvertisingInfoReduce());

        reduce.addSink(new AdvertisingInfoSink());

        source.print();

        env.execute("AdvertisingMinuteAnalysis_08");
    }

}
