package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.jw.entity.FlowInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class FlowUserDetailMap implements MapFunction<String, FlowInfo> {
    @Override
    public FlowInfo map(String data) throws Exception {

        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);

        String deviceType = scanPageLog.getDeviceType();

        DeviceCommonInfo deviceCommonInfo = scanPageLog.getDeviceCommonInfo();
        String userId = deviceCommonInfo.getUerId();

        // 次数，是否新增用户，是否活跃用户
        FlowInfo flowInfo = new FlowInfo();
        flowInfo.setUserId(userId);
        flowInfo.setTimes(1L);
        if (deviceCommonInfo.isNew()) {
            flowInfo.setNewUserNum(1L);
        }

        if (deviceCommonInfo.isHourActive()) {
            flowInfo.setHourActiveNums(1L);
        }
        if (deviceCommonInfo.isDayActive()) {
            flowInfo.setDayActiveNums(1L);
        }
        if (deviceCommonInfo.isWeekActive()) {
            flowInfo.setWeekActiveNums(1L);
        }
        if (deviceCommonInfo.isMonthActive()) {
            flowInfo.setMonthActiveNums(1L);
        }

        String visitTime = scanPageLog.getVisitTime();
        String byInterHour = DateUtil.getByInterHour(visitTime);
        flowInfo.setTimeInfo(byInterHour);

        flowInfo.setGroupByField(userId + "==" + deviceType + "==" + byInterHour);
        flowInfo.setDeviceType(deviceType);

        return flowInfo;
    }
}
