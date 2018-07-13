package com.wm.toec.microenv.viewmodel.login;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.LoginBean;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.util.SPUtil;
import com.wm.toec.microenv.util.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/2.
 * login viewmodel
 */

public class LoginViewModel extends ViewModel {
    public final ObservableField<String> phone = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    private BaseActivity mActivity;
    private LoginCommand mLoginCommand;

    public LoginViewModel(BaseActivity activity){
        this.mActivity = activity;
    }
    public void setLoginComman(LoginCommand mLoginCommand) {
        this.mLoginCommand = mLoginCommand;
    }
    public void login(){
        if (!verifyData()){
            return;
        }
        //登录逻辑
        HttpSet.getInstance().getToecServer().login(phone.get(),password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mActivity.addDisposable(d);
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        switch (loginBean.getStatus()){
                            case 0:{
                                ToastUtil.showToast("密码错误！");
                                break;
                            }
                            case 1:{
                                SPUtil.putBoolean(Constants.IS_LOGIN,true);
                                SPUtil.putString(Constants.USER_ID,loginBean.getUserId());
                                Constants.userID = loginBean.getUserId();
                                Constants.isLogin = true;
                                mLoginCommand.loginSuccess();
                                Rxbus.getInstance().post(new BaseMessage(1,""));
                                break;
                            }
                            case 2:{
                                ToastUtil.showToast("该手机号尚未在平台注册");
                                break;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast("网络错误！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    private boolean verifyData() {
        if (TextUtils.isEmpty(phone.get())) {
            ToastUtil.showToast("请输入用户手机");
            return false;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtil.showToast("请输入密码");
            return false;
        }
        return true;
    }

    /**
     * 释放控制登录界面的回调
     */
    public void onDestroy(){
        mLoginCommand = null;
    }

}
