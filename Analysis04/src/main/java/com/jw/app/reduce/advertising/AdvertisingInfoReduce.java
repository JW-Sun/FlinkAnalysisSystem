package com.jw.app.reduce.advertising;

import com.jw.entity.AdvertisingInfo;
import com.jw.entity.ChannelInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class AdvertisingInfoReduce implements ReduceFunction<AdvertisingInfo> {
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
        res.setTimes(times + times1);
        res.setAdId(adid);
        res.setProductId(productId);
        res.setUserNums(userNum1 + userNum2);



        /*++*/
        return res;
    }
}
