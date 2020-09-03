package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.entity.AdvertisingInfo;
import com.jw.entity.ChannelInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class AdvertisingMap implements MapFunction<String, AdvertisingInfo> {
    @Override
    public AdvertisingInfo map(String data) throws Exception {

        JSONObject jsonObject = JSON.parseObject(data);

        // 五分钟的时间间隔问题
        String interMinute = "";
        AdvertisingInfo advertisingInfo = new AdvertisingInfo();

        String deviceCommonInfoStr = jsonObject.getString("deviceCommonInfo");
        DeviceCommonInfo deviceCommonInfo = JSON.parseObject(deviceCommonInfoStr, DeviceCommonInfo.class);

        // 获得渠道信息
        String channelInfoString = deviceCommonInfo.getChannelInfo();

        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);
        String visitTime = scanPageLog.getVisitTime();

        String interTime = DateUtil.getByInterMinute(visitTime);
        String flag = jsonObject.getString("flag");
        if ("hour".equals(flag)) {
            interTime = DateUtil.getByInterHour(visitTime);
            if (deviceCommonInfo.isHourActive()) {
                advertisingInfo.setUserNums(1L);
            }
        } else if ("minute".equals(flag)) {
            interTime = DateUtil.getByInterMinute(visitTime);
            // 5分钟的适用状态
            if (deviceCommonInfo.isFiveMinuteActive()) {
                advertisingInfo.setUserNums(1L);
            }
        }

        // 获取广告id和商品id
        String adId = scanPageLog.getAdId();
        String productId = scanPageLog.getProductId();

        // 获得用户id
        String userId = deviceCommonInfo.getUerId();


        if (StringUtils.isNotBlank(adId)) {
            advertisingInfo .setTimes(1L);
            advertisingInfo.setUserId(userId);
            advertisingInfo.setTimeInfo(interTime);
            advertisingInfo.setGroupByFieldString(interTime);
            advertisingInfo.setProductId(productId);
            advertisingInfo.setAdId(adId);
        }

        return advertisingInfo;
    }
}
