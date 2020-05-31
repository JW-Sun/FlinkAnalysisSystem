package com.jw.entity;

public class ChannelInfo {

    private String channelInfo; // 渠道

    private String timeInfo; // 分钟
    private Long times = 0L; // 访问次数
    private String deviceType; // 终端类型
    private String groupByField; // 分组字段
    private Long newUserNum = 0L; // 新增用户的数量

    public ChannelInfo() {
    }

    private Long hourActiveNums = 0L; // 小时活跃数量
    private Long dayActiveNums = 0L; // 天活跃数量
    private Long weekActiveNums = 0L; // 周活跃数量
    private Long monthActiveNums = 0L; // 月活跃数量
    private Long userNums = 0L; // 用户数量

    private String userId; // 用户id


    public String getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(String channelInfo) {
        this.channelInfo = channelInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getUserNums() {
        return userNums;
    }

    public void setUserNums(Long userNums) {
        this.userNums = userNums;
    }

    public Long getHourActiveNums() {
        return hourActiveNums;
    }

    public void setHourActiveNums(Long hourActiveNums) {
        this.hourActiveNums = hourActiveNums;
    }

    public Long getDayActiveNums() {
        return dayActiveNums;
    }

    public void setDayActiveNums(Long dayActiveNums) {
        this.dayActiveNums = dayActiveNums;
    }

    public Long getWeekActiveNums() {
        return weekActiveNums;
    }

    public void setWeekActiveNums(Long weekActiveNums) {
        this.weekActiveNums = weekActiveNums;
    }

    public Long getMonthActiveNums() {
        return monthActiveNums;
    }

    public void setMonthActiveNums(Long monthActiveNums) {
        this.monthActiveNums = monthActiveNums;
    }


    public Long getNewUserNum() {
        return newUserNum;
    }

    public void setNewUserNum(Long newUserNum) {
        this.newUserNum = newUserNum;
    }

    public String getTimeInfo() {
        return timeInfo;
    }

    public void setTimeInfo(String timeInfo) {
        this.timeInfo = timeInfo;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getGroupByField() {
        return groupByField;
    }

    public void setGroupByField(String groupByField) {
        this.groupByField = groupByField;
    }
}
