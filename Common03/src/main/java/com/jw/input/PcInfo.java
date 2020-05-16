package com.jw.input;

public class PcInfo extends DeviceCommonInfo {
    private String macAddress;
    private String cookie;
    private String remoteName;
    private String deviceType;
    private String os;
    private String browser;
    private String resolution;
    private String sourceInfo;
    private String sourceType;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    @Override
    public String getDeviceType() {
        return deviceType;
    }

    @Override
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
