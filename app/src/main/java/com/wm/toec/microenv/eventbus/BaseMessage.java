package com.wm.toec.microenv.eventbus;

/**
 * Created by wm on 2018/7/3.
 * 基本事件的基类
 */

public class BaseMessage {


    int mCode;//1登录成功关闭入口界面 2通知devicemanager界面更新卡片3通知family manager更新列表//4退出当前app
    String mContent;

    public int getmCode() {
        return mCode;
    }
    public BaseMessage(int mCode, String mContent) {
        this.mCode = mCode;
        this.mContent = mContent;
    }
    public void setmCode(int mCode) {
        this.mCode = mCode;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
}
