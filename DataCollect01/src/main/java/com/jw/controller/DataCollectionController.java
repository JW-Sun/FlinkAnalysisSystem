package com.jw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * 数据收集服务
 */
@RestController
public class DataCollectionController {

    @GetMapping("/test")
    public String testHello(String name) {
        return name;
    }

}
