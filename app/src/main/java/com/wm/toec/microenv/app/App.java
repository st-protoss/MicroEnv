package com.wm.toec.microenv.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;

import com.baidu.mapapi.SDKInitializer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.wm.toec.microenv.plugin.lbs.LocationService;

/**
 * Created by wm on 2018/6/6.
 * application基类
 */

public class App extends Application {
    private static Context mAppContext;
    public LocationService locationService;
    public Vibrator mVibrator;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        //初始化zxing
        ZXingLibrary.initDisplayOpinion(mAppContext);
        //初始化baidumap
        //locationService = new LocationService(mAppContext);
        mVibrator =(Vibrator)mAppContext.getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(mAppContext);
    }

    public static Context getAppContext(){
        return mAppContext;
    }
}
