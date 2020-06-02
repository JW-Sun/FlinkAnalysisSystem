package com.jw.app.sink;

import com.jw.entity.ChannelInfo;
import com.jw.entity.FlowInfo;
import com.jw.utils.ClickHouseUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.HashMap;
import java.util.Map;

public class ChannelInfoSink implements SinkFunction<ChannelInfo> {

    @Override
    public void invoke(ChannelInfo in, Context context) throws Exception {
        String timeInfo = in.getTimeInfo();
        String deviceType = in.getDeviceType();
        String channelInfoString = in.getChannelInfo();
        Long times = in.getTimes();

        Long newUserNum = in.getNewUserNum();
        Long hourActiveNums = in.getHourActiveNums();
        Long dayActiveNums = in.getDayActiveNums();
        Long weekActiveNums = in.getWeekActiveNums();
        Long monthActiveNums = in.getMonthActiveNums();

        Long userNums = in.getUserNums();


        Map<String, String> map = new HashMap<>();
        map.put("timeInfo", timeInfo);
        map.put("deviceType", deviceType);
        map.put("channelInfo", channelInfoString);
        map.put("times", String.valueOf(times));

        map.put("newUserNum", String.valueOf(newUserNum));
        map.put("hourActiveNums", hourActiveNums + "");
        map.put("dayActiveNums", dayActiveNums + "");
        map.put("weekActiveNums", weekActiveNums + "");
        map.put("monthActiveNums", monthActiveNums + "");

        map.put("userNums", userNums + "");


        ClickHouseUtil.insert("ChannelInfo", map);
    }
}
