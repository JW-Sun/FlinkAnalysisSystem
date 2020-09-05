package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.jw.entity.ChannelInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class ChannelUserDetailMap implements MapFunction<String, ChannelInfo> {
    @Override
    public ChannelInfo map(String data) throws Exception {

        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);

        String deviceType = scanPageLog.getDeviceType();

        DeviceCommonInfo deviceCommonInfo = scanPageLog.getDeviceCommonInfo();
        String userId = deviceCommonInfo.getUerId();

        String channelInfoString = deviceCommonInfo.getChannelInfo();

        // 次数，是否新增用户，是否活跃用户
        ChannelInfo res = new ChannelInfo();

        res.setUserId(userId);
        res.setChannelInfo(channelInfoString);
        res.setTimes(1L);
        if (deviceCommonInfo.isNew()) {
            res.setNewUserNum(1L);
        }

        if (deviceCommonInfo.isHourActive()) {
            res.setHourActiveNums(1L);
        }
        if (deviceCommonInfo.isDayActive()) {
            res.setDayActiveNums(1L);
        }
        if (deviceCommonInfo.isWeekActive()) {
            res.setWeekActiveNums(1L);
        }
        if (deviceCommonInfo.isMonthActive()) {
            res.setMonthActiveNums(1L);
        }

        String visitTime = scanPageLog.getVisitTime();
        String byInterHour = DateUtil.getByInterHour(visitTime);
        res.setTimeInfo(byInterHour);

        res.setGroupByField(userId + "==" + deviceType + "==" + byInterHour + "==" + channelInfoString);
        res.setDeviceType(deviceType);

        return res;
    }
}
