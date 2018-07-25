package com.wm.toec.microenv.bean;

/**
 * Created by wm on 2018/7/3.
 * 登录的实体类
 */

public class LoginBean {
    int status;//0密码错误 或用户名不存在 1登陆成功 2用户被冻结
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
