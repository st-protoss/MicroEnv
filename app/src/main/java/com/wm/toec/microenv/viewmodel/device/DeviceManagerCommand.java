package com.wm.toec.microenv.viewmodel.device;

import com.wm.toec.microenv.bean.DeviceInfoBean;

/**
 * Created by wm on 2018/7/3.
 * 设备管理界面操作
 */

public interface DeviceManagerCommand {
    public void showBind(DeviceInfoBean deviceInfoBean);
    public void showUnbind();
}
