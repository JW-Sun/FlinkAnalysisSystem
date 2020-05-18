package com.jw.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jw.input.AppInfo;
import com.jw.input.PcInfo;
import com.jw.input.ScanPageLog;
import com.jw.input.XiaoChengXuInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/***
 * 数据收集服务
 */
@RestController
public class DataCollectionController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @PostMapping("/dataCollect")
    public void dataCollect(@RequestBody String data) {
        System.out.println(data);
        if (StringUtils.isNotBlank(data)) {
            JSONObject jsonObject = JSON.parseObject(data);
            String deviceType = jsonObject.getString("deviceType");
            // 0:pc 1：app  2:小程序端  3：网页浏览日志
            if ("0".equals(deviceType)) {
                System.out.println("pc");
                PcInfo pcInfo = JSONObject.parseObject(data, PcInfo.class);
            } else if ("1".equals(deviceType)) {
                System.out.println("app");
                AppInfo appInfo = JSONObject.parseObject(data, AppInfo.class);
            } else if ("2".equals(deviceType)) {
                System.out.println("xiaochengxu");
                XiaoChengXuInfo xiaoChengXuInfo = JSONObject.parseObject(data, XiaoChengXuInfo.class);
            } else if ("4".equals(deviceType)) {
                System.out.println("scanpage");
                ScanPageLog scanPageLog = JSONObject.parseObject(data, ScanPageLog.class);
            }
            kafkaTemplate.send("dataInfo", data);
        }

    }

}
