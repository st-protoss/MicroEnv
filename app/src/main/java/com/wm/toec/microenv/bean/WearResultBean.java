package com.wm.toec.microenv.bean;

import java.util.Date;

/**
 * Created by wm on 2018/7/11.
 * 穿戴数据和健康状况的实体类
 */

public class WearResultBean {
    int status;//1均上传2差一个未上传
    String year;
    String month;
    String day;
    WearData wearData;
    HealthCondition healthCondition;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public WearData getWearData() {
        return wearData;
    }

    public void setWearData(WearData wearData) {
        this.wearData = wearData;
    }

    public HealthCondition getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(HealthCondition healthCondition) {
        this.healthCondition = healthCondition;
    }

    //穿戴数据内部类
    public static class WearData{
        String sleepTime;//睡眠时间
        String deepSleepTime;//深度睡眠时间
        String heartbeat;//心跳
        String bloodPressureH;//血压高压
        String bloodPressureL;//血压低压
        String bloodGlucose;//血糖
        String footStep;//步数

        public String getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(String sleepTime) {
            this.sleepTime = sleepTime;
        }

        public String getDeepSleepTime() {
            return deepSleepTime;
        }

        public void setDeepSleepTime(String deepSleepTime) {
            this.deepSleepTime = deepSleepTime;
        }

        public String getHeartbeat() {
            return heartbeat;
        }

        public void setHeartbeat(String heartbeat) {
            this.heartbeat = heartbeat;
        }

        public String getBloodPressureH() {
            return bloodPressureH;
        }

        public void setBloodPressureH(String bloodPressureH) {
            this.bloodPressureH = bloodPressureH;
        }

        public String getBloodPressureL() {
            return bloodPressureL;
        }

        public void setBloodPressureL(String bloodPressureL) {
            this.bloodPressureL = bloodPressureL;
        }

        public String getBloodGlucose() {
            return bloodGlucose;
        }

        public void setBloodGlucose(String bloodGlucose) {
            this.bloodGlucose = bloodGlucose;
        }

        public String getFootStep() {
            return footStep;
        }

        public void setFootStep(String footStep) {
            this.footStep = footStep;
        }
    }
    //健康状况的内部类
    public static class HealthCondition{
        int condition;//1优2良3疾病
        String conditionDescription;//当日健康感觉描述

        public int getCondition() {
            return condition;
        }

        public void setCondition(int condition) {
            this.condition = condition;
        }

        public String getConditionDescription() {
            return conditionDescription;
        }

        public void setConditionDescription(String conditionDescription) {
            this.conditionDescription = conditionDescription;
        }
    }
}
