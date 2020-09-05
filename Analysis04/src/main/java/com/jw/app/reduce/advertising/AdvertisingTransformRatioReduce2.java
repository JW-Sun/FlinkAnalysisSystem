package com.jw.app.reduce.advertising;

import com.jw.entity.AdvertisingInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

/***
 * 起到过滤的作用，在多个用户点击广告购买商品之后，如果购买多次，也是算做一次来进行处理
 */
public class AdvertisingTransformRatioReduce2 implements ReduceFunction<AdvertisingInfo> {
    @Override
    public AdvertisingInfo reduce(AdvertisingInfo advertisingInfo, AdvertisingInfo t1) throws Exception {
        String groupByField = advertisingInfo.getGroupByFieldString();
        String timeInfo = advertisingInfo.getTimeInfo();
        String productId = advertisingInfo.getProductId();
        String adId = advertisingInfo.getAdId();

        long userOrderNums1 = advertisingInfo.getUserOrderNums();
        long userOrderNums2 = t1.getUserOrderNums();

        AdvertisingInfo advertisingInfofinal = new AdvertisingInfo();
        advertisingInfofinal.setGroupByFieldString(groupByField);
        advertisingInfofinal.setTimeInfo(timeInfo);
        advertisingInfofinal.setAdId(adId);
        advertisingInfofinal.setProductId(productId);
        advertisingInfofinal.setUserOrderNums(userOrderNums1+userOrderNums2);
        return advertisingInfofinal;
    }
}
