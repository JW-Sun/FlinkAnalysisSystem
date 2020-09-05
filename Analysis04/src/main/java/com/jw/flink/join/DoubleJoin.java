package com.jw.flink.join;

import com.jw.utils.KafkaUtil2;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static org.apache.flink.streaming.api.windowing.time.Time.seconds;

/*
*   test1: id, time, userId
*   test2:id, desc, userId
*   合并: id, time, desc, userId
* */

public class DoubleJoin {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());



        // 获得第一个Kafka流
        DataStream<String> source1 = env
                .addSource(new KafkaUtil2().getFlinkKafkaConsumer("test"))
                .map(new MapFunction<ConsumerRecord<String, String>, String>() {
                    @Override
                    public String map(ConsumerRecord<String, String> value) throws Exception {
                        return value.value();
                    }
                });

        // 获得第二个Kafka流
        DataStream<String> source2 = env
                .addSource(new KafkaUtil2().getFlinkKafkaConsumer("test1"))
                .map(new MapFunction<ConsumerRecord<String, String>, String>() {
                    @Override
                    public String map(ConsumerRecord<String, String> value) throws Exception {
                        return value.value();
                    }
                });


        // 双流join, 关联userId
        DataStream<String> joinStream = source1
                .join(source2)
                .where(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        String[] split = value.split(",");
                        return split[2];
                    }
                })
                .equalTo(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        return value.split(",")[2];
                    }
                })
                .window(TumblingProcessingTimeWindows.of(seconds(10)))
                .apply(new JoinFunction<String, String, String>() {
                    @Override
                    public String join(String first, String second) throws Exception {
                        String[] split1 = first.split(",");
                        String[] split2 = second.split(",");
                        String res = split1[2] + ", " + split1[1] + ", " + split2[1];
                        return res;
                    }
                });

        joinStream.print();
        env.execute();
    }
}
