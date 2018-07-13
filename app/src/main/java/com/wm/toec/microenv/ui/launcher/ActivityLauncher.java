package com.wm.toec.microenv.ui.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.ui.main.ActivityMain;
import com.wm.toec.microenv.ui.portal.ActivityPortal;
import com.wm.toec.microenv.util.SPUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by wm on 2018/7/3.
 * 登录/广告界面
 */

public class ActivityLauncher extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {jump();});
    }
    private void jump(){
        if (SPUtil.getBoolean(Constants.IS_LOGIN,false)){
            Constants.userID = SPUtil.getString(Constants.USER_ID,"");
            //已经登录后直接打开主界面
            ActivityMain.start(ActivityLauncher.this);
        }else{
            //未登录进入入口界面进行登录
            ActivityPortal.start(ActivityLauncher.this);
            finish();
        }
    }
}
