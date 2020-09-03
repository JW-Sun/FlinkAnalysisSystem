package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.jw.entity.AdvertisingInfo;
import com.jw.entity.ChannelInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;

public class AdvertisingUserDetailMap implements MapFunction<String, AdvertisingInfo> {
    @Override
    public AdvertisingInfo map(String data) throws Exception {

        ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);

        // 拿到广告和商品的信息
        String adid = scanPageLog.getAdId();
        String productId = scanPageLog.getProductId();

        // 返回的实体类
        AdvertisingInfo res = new AdvertisingInfo();

        String deviceType = scanPageLog.getDeviceType();

        DeviceCommonInfo deviceCommonInfo = scanPageLog.getDeviceCommonInfo();
        String userId = deviceCommonInfo.getUerId();

        String channelInfoString = deviceCommonInfo.getChannelInfo();

        res.setDeviceType(deviceType);
        res.setUserId(userId);
        res.setTimes(1L);


        String visitTime = scanPageLog.getVisitTime();
        String byInterHour = DateUtil.getByInterHour(visitTime);
        res.setTimeInfo(byInterHour);

        // 组装分组的字段
        res.setGroupByFieldString(userId + "==" + deviceType + "==" + byInterHour);
        res.setDeviceType(deviceType);

        res.setAdId(adid);
        res.setProductId(productId);

        return res;
    }
}
