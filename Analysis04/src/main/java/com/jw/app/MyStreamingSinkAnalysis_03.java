package com.jw.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.io.IOException;
import java.util.Properties;

public class MyStreamingSinkAnalysis_03 {
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
        ).setStartFromLatest());


        env.enableCheckpointing(5000);

        final StreamingFileSink sink = StreamingFileSink
                .forRowFormat(new Path("F:\\output\\streamingFileSink"), new SimpleStringEncoder<String>("UTF-8"))
                .withBucketAssigner(new MyBucketAssigner())
                .withBucketCheckInterval(6 * 1000L)
                .build();

        source.addSink(sink);


        env.execute("MyStreamingSinkAnalysis");
    }
}

class MyBucketAssigner implements BucketAssigner {

    @Override
    public Object getBucketId(Object element, Context context) {
        JSONObject jsonObject = JSON.parseObject(element.toString());
        if (jsonObject == null || !jsonObject.containsKey("visitTime")) {
            return null;
        }
        String date = (String) jsonObject.get("visitTime");
        String format = DateUtil.getByMillions(date, "yyyyMMddHH");
        return format;
    }

    @Override
    public SimpleVersionedSerializer getSerializer() {
        return new MySerialization();
    }
}

class MySerialization implements SimpleVersionedSerializer<String> {

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
