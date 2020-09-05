package com.jw.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jw.utils.DateUtil;
import org.apache.flink.streaming.connectors.fs.Clock;
import org.apache.flink.streaming.connectors.fs.bucketing.BasePathBucketer;
import org.apache.hadoop.fs.Path;

import java.io.File;

public class MyPathBucket extends BasePathBucketer<String> {

    @Override
    public Path getBucketPath(Clock clock, Path basePath, String element) {
        JSONObject jsonObject = JSONObject.parseObject(element);
        if (jsonObject == null || !jsonObject.containsKey("visitTime")) {
            return new Path(String.valueOf(basePath));
        }
        String date = jsonObject.getString("visitTime");

        String dateString = DateUtil.getByMillions(date, "yyyyMMddHH");

        return new Path(basePath + File.separator + dateString);
    }

    public static void main(String[] args) {
        String s = "{\"visitTime\":\"1586665943000\"}";
        String s1 = null;
        JSONObject jsonObject = JSON.parseObject(s1);
        System.out.println(jsonObject);
    }
}
