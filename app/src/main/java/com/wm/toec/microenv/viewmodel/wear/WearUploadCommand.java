package com.wm.toec.microenv.viewmodel.wear;

import com.wm.toec.microenv.bean.WearResultBean;

/**
 * Created by toec on 2018/7/11.
 */

public interface WearUploadCommand {
    public void loadSuccess(WearResultBean wearResultBean);
    public void networkError();
}
