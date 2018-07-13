package com.wm.toec.microenv.viewmodel.device;

/**
 * Created by toec on 2018/7/4.
 */

public interface LocationCommand {
    public void confirmLocation(String location);
    public void bindSuccess();
    public void bindFail();
}
