package com.jw.app.advertising;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.app.map.AdvertisingTransferRatioMap;
import com.jw.app.map.AdvertisingTransferRatioMap2;
import com.jw.app.reduce.advertising.AdvertisingInfoReduce;
import com.jw.app.reduce.advertising.AdvertisingTransformRatioReduce2;
import com.jw.app.sink.advertising.AdvertisingInfoSink;
import com.jw.entity.AdvertisingInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.KafkaUtil2;
import com.jw.yewu.OrderInfo;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class AdvertisingTranferRatio_11 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(1);

        DataStream<String> dataInfoSource = env
                .addSource(new KafkaUtil2().getFlinkKafkaConsumer("dataInfo"))
                .map(new MapFunction<ConsumerRecord<String, String>, String>() {
                    @Override
                    public String map(ConsumerRecord<String, String> value) throws Exception {
                        return value.value();
                    }
                });

        DataStream<String> orderInfoSource = env
                .addSource(new KafkaUtil2().getFlinkKafkaConsumer("orderInfo"))
                .map(new MapFunction<ConsumerRecord<String, String>, String>() {
                    @Override
                    public String map(ConsumerRecord<String, String> value) throws Exception {
                        return value.value();
                    }
                });

        
        // 双流join
        DataStream<String> joinStream = dataInfoSource
                .join(orderInfoSource)
                .where(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        ScanPageLog scanPageLog = JSON.parseObject(value, ScanPageLog.class);
                        DeviceCommonInfo deviceCommonInfo = scanPageLog.getDeviceCommonInfo();
                        String userId = deviceCommonInfo.getUerId();
                        return userId;
                    }
                })
                .equalTo(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String value) throws Exception {
                        OrderInfo orderInfo = JSON.parseObject(value, OrderInfo.class);
                        Long userId = orderInfo.getUserId();
                        return String.valueOf(userId);
                    }
                })
                .window(TumblingProcessingTimeWindows.of(Time.hours(1)))
                .apply(new JoinFunction<String, String, String>() {
                    @Override
                    public String join(String first, String second) throws Exception {
                        ScanPageLog scanPageLog = JSON.parseObject(first, ScanPageLog.class);
                        OrderInfo orderInfo = JSON.parseObject(second, OrderInfo.class);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("scanPageLog", scanPageLog);
                        jsonObject.put("orderInfo", orderInfo);
                        return JSON.toJSONString(jsonObject);
                    }
                });

        // 拿到join的流之后，需要进行计算广告转化率的问题

        // 去重的问题
        DataStream<AdvertisingInfo> quchongStream = joinStream
                .map(new AdvertisingTransferRatioMap())
                .keyBy(new KeySelector<AdvertisingInfo, String>() {
                    @Override
                    public String getKey(AdvertisingInfo value) throws Exception {
                        return value.getGroupByFieldString();
                    }
                })
                .timeWindow(Time.hours(1L))
                .reduce(new AdvertisingInfoReduce());


        DataStream<AdvertisingInfo> reduce = quchongStream
                .map(new AdvertisingTransferRatioMap2())
                .keyBy(new KeySelector<AdvertisingInfo, String>() {
                    @Override
                    public String getKey(AdvertisingInfo value) throws Exception {
                        return value.getGroupByFieldString();
                    }
                })
                .timeWindow(Time.hours(1L))
                .reduce(new AdvertisingTransformRatioReduce2());

        reduce.addSink(new AdvertisingInfoSink());

        env.execute();
    }

}
