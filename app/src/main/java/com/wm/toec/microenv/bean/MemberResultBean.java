package com.wm.toec.microenv.bean;

/**
 * Created by wm on 2018/7/5.
 * 对成员操作的结果实体类
 */

public class MemberResultBean {
    int result;//0fail 1success
    String errorContent;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }
}
