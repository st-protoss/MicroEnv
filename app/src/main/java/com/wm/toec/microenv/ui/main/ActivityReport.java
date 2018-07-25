package com.wm.toec.microenv.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.databinding.ActivityReportBinding;
import com.wm.toec.microenv.plugin.MyWebChromeClient;
import com.wm.toec.microenv.plugin.MyWebViewClient;
import com.wm.toec.microenv.util.ShareUtil;
import com.wm.toec.microenv.viewmodel.main.ActivityReportCommand;

/**
 * Created by toec on 2018/7/25.
 */

public class ActivityReport extends BaseActivity<ActivityReportBinding> implements ActivityReportCommand {

    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent intent = getIntent();
        String memberId = intent.getStringExtra("memberId");
        setTitle("健康报告");
        mUrl = "https://baidu.com";
        initWebView();
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        WebView webView = mBindView.reportWv;
        mBindView.reportPb.setVisibility(View.VISIBLE);
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(false);
        // 不缩放
        webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);
        //webView.loadUrl("file:///android_asset/echarts.html");
        webView.loadUrl(mUrl);
        webView.setWebViewClient(new MyWebViewClient(this, this));
        webView.setWebChromeClient(new MyWebChromeClient(this, this));
        //webView.loadUrl("javascript: showChart("+jsonData+")");

    }


    @Override
    public void hideProgressbar() {
        mBindView.reportPb.setVisibility(View.GONE);
    }

    @Override
    public void refreshProgress(int progress) {
        mBindView.reportPb.setProgress(progress);
        if (progress == 100) {
            mBindView.reportPb.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String shareText = mUrl + "[分享自健康微环境助手]+\n" + "点击查看健康报告";
                ShareUtil.share(ActivityReport.this, shareText);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebView webView = mBindView.reportWv;
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
    }
}
