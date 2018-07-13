package com.wm.toec.microenv.bean;

/**
 * Created by wm on 2018/7/3.
 * 验证码实体类
 */

public class VerifyCodeBean {
    String verifycode;
    int status;

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
