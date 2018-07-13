package com.wm.toec.microenv.bean;

/**
 * Created by wm on 2018/7/3.
 * 注册返回的实体类
 */

public class RegisterBean {
    int status;//1成功 0注册失败 2验证码错误
    String userId;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
