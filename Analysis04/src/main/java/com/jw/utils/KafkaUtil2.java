package com.jw.utils;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.Serializable;
import java.util.Properties;

public class KafkaUtil2 implements Serializable {

    public static Properties getConsumerProp() {
        Properties prop = new Properties();
        // prop.put("bootstrap.servers", "192.168.159.102:9092, 192.168.159.103:9092, 192.168.159.104:9092");
        prop.put("bootstrap.servers", "k1:9092");
        prop.put("group.id", "test02_group");
        prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("auto.offset.reset", "latest");

        return prop;
    }

    public FlinkKafkaConsumer011<ConsumerRecord<String, String>> getFlinkKafkaConsumer(String topic) {
        Properties prop = getConsumerProp();

        FlinkKafkaConsumer011<ConsumerRecord<String, String>> stringFlinkKafkaConsumer = new FlinkKafkaConsumer011<>(
                topic,
                // new SimpleStringSchema(),
               new CustomDeSerializationSchema(),
                prop
        );
        return stringFlinkKafkaConsumer;
    }

    private static Properties getProducerProp() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.159.102:9092, 192.168.159.103:9092, 192.168.159.104:9092");
        props.put("acks", "all"); // 发送所有ISR
        props.put("retries", 2); // 重试次数
        props.put("batch.size", 16384); // 批量发送大小
        props.put("buffer.memory", 33554432); // 缓存大小，根据本机内存大小配置
        props.put("linger.ms", 1000); // 发送频率，满足任务一个条件发送
        props.put("client.id", "producer-syn-1"); // 发送端id,便于统计
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    public FlinkKafkaProducer011<String> getFlinkKafkaProducer(String topic) {
        FlinkKafkaProducer011<String> stringFlinkKafkaProducer011 = new FlinkKafkaProducer011<>(
                "192.168.159.102:9092, 192.168.159.103:9092, 192.168.159.104:9092",
                topic,
                new SimpleStringSchema()
        );
        return stringFlinkKafkaProducer011;
    }


    /***
     *  自定义kafka序列化
     */
    class CustomDeSerializationSchema implements KafkaDeserializationSchema<ConsumerRecord<String, String>> {

        /***
         * 表示是否是最后一条元素，设置为false，表示数据的源源不断的到达。
         * @param stringStringConsumerRecord
         * @return
         */
        @Override
        public boolean isEndOfStream(ConsumerRecord<String, String> stringStringConsumerRecord) {
            return false;
        }

        // 返回一个ConsumerRecord<String, String>类型的数据，包含topic，partition，offset等信息。
        @Override
        public ConsumerRecord<String, String> deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {

            String key = "";
            String value = "";
            if (consumerRecord.key() != null) {
                key = new String(consumerRecord.key());
            }
            if (consumerRecord.value() != null) {
                value = new String(consumerRecord.value());
            }

            return new ConsumerRecord<String, String>(
                    consumerRecord.topic(),
                    consumerRecord.partition(),
                    consumerRecord.offset(),
                    key,
                    value
            );
        }

        // 指定输入的类型：
        @Override
        public TypeInformation<ConsumerRecord<String, String>> getProducedType() {
            return TypeInformation.of(new TypeHint<ConsumerRecord<String, String>>() {});
        }
    }


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        FlinkKafkaConsumer011<ConsumerRecord<String, String>> source = new KafkaUtil2().getFlinkKafkaConsumer("test");

        DataStreamSource<ConsumerRecord<String, String>> sourceStream = env.addSource(source);

        sourceStream.map(new MapFunction<ConsumerRecord<String, String>, String>() {
            @Override
            public String map(ConsumerRecord<String, String> value) throws Exception {
                System.out.println("topic: " + value.topic());
                System.out.println("partition: " + value.partition());
                System.out.println("offset: " + value.offset());

                return value.value();
            }
        }).print();

        env.execute();
    }

}
