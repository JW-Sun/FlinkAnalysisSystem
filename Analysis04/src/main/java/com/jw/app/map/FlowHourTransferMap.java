package com.jw.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;

public class FlowHourTransferMap implements MapFunction<String, String> {
    @Override
    public String map(String s) throws Exception {

        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.put("flag", "hour");
        String s1 = JSON.toJSONString(jsonObject);
        return s1;
    }
}
