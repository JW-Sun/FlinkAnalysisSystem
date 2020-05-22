package com.jw.input;

public class DeviceCommonInfo {
    // 未登录为用户为-1
    private String uerId;

    //设备Id
    private String deviceId;

    // pc 小程序 app
    // private String deviceType;

    private String openTime;

    private String leaveTime;

    private String remoteId;

    private String country;

    private String province;

    private String city;

    // 是否是新增用户
    private boolean isNew;
    // 是否5分钟活跃
    private boolean fiveMinuteActive;
    // 是否是小时活跃
    private boolean hourActive;
    // 是否是天活跃
    private boolean dayActive;
    // 是否是月活跃
    private boolean monthActive;
    // 是否是周活跃
    private boolean weekActive;


    public boolean isFiveMinuteActive() {
        return fiveMinuteActive;
    }

    public void setFiveMinuteActive(boolean fiveMinuteActive) {
        this.fiveMinuteActive = fiveMinuteActive;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isHourActive() {
        return hourActive;
    }

    public void setHourActive(boolean hourActive) {
        this.hourActive = hourActive;
    }

    public boolean isDayActive() {
        return dayActive;
    }

    public void setDayActive(boolean dayActive) {
        this.dayActive = dayActive;
    }

    public boolean isMonthActive() {
        return monthActive;
    }

    public void setMonthActive(boolean monthActive) {
        this.monthActive = monthActive;
    }

    public boolean isWeekActive() {
        return weekActive;
    }

    public void setWeekActive(boolean weekActive) {
        this.weekActive = weekActive;
    }

    public String getUerId() {
        return uerId;
    }

    public void setUerId(String uerId) {
        this.uerId = uerId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
