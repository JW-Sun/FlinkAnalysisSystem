package com.jw.app.reduce.advertising;

import com.jw.entity.AdvertisingInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

/***
 * 起到过滤的作用，在多个用户点击广告购买商品之后，如果购买多次，也是算做一次来进行处理
 */
public class AdvertisingTransformRatioReduce implements ReduceFunction<AdvertisingInfo> {
    @Override
    public AdvertisingInfo reduce(AdvertisingInfo value1, AdvertisingInfo value2) throws Exception {
        Long times = value1.getTimes();
        Long times1 = value2.getTimes();

        Long userNum1 = value1.getUserNums();
        Long userNum2 = value2.getUserNums();
        /*++*/
        String groupByField = value1.getGroupByFieldString();
        String timeInfo = value1.getTimeInfo();

        String adid = value1.getAdId();
        String productId = value1.getProductId();


        AdvertisingInfo res = new AdvertisingInfo();
        res.setGroupByFieldString(groupByField);
        res.setTimeInfo(timeInfo);
        res.setAdId(adid);
        res.setProductId(productId);


        /*++*/
        return res;
    }
}
