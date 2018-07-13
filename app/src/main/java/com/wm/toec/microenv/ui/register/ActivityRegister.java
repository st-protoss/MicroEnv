package com.wm.toec.microenv.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.databinding.ActivityRegisterBinding;
import com.wm.toec.microenv.ui.login.ActivityLogin;
import com.wm.toec.microenv.ui.main.ActivityMain;
import com.wm.toec.microenv.util.ToastUtil;
import com.wm.toec.microenv.viewmodel.register.RegisterCommand;
import com.wm.toec.microenv.viewmodel.register.RegisterViewModel;

/**
 * Created by toec on 2018/7/3.
 */

public class ActivityRegister extends BaseActivity<ActivityRegisterBinding> implements RegisterCommand {

    private RegisterViewModel registerViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("平台注册");
        registerViewModel = new RegisterViewModel(ActivityRegister.this);
        mBindView.setViewmodel(registerViewModel);
        registerViewModel.setRegisterCommand(this);
    }

    public void register(){
        registerViewModel.register();
    }
    public void getVerifyCode(){
        registerViewModel.getVerifyCode();
    }
    /**
     * 开始register activity
     * @param context
     */
    public static void start(Context context){
        Intent intent = new Intent(context,ActivityRegister.class);
        context.startActivity(intent);
    }
    @Override
    public void registerSuccess() {
        //注册成功关闭当前activity
        Intent intent = new Intent(ActivityRegister.this,ActivityMain.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerViewModel.onDestroy();
    }
}
