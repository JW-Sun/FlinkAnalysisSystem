package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.entity.FlowInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class FlowMap implements MapFunction<String, FlowInfo> {
    @Override
    public FlowInfo map(String data) throws Exception {

        JSONObject jsonObject = JSON.parseObject(data);

        // 五分钟的时间间隔问题
        String interMinute = "";

        String deviceType = jsonObject.getString("deviceType");
        String deviceCommonInfoStr = jsonObject.getString("deviceCommonInfo");
        DeviceCommonInfo deviceCommonInfo = JSON.parseObject(deviceCommonInfoStr, DeviceCommonInfo.class);

        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);
        String visitTime = scanPageLog.getVisitTime();

        /* 封装输出数据的对象 */
        FlowInfo res = new FlowInfo();

        String flag = jsonObject.getString("flag");
        if ("hour".equals(flag)) {
            interMinute = DateUtil.getByInterHour(visitTime);
            // 小时活跃状态
            if (deviceCommonInfo.isHourActive()) {
                res.setUserNums(1L);
            }
        } else if ("minute".equals(flag)) {
            interMinute = DateUtil.getByInterMinute(visitTime);
            // 5分钟的活跃状态
            if (deviceCommonInfo.isFiveMinuteActive()) {
                res.setUserNums(1L);
            }
        }


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
