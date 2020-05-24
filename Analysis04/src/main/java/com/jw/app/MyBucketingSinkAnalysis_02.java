package com.jw.app;

import com.jw.app.map.FlowHourTransferMap;
import com.jw.app.map.FlowMap;
import com.jw.app.reduce.FlowReduce;
import com.jw.app.sink.FlowSink;
import com.jw.entity.FlowInfo;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.fs.Clock;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.Bucketer;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.hadoop.fs.Path;

import java.util.Properties;

public class MyBucketingSinkAnalysis_02 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.159.102:9092");
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

        BucketingSink<String> sink = new BucketingSink<String>("F:\\test");
        sink.setBucketer(new MyPathBucket());
        sink.setWriter(new StringWriter());
        sink.setBatchRolloverInterval(5 * 1000);

        source.addSink(sink);

        env.execute("MyBucketingSinkAnalysis_02");
    }
}
