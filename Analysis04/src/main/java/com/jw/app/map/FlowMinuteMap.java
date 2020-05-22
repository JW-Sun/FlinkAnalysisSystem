package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jw.entity.FlowMinuteInfo;
import com.jw.input.AppInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class FlowMinuteMap implements MapFunction<String, FlowMinuteInfo> {
    @Override
    public FlowMinuteInfo map(String data) throws Exception {

        JSONObject jsonObject = JSON.parseObject(data);
        String deviceType = jsonObject.getString("deviceType");
        String deviceCommonInfoStr = jsonObject.getString("deviceCommonInfo");
        DeviceCommonInfo deviceCommonInfo = JSON.parseObject(deviceCommonInfoStr, DeviceCommonInfo.class);


        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);
        String visitTime = scanPageLog.getVisitTime();
        // 五分钟的时间间隔问题
        String interMinute = DateUtil.getByInterMinute(visitTime);

        FlowMinuteInfo res = new FlowMinuteInfo();
        res.setTimeInfo(interMinute);
        res.setDeviceType(deviceType);
        // 分组字段的选择就是通过拼接字符串的形式进行。
        res.setGroupByField(interMinute + "==" + deviceType);
        res.setTimes(1L);

        // 如果是新用户，就设置为1.
        if (deviceCommonInfo.isNew()) {
            res.setNewUserNum(1L);
        }

        // 小时活跃状态
        if (deviceCommonInfo.isHourActive()) {
            res.setHourActiveNums(1L);
        }

        // 天活跃状态
        if (deviceCommonInfo.isDayActive()) {
            res.setDayActiveNums(1L);
        }

        // 月活跃状态
        if (deviceCommonInfo.isMonthActive()) {
            res.setMonthActiveNums(1L);
        }

        // 周活跃状态
        if (deviceCommonInfo.isWeekActive()) {
            res.setWeekActiveNums(1L);
        }

        // 0:app 1：pc  2:小程序端
        /*if ("0".equals(deviceType)) {

            AppInfo appInfo = JSONObject.parseObject(deviceCommonInfo, AppInfo.class);
            scanPageLog.setDeviceCommonInfo(appInfo);

        } else if ("1".equals(deviceType)) {



        } else if ("2".equals(deviceType)) {



        }*/

        return res;
    }
}
