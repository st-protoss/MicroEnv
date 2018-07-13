package com.wm.toec.microenv.ui.portal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.databinding.ActivityPortalBinding;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.ui.login.ActivityLogin;
import com.wm.toec.microenv.ui.member.ActivityMemberDetail;
import com.wm.toec.microenv.ui.register.ActivityRegister;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wm on 2018/7/2.
 * 入口activity
 */

public class ActivityPortal extends AppCompatActivity implements View.OnClickListener {
    ActivityPortalBinding mBinding;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_portal);
        mBinding.portalLogin.setOnClickListener(this);
        mBinding.portalRegister.setOnClickListener(this);
        registeRxBus();
    }

    /**
     * 注册事件总线
     */
    private void registeRxBus() {
        disposable = Rxbus.getInstance().toObservable(BaseMessage.class)
                .subscribe(new Consumer<BaseMessage>() {
                    @Override
                    public void accept(BaseMessage baseMessage) throws Exception {
                        if (baseMessage.getmCode()==1){
                            //注册/登录成功 需要关闭当前的入口activity
                            ActivityPortal.this.finish();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.portal_login:{
                //登录
                ActivityLogin.start(ActivityPortal.this);
                break;
            }
            case R.id.portal_register:{
                //注册
                ActivityRegister.start(ActivityPortal.this);
                break;
            }
            default:break;
        }
    }
    /**
     * 开始login activity
     * @param context
     */
    public static void start(Context context){
        Intent intent = new Intent(context,ActivityPortal.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null){
            disposable.dispose();
        }
    }
}
