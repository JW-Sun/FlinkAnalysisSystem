package com.jw.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.app.map.ChannelUserDetailMap;
import com.jw.app.map.FlowUserDetailMap;
import com.jw.app.reduce.ChannelInfoReduce;
import com.jw.app.reduce.FlowUserDetailReduce;
import com.jw.entity.ChannelInfo;
import com.jw.entity.FlowInfo;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.io.IOException;
import java.util.Properties;

public class ChannelHourUserDetailAnalysis_07 {
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
        ).setStartFromLatest());

        env.enableCheckpointing(5000);

        DataStream<ChannelInfo> map = source.map(new ChannelUserDetailMap());

        // 分组
        DataStream<ChannelInfo> reduce = map
                .keyBy(new KeySelector<ChannelInfo, String>() {
                    @Override
                    public String getKey(ChannelInfo value) throws Exception {
                        return value.getGroupByField();
                    }
                })
                .timeWindow(Time.hours(1L))
                .reduce(new ChannelInfoReduce());

        /* 将FlowInfo转换为JSONString */
        DataStream<String> channelInfoJsonString = reduce.map(new MapFunction<ChannelInfo, String>() {
            @Override
            public String map(ChannelInfo value) throws Exception {
                return JSON.toJSONString(value);
            }
        });

        // TODO 这里可以需要进行更改，转换成JsonString
        final StreamingFileSink sink = StreamingFileSink
                .forRowFormat(new Path("hdfs://192.168.159.102:9000/project/FlinkClickHouse/ChannelAnalysis"), new SimpleStringEncoder<String>("UTF-8"))
                .withBucketAssigner(new MyChannelUserDetailBucketAssigner())
                .withBucketCheckInterval(60 * 60 * 1000L)
                .build();

        channelInfoJsonString.addSink(sink);

        source.print();

        env.execute("FlowHourUserDetailAnalysis_04");
    }
}

class MyChannelUserDetailBucketAssigner implements BucketAssigner {

    @Override
    public Object getBucketId(Object element, Context context) {
        JSONObject jsonObject = JSON.parseObject(element.toString());
        if (jsonObject == null || !jsonObject.containsKey("timeInfo")) {
            return null;
        }
        String date = (String) jsonObject.get("timeInfo");
        // String format = DateUtil.getByMillions(date, "yyyyMMddHH");
        String res = date.substring(0, 8) + "/" + date.substring(8, 10);
        System.out.println("BucketAssigner: " + res);
        return res;
    }

    @Override
    public SimpleVersionedSerializer getSerializer() {
        return new MySerializationn_channel();
    }
}

class MySerializationn_channel implements SimpleVersionedSerializer<String> {

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public byte[] serialize(String obj) throws IOException {
        return obj.getBytes();
    }

    @Override
    public String deserialize(int version, byte[] serialized) throws IOException {
        if (version != 77) {
            throw new IOException("version mismatch");
        }
        return new String(serialized);
    }
}
