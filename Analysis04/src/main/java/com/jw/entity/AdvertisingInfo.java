package com.jw.entity;

public class AdvertisingInfo {

    private String userId = "";

    private String timeInfo = "";

    // 分组字段
    private String groupByFieldString = "";

    // 访问次数
    private Long times = 0L;

    // 用户数目
    private Long userNums = 0L;

    // 广告id
    private String adId = "";

    // 商品id
    private String productId = "";

    // 设备信息
    private String deviceType = "";

    // 成交的用户数
    private Long userOrderNums = 0L;

    public Long getUserOrderNums() {
        return userOrderNums;
    }

    public void setUserOrderNums(Long userOrderNums) {
        this.userOrderNums = userOrderNums;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getUserNums() {
        return userNums;
    }

    public void setUserNums(Long userNums) {
        this.userNums = userNums;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeInfo() {
        return timeInfo;
    }

    public void setTimeInfo(String timeInfo) {
        this.timeInfo = timeInfo;
    }

    public String getGroupByFieldString() {
        return groupByFieldString;
    }

    public void setGroupByFieldString(String groupByFieldString) {
        this.groupByFieldString = groupByFieldString;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }
}
