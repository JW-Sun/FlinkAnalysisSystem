package com.jw.app.reduce;

import com.jw.entity.ChannelInfo;
import com.jw.entity.FlowInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class ChannelInfoReduce implements ReduceFunction<ChannelInfo> {
    @Override
    public ChannelInfo reduce(ChannelInfo value1, ChannelInfo value2) throws Exception {
        Long times = value1.getTimes();
        Long times1 = value2.getTimes();

        /*++*/
        String groupByField = value1.getGroupByField();
        String deviceType = value1.getDeviceType();
        String timeInfo = value1.getTimeInfo();
        String channelInfoString = value1.getChannelInfo();

        ChannelInfo res = new ChannelInfo();
        res.setGroupByField(groupByField);
        res.setDeviceType(deviceType);
        res.setTimeInfo(timeInfo);
        res.setChannelInfo(channelInfoString);
        res.setTimes(times + times1);

        // 新增用户
        Long newUserNum1 = value1.getNewUserNum();
        Long newUserNum2 = value1.getNewUserNum();

        // 小时
        Long hourActiveNums1 = value1.getHourActiveNums();
        Long hourActiveNums2 = value2.getHourActiveNums();

        // 天
        Long dayActiveNums1 = value1.getDayActiveNums();
        Long dayActiveNums2 = value2.getDayActiveNums();

        // 周
        Long weekActiveNums1 = value1.getWeekActiveNums();
        Long weekActiveNums2 = value2.getWeekActiveNums();

        // 月
        Long monthActiveNums1 = value1.getMonthActiveNums();
        Long monthActiveNums2 = value2.getMonthActiveNums();

        // 活跃用户的数量
        Long userNums1 = value1.getUserNums();
        Long userNums2 = value2.getUserNums();

        /*++*/
        res.setNewUserNum(newUserNum1 + newUserNum2);
        res.setHourActiveNums(hourActiveNums1 + hourActiveNums2);
        res.setDayActiveNums(dayActiveNums1 + dayActiveNums2);
        res.setWeekActiveNums(weekActiveNums1 + weekActiveNums2);
        res.setMonthActiveNums(monthActiveNums1 + monthActiveNums2);


        return res;
    }
}
