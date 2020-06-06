package com.jw.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaUtil {

    public static Properties getKafkaProp() {
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

    public static void sendData(String topic, String data) {
        Properties kafkaProp = getKafkaProp();
        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProp);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, data);

        Future<RecordMetadata> future = producer.send(record);
        try {
            RecordMetadata recordMetadata = future.get();
            // 得到发送消息的回调结果。
            System.out.println("Kafka消息发送成功。");
            System.out.println("topic: " + recordMetadata.topic());
            System.out.println("partition: " + recordMetadata.partition());
            System.out.println("offset: " + recordMetadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (producer != null) {
            producer.close();
        }
    }

    public static void main(String[] args) {
        // sendData("test", "aaaaaaaaaaaaaaaaaaaaaaaa");
    }

}
