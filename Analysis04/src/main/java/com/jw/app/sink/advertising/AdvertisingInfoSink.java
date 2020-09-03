package com.jw.app.sink.advertising;

import com.jw.entity.AdvertisingInfo;
import com.jw.entity.ChannelInfo;
import com.jw.utils.ClickHouseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.HashMap;
import java.util.Map;

public class AdvertisingInfoSink implements SinkFunction<AdvertisingInfo> {

    @Override
    public void invoke(AdvertisingInfo in, Context context) throws Exception {
        String timeInfo = in.getTimeInfo();
        Long times = in.getTimes();

        if (StringUtils.isBlank(timeInfo)) {
            return;
        }

        String adid = in.getAdId();
        String productId = in.getProductId();
        Long userNum = in.getUserNums();

        Map<String, String> map = new HashMap<>();
        map.put("timeInfo", timeInfo);
        map.put("times", String.valueOf(times));
        map.put("adId", adid);
        map.put("productId", productId);
        map.put("userNums", String.valueOf(userNum));

        ClickHouseUtil.insert("AdvertisingInfo", map);
    }
}
