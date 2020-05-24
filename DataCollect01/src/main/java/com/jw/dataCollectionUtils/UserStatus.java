package com.jw.dataCollectionUtils;

import com.jw.input.AppInfo;
import com.jw.input.DeviceCommonInfo;
import com.jw.input.PcInfo;
import com.jw.input.XiaoChengXuInfo;
import com.jw.utils.DateUtil;
import com.jw.utils.HBaseUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class UserStatus {

    /***
     * 过滤是否是新增用户状态
     * create "deviceCommonInfoApp", "info"
     * create "deviceCommonInfoPc", "info"
     * create "deviceCommonInfoXiaochengxu", "info"
     *
     * @return
     */
    public static void filterNewStatus(DeviceCommonInfo deviceCommonInfo) throws IOException {
        if (deviceCommonInfo instanceof AppInfo) {
            AppInfo appInfo = (AppInfo) deviceCommonInfo;
            String deviceId = appInfo.getDeviceId();

            // 通过HBaseUtil来获得数据
            String s = HBaseUtil.get("deviceCommonInfoApp", deviceId, "info", "uniqueId");
            if (StringUtils.isBlank(s)) {
                String s2 = HBaseUtil.get("deviceCommonInfoPc", deviceId, "info", "uniqueId");
                if (StringUtils.isBlank(s2)) {
                    appInfo.setNew(true);
                    HBaseUtil.insert("deviceCommonInfoApp", deviceId, "info", "uniqueId", deviceId);
                }
            }
            deviceCommonInfo = appInfo;

        } else if (deviceCommonInfo instanceof PcInfo) {
            PcInfo pcInfo = (PcInfo) deviceCommonInfo;
            String macAddress = pcInfo.getMacAddress();
            String deviceId = pcInfo.getDeviceId();

            if (!StringUtils.isBlank(macAddress)) {
                // 通过HBaseUtil来获得数据
                String s = HBaseUtil.get("deviceCommonInfoPc", macAddress, "info", "uniqueId");
                if (StringUtils.isBlank(s)) {
                    pcInfo.setNew(true);
                    HBaseUtil.insert("deviceCommonInfoPc", macAddress, "info", "uniqueId", macAddress);
                }
            } else if (!StringUtils.isBlank(deviceId)) {
                // 通过HBaseUtil来获得数据
                String s = HBaseUtil.get("deviceCommonInfoPc", deviceId, "info", "uniqueId");
                if (StringUtils.isBlank(s)) {

                    String s1 = HBaseUtil.get("deviceCommonInfoApp", deviceId, "info", "uniqueId");
                    if (StringUtils.isBlank(s1)) {
                        pcInfo.setNew(true);
                        HBaseUtil.insert("deviceCommonInfoPc", deviceId, "info", "uniqueId", deviceId);
                    }
                }
            }
            deviceCommonInfo = pcInfo;
        } else if (deviceCommonInfo instanceof XiaoChengXuInfo) {
            XiaoChengXuInfo xiaoChengXuInfo = (XiaoChengXuInfo) deviceCommonInfo;
            String winxinAccount = xiaoChengXuInfo.getWinxinAccount();
            // 通过HBaseUtil来获得数据
            String s = HBaseUtil.get("deviceCommonInfoXiaochengxu", winxinAccount, "info", "uniqueId");
            if (StringUtils.isBlank(s)) {
                xiaoChengXuInfo.setNew(true);
                HBaseUtil.insert("deviceCommonInfoXiaochengxu", winxinAccount, "info", "uniqueId", winxinAccount);
            }
            deviceCommonInfo = xiaoChengXuInfo;

        }
    }

    public static void filterActiveStatus(DeviceCommonInfo deviceCommonInfo) throws IOException {
        if (deviceCommonInfo instanceof AppInfo) {
            AppInfo appInfo = (AppInfo) deviceCommonInfo;
            String deviceId = appInfo.getDeviceId();
            Long openTime = Long.valueOf(appInfo.getOpenTime());

            // 通过HBaseUtil来获得数据
            String res = HBaseUtil.get("deviceCommonInfoApp", deviceId, "info", "lastVisitTime");
            String lastVisitTime = "";
            if (StringUtils.isBlank(res)) {
                String res2 = HBaseUtil.get("deviceCommonInfoPc", deviceId, "info", "lastVisitTime");
                if (StringUtils.isBlank(res2)) {
                    appInfo.setHourActive(true);
                    appInfo.setDayActive(true);
                    appInfo.setMonthActive(true);
                    appInfo.setWeekActive(true);
                    HBaseUtil.insert("deviceCommonInfoApp", deviceId, "info", "lastVisitTime", String.valueOf(openTime));
                } else {
                    lastVisitTime = res2;
                }
            } else {
                lastVisitTime = res;
            }

            if (!strBlank(lastVisitTime)) {

                Long last = Long.valueOf(lastVisitTime);

                // 小时
                Long currentHourStart = DateUtil.getCurrentHourStart(openTime);
                if (last < currentHourStart) {
                    appInfo.setHourActive(true);
                }

                // 天
                Long currentDayStart = DateUtil.getCurrentDayStart(openTime);
                if (last < currentDayStart) {
                    appInfo.setDayActive(true);
                }

                // 周
                Long currentWeekStart = DateUtil.getCurrentWeekStart(openTime);
                if (last < currentWeekStart) {
                    appInfo.setWeekActive(true);
                }

                // 月
                Long currentMonthStart = DateUtil.getCurrentMonthStart(openTime);
                if (last < currentMonthStart) {
                    appInfo.setMonthActive(true);
                }

                // min
                //分钟
                Long fiveMinuteInter = DateUtil.getCurrentFiveMinuteInterStart(openTime);
                if (last < fiveMinuteInter) {
                    appInfo.setFiveMinuteActive(true);
                }
            }
            HBaseUtil.insert("deviceCommonInfoApp", deviceId, "info", "lastVisitTime", String.valueOf(openTime));

        } else if (deviceCommonInfo instanceof PcInfo) {
            PcInfo pcInfo = (PcInfo) deviceCommonInfo;
            String macAddress = pcInfo.getMacAddress();
            String deviceId = pcInfo.getDeviceId();
            String lastTime = "";

            Long openTime = Long.valueOf(pcInfo.getOpenTime());

            if (!strBlank(macAddress)) {
                String res1 = HBaseUtil.get("deviceCommonInfoPc", macAddress,
                        "info", "lastVisitTime");
                if (!strBlank(res1)) {
                    lastTime = res1;
                } else {
                    pcInfo.setHourActive(true);
                    pcInfo.setDayActive(true);
                    pcInfo.setMonthActive(true);
                    pcInfo.setWeekActive(true);
                    HBaseUtil.insert("deviceCommonInfoPc", macAddress,
                            "info", "lastVisitTime", String.valueOf(openTime));
                }
                HBaseUtil.insert("deviceCommonInfoPc", macAddress,
                        "info", "lastVisitTime", String.valueOf(openTime));
            } else if (!strBlank(deviceId)) {
                String res1 = HBaseUtil.get("deviceCommonInfoPc", deviceId,
                        "info", "lastVisitTime");

                if (strBlank(res1)) {
                    String res2 = HBaseUtil.get("deviceCommonInfoApp", deviceId,
                            "info", "lastVisitTime");

                    if (strBlank(res2)) {
                        pcInfo.setHourActive(true);
                        pcInfo.setMonthActive(true);
                        pcInfo.setDayActive(true);
                        pcInfo.setWeekActive(true);

                        HBaseUtil.insert("deviceCommonInfoPc", deviceId,
                                "info", "lastVisitTime", String.valueOf(openTime));
                    } else {
                        lastTime = res2;
                    }
                } else {
                    lastTime = res1;
                }

                HBaseUtil.insert("deviceCommonInfoPc", deviceId,
                        "info", "lastVisitTime", String.valueOf(openTime));
            }

            if (!strBlank(lastTime)) {

                Long last = Long.valueOf(lastTime);

                // 小时
                Long currentHourStart = DateUtil.getCurrentHourStart(openTime);
                if (last < currentHourStart) {
                    pcInfo.setHourActive(true);
                }

                // 天
                Long currentDayStart = DateUtil.getCurrentDayStart(openTime);
                if (last < currentDayStart) {
                    pcInfo.setDayActive(true);
                }

                // 周
                Long currentWeekStart = DateUtil.getCurrentWeekStart(openTime);
                if (last < currentWeekStart) {
                    pcInfo.setWeekActive(true);
                }

                // 月
                Long currentMonthStart = DateUtil.getCurrentMonthStart(openTime);
                if (last < currentMonthStart) {
                    pcInfo.setMonthActive(true);
                }

                // min
                //分钟
                Long fiveMinuteInter = DateUtil.getCurrentFiveMinuteInterStart(openTime);
                if (last < fiveMinuteInter) {
                    pcInfo.setFiveMinuteActive(true);
                }
            }


        } else if (deviceCommonInfo instanceof XiaoChengXuInfo) {
            XiaoChengXuInfo xiaochengxuInfo = (XiaoChengXuInfo) deviceCommonInfo;
            String weixinAccount = xiaochengxuInfo.getWinxinAccount();
            String openTime = xiaochengxuInfo.getOpenTime();
            Long openTimeMillons = Long.valueOf(openTime);
            String lastVisitTime = "";

            String res1 = HBaseUtil.get("deviceCommonInfoXiaochengxu", weixinAccount,
                    "info", "lastVisitTime");
            if (strBlank(res1)) {
                xiaochengxuInfo.setHourActive(true);
                xiaochengxuInfo.setDayActive(true);
                xiaochengxuInfo.setMonthActive(true);
                xiaochengxuInfo.setWeekActive(true);
                HBaseUtil.insert("deviceCommonInfoXiaochengxu", weixinAccount,
                        "info", "lastVisitTime", openTime);
            } else {
                lastVisitTime = res1;
            }

            if(StringUtils.isNotBlank(lastVisitTime)) {
                long lastVisitTimeMillons = Long.valueOf(lastVisitTime);
                //小时
                long hourStart = DateUtil.getCurrentHourStart(openTimeMillons);
                if(lastVisitTimeMillons < hourStart) {
                    xiaochengxuInfo.setHourActive(true);
                }

                //天
                long dayStart = DateUtil.getCurrentDayStart(openTimeMillons);
                if(lastVisitTimeMillons < dayStart) {
                    xiaochengxuInfo.setDayActive(true);
                }

                //周
                long weekStart = DateUtil.getCurrentWeekStart(openTimeMillons);
                if(lastVisitTimeMillons < weekStart) {
                    xiaochengxuInfo.setWeekActive(true);
                }

                //月
                long monthStart = DateUtil.getCurrentMonthStart(openTimeMillons);
                if(lastVisitTimeMillons < monthStart) {
                    xiaochengxuInfo.setMonthActive(true);
                }

                //分钟
                Long fiveMinuteInter = DateUtil.getCurrentFiveMinuteInterStart(openTimeMillons);
                if (lastVisitTimeMillons < fiveMinuteInter) {
                    xiaochengxuInfo.setFiveMinuteActive(true);
                }

            }

        }
    }


    private static boolean strBlank(String s) {
        return StringUtils.isBlank(s);
    }
}
