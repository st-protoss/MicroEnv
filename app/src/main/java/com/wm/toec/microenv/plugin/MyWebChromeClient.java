package com.wm.toec.microenv.plugin;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.viewmodel.main.ActivityReportCommand;

/**
 * Created by toec on 2018/7/25.
 */

public class MyWebChromeClient extends WebChromeClient {
    public BaseActivity mActivity;
    public ActivityReportCommand activityReportCommand;

    public MyWebChromeClient(BaseActivity mActivity, ActivityReportCommand activityReportCommand) {
        this.mActivity = mActivity;
        this.activityReportCommand = activityReportCommand;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        activityReportCommand.refreshProgress(newProgress);
    }
}
