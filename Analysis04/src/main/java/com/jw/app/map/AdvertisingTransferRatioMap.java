package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.entity.AdvertisingInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.ScanPageLog;
import com.jw.utils.DateUtil;
import com.jw.yewu.OrderInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

public class AdvertisingTransferRatioMap implements MapFunction<String, AdvertisingInfo> {
    @Override
    public AdvertisingInfo map(String ins) throws Exception {

        AdvertisingInfo advertisingInfo = new AdvertisingInfo();

        JSONObject jsonObject = JSON.parseObject(ins);
        ScanPageLog scanPageLog = jsonObject.getObject("scanPageLog", ScanPageLog.class);
        OrderInfo orderInfo = jsonObject.getObject("orderInfo", OrderInfo.class);

        String deviceCommonInfoStr = jsonObject.getString("deviceCommonInfo");
        DeviceCommonInfo deviceCommonInfo = JSON.parseObject(deviceCommonInfoStr, DeviceCommonInfo.class);

        // 获得渠道信息
        String channelInfoString = deviceCommonInfo.getChannelInfo();

        String visitTime = scanPageLog.getVisitTime();

        String interTime = DateUtil.getByInterMinute(visitTime);

        // 获取广告id和商品id
        String adId = scanPageLog.getAdId();
        String productId = scanPageLog.getProductId();

        // 获得用户id
        String userId = deviceCommonInfo.getUerId();

        if (StringUtils.isNotBlank(adId)) {
            advertisingInfo.setUserId(userId);
            advertisingInfo.setTimeInfo(interTime);
            advertisingInfo.setGroupByFieldString(productId + "==" + interTime + "==" + adId + "==" + userId);
            advertisingInfo.setProductId(productId);
            advertisingInfo.setAdId(adId);
        }
        return advertisingInfo;
    }
}
