package com.jw.app.reduce;

import com.jw.entity.FlowMinuteInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class FlowMinuteReduce implements ReduceFunction<FlowMinuteInfo> {
    @Override
    public FlowMinuteInfo reduce(FlowMinuteInfo value1, FlowMinuteInfo value2) throws Exception {
        Long times = value1.getTimes();
        Long times1 = value2.getTimes();

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

        FlowMinuteInfo res = new FlowMinuteInfo();
        res.setGroupByField(value1.getGroupByField());
        res.setTimes(times + times1);
        res.setDeviceType(value1.getDeviceType());
        res.setTimeInfo(value1.getTimeInfo());
        res.setNewUserNum(newUserNum1 + newUserNum2);

        res.setHourActiveNums(hourActiveNums1 + hourActiveNums2);
        res.setDayActiveNums(dayActiveNums1 + dayActiveNums2);
        res.setWeekActiveNums(weekActiveNums1 + weekActiveNums2);
        res.setMonthActiveNums(monthActiveNums1 + monthActiveNums2);

        return res;
    }
}
