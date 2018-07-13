package com.wm.toec.microenv.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squareup.haha.perflib.Main;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.ui.portal.ActivityPortal;
import com.wm.toec.microenv.ui.setting.SettingActivity;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wm on 2018/7/3.
 * 主界面
 */

public class ActivityMain extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    /**
     * 开始main activity
     * @param context
     */
    public static void start(Context context){
        Intent intent = new Intent(context,ActivityMain.class);
        context.startActivity(intent);
    }
    /**
     * 注册事件总线
     */
    private void registeRxBus() {
        Disposable disposable = Rxbus.getInstance().toObservable(BaseMessage.class)
                .subscribe(new Consumer<BaseMessage>() {
                    @Override
                    public void accept(BaseMessage baseMessage) throws Exception {
                        if (baseMessage.getmCode()==4){
                            //退出该app
                            ActivityMain.this.finish();
                        }
                    }
                });
    }
}
