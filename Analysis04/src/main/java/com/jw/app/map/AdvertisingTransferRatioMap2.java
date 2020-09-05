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

public class AdvertisingTransferRatioMap2 implements MapFunction<AdvertisingInfo, AdvertisingInfo> {
    @Override
    public AdvertisingInfo map(AdvertisingInfo advertisingInfo) throws Exception {
        String timeinfo = advertisingInfo.getTimeInfo();//分钟,小时
        String adId = advertisingInfo.getAdId();//广告id
        String productId = advertisingInfo.getProductId();//商品id
        AdvertisingInfo advertisingInfoFinal = new AdvertisingInfo();
        advertisingInfoFinal.setAdId(adId);
        advertisingInfoFinal.setProductId(productId);
        advertisingInfoFinal.setTimeInfo(timeinfo);
        advertisingInfoFinal.setUserOrderNums(1L);
        advertisingInfoFinal.setGroupByFieldString(adId+"=="+productId+"=="+timeinfo);
        return advertisingInfoFinal;
    }
}
