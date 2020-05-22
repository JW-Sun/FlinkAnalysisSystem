package com.jw.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jw.dataCollectionUtils.UserStatus;
import com.jw.input.AppInfo;
import com.jw.input.PcInfo;
import com.jw.input.ScanPageLog;
import com.jw.input.XiaoChengXuInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/***
 * 数据收集服务
 */
@RestController
public class DataCollectionController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @PostMapping("/dataCollect")
    public void dataCollect(@RequestBody String data) throws IOException {
        System.out.println(data);
        if (StringUtils.isNotBlank(data)) {
            JSONObject jsonObject = JSON.parseObject(data);
            String deviceType = jsonObject.getString("deviceType");
            ScanPageLog scanPageLog = JSON.parseObject(data, ScanPageLog.class);
            String deviceCommonInfo = jsonObject.getString("deviceCommonInfo");
            // 0:app 1：pc  2:小程序端
            if ("0".equals(deviceType)) {
                System.out.println("app");
                AppInfo appInfo = JSONObject.parseObject(deviceCommonInfo, AppInfo.class);

                UserStatus.filterNewStatus(appInfo);
                UserStatus.filterActiveStatus(appInfo);

                scanPageLog.setDeviceCommonInfo(appInfo);
            } else if ("1".equals(deviceType)) {
                System.out.println("pc");
                PcInfo pcInfo = JSONObject.parseObject(data, PcInfo.class);

                UserStatus.filterNewStatus(pcInfo);
                UserStatus.filterActiveStatus(pcInfo);

                scanPageLog.setDeviceCommonInfo(pcInfo);
            } else if ("2".equals(deviceType)) {
                System.out.println("xiaochengxu");
                XiaoChengXuInfo xiaoChengXuInfo = JSONObject.parseObject(data, XiaoChengXuInfo.class);

                UserStatus.filterNewStatus(xiaoChengXuInfo);
                UserStatus.filterActiveStatus(xiaoChengXuInfo);

                scanPageLog.setDeviceCommonInfo(xiaoChengXuInfo);
            }
            String scanPageLogString = JSON.toJSONString(scanPageLog);
            System.out.println(scanPageLogString);
            kafkaTemplate.send("dataInfo", scanPageLogString);
        }

    }

}
