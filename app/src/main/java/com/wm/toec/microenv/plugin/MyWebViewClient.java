package com.wm.toec.microenv.plugin;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.util.CheckNetwork;
import com.wm.toec.microenv.viewmodel.main.ActivityReportCommand;

/**
 * Created by wm on 2018/7/25.
 * 自定义webview client
 */

public class MyWebViewClient extends WebViewClient {
    public BaseActivity mActivity;
    public ActivityReportCommand activityReportCommand;

    public MyWebViewClient(BaseActivity mActivity, ActivityReportCommand command) {
        this.mActivity = mActivity;
        this.activityReportCommand = command;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (!CheckNetwork.isNetworkConnected(mActivity)) {
            activityReportCommand.hideProgressbar();
        }
        super.onPageFinished(view, url);
    }
}
