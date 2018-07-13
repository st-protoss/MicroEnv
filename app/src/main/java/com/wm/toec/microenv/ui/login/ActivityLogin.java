package com.wm.toec.microenv.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.databinding.ActivityLoginBinding;
import com.wm.toec.microenv.ui.main.ActivityMain;
import com.wm.toec.microenv.viewmodel.login.LoginCommand;
import com.wm.toec.microenv.viewmodel.login.LoginViewModel;

/**
 * Created by toec on 2018/7/2.
 */

public class ActivityLogin extends BaseActivity<ActivityLoginBinding> implements LoginCommand{

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录平台");
        loginViewModel = new LoginViewModel(this);
        mBindView.setViewmodel(loginViewModel);
        loginViewModel.setLoginComman(this);
    }

    public void login(){
        loginViewModel.login();
    }

    /**
     * 开始login activity
     * @param context
     */
    public static void start(Context context){
        Intent intent = new Intent(context,ActivityLogin.class);
        context.startActivity(intent);
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSuccess() {
        Intent intent = new Intent(ActivityLogin.this,ActivityMain.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginViewModel.onDestroy();
    }
}
