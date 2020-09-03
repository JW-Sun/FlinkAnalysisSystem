package com.jw.app.reduce.advertising;

import com.jw.entity.AdvertisingInfo;
import com.jw.entity.ChannelInfo;
import org.apache.flink.api.common.functions.ReduceFunction;

public class AdvertisingUserDetailReduce implements ReduceFunction<AdvertisingInfo> {
    @Override
    public AdvertisingInfo reduce(AdvertisingInfo value1, AdvertisingInfo value2) throws Exception {
        Long times = value1.getTimes();
        Long times1 = value2.getTimes();

        AdvertisingInfo res = new AdvertisingInfo();

        res.setAdId(value1.getAdId());
        res.setProductId(value1.getProductId());

        res.setGroupByFieldString(value1.getGroupByFieldString());
        res.setTimes(times + times1);
        res.setDeviceType(value1.getDeviceType());
        res.setTimeInfo(value1.getTimeInfo());
        res.setUserId(value1.getUserId());
        res.setDeviceType(value1.getDeviceType());

        return res;
    }
}
