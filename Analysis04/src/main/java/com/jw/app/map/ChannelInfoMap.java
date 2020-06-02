package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.entity.ChannelInfo;
import com.jw.entity.FlowInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class ChannelInfoMap implements MapFunction<String, ChannelInfo> {
    @Override
    public ChannelInfo map(String data) throws Exception {

        JSONObject jsonObject = JSON.parseObject(data);

        // 五分钟的时间间隔问题
        String interMinute = "";

        String deviceType = jsonObject.getString("deviceType");
        String deviceCommonInfoStr = jsonObject.getString("deviceCommonInfo");
        DeviceCommonInfo deviceCommonInfo = JSON.parseObject(deviceCommonInfoStr, DeviceCommonInfo.class);

        // 获得渠道信息
        String channelInfoString = deviceCommonInfo.getChannelInfo();

        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);
        String visitTime = scanPageLog.getVisitTime();

        /* 封装输出数据的对象 */
        ChannelInfo channelInfo = new ChannelInfo();

        String interTime = DateUtil.getByInterMinute(visitTime);

        String flag = jsonObject.getString("flag");
        if ("hour".equals(flag)) {
            interTime = DateUtil.getByInterHour(visitTime);
            // 小时活跃状态
            if (deviceCommonInfo.isHourActive()) {
                channelInfo.setUserNums(1L);
            }
        } else if ("minute".equals(flag)) {
            interTime = DateUtil.getByInterMinute(visitTime);
            // 5分钟的活跃状态
            if (deviceCommonInfo.isFiveMinuteActive()) {
                channelInfo.setUserNums(1L);
            }
        }


        channelInfo.setChannelInfo(channelInfoString);
        // 先按分钟级进行
        channelInfo.setTimeInfo(interTime);
        channelInfo.setTimes(1L);
        // 分组条件
        channelInfo.setGroupByField(deviceType + "==" + channelInfoString + "==" + interTime);

        // 如果是新用户，就设置为1.
        if (deviceCommonInfo.isNew()) {
            channelInfo.setNewUserNum(1L);
        }

        // 小时活跃状态
        if (deviceCommonInfo.isHourActive()) {
            channelInfo.setHourActiveNums(1L);
        }

        // 天活跃状态
        if (deviceCommonInfo.isDayActive()) {
            channelInfo.setDayActiveNums(1L);
        }

        // 月活跃状态
        if (deviceCommonInfo.isMonthActive()) {
            channelInfo.setMonthActiveNums(1L);
        }

        // 周活跃状态
        if (deviceCommonInfo.isWeekActive()) {
            channelInfo.setWeekActiveNums(1L);
        }



        return channelInfo;
    }
}
