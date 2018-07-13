package com.wm.toec.microenv.viewmodel.register;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.RegisterBean;
import com.wm.toec.microenv.bean.VerifyCodeBean;
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
 * Created by toec on 2018/7/3.
 */

public class RegisterViewModel extends ViewModel {
    public final ObservableField<String> phone = new ObservableField<>();
    public final ObservableField<String> verifycode = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    private BaseActivity mActivity;
    private RegisterCommand registerCommand;

    public RegisterViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setRegisterCommand(RegisterCommand registerCommand) {
        this.registerCommand = registerCommand;
    }

    /**
     * 注册逻辑
     */
    public void register(){
        if(!verifyData()){
            return;
        }
        HttpSet.getInstance().getToecServer().register(phone.get(),verifycode.get(),password.get())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RegisterBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mActivity.addDisposable(d);
                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {
                        switch (registerBean.getStatus()){
                            case 0:{
                                ToastUtil.showToast("注册失败！");
                                break;
                            }
                            case 1:{
                                SPUtil.putBoolean(Constants.IS_LOGIN,true);
                                SPUtil.putString(Constants.USER_ID,"");
                                Constants.userID = registerBean.getUserId();
                                Constants.isLogin = true;
                                registerCommand.registerSuccess();
                                Rxbus.getInstance().post(new BaseMessage(1,""));
                                break;
                            }
                            case 2:{
                                ToastUtil.showToast("验证码不匹配，请重新获取二维码！");
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

    /**
     * 获取验证码
     */
    public void getVerifyCode(){
        if (TextUtils.isEmpty(phone.get())){
            ToastUtil.showToast("请输入注册用的手机号");
            return;
        }
        HttpSet.getInstance().getToecServer().getVerifyCode(phone.get())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<VerifyCodeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mActivity.addDisposable(d);
                    }

                    @Override
                    public void onNext(VerifyCodeBean verifyCodeBean) {
                        switch (verifyCodeBean.getStatus()){
                            case 0:{
                                ToastUtil.showToast("获取失败！");
                                break;
                            }
                            case 1:{
                                ToastUtil.showToast("获取成功");
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
            ToastUtil.showToast("请输入注册用的手机号");
            return false;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtil.showToast("请输入初始密码");
            return false;
        }
        if(TextUtils.isEmpty(verifycode.get())){
            ToastUtil.showToast("验证码不能为空");
        }
        return true;
    }

    /**
     * 释放registerCommand
     */
    public void onDestroy(){
        registerCommand = null;
    }
}
