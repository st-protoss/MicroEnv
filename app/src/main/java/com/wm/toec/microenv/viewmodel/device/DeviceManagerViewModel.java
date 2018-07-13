package com.wm.toec.microenv.viewmodel.device;

import android.arch.lifecycle.ViewModel;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.DeviceInfoBean;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by toec on 2018/7/3.
 */

public class DeviceManagerViewModel extends ViewModel {
    private DeviceManagerCommand deviceManagerCommand;
    private BaseActivity mActivity;

    public DeviceManagerViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }


    public void setDeviceManagerCommand(DeviceManagerCommand deviceManagerCommand) {
        this.deviceManagerCommand = deviceManagerCommand;
    }
    /**
     * 获取该用户ID下的用户信息
     */
    public void refresh(){
        Disposable disposable = HttpSet.getInstance().getToecServer().getDeviceInfo(Constants.userID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(deviceInfoBean -> {
                    if (deviceInfoBean.getIsBind()==1){
                        //已绑定
                        deviceManagerCommand.showBind(deviceInfoBean);
                    }else{
                        //未绑定
                        deviceManagerCommand.showUnbind();
                    }
                },throwable -> {
                    ToastUtil.showToast("网络错误");
                });
        mActivity.addDisposable(disposable);
    }
    /**
     * 绑定设备
     */
    public void bind(String deviceId){
        Disposable disposable = HttpSet.getInstance().getToecServer().bindDevice(Constants.userID,deviceId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(bindResultBean ->{
                   if (bindResultBean.getStatus()==0){
                       ToastUtil.showToast("绑定失败！");
                   }else {
                       ToastUtil.showToast("绑定成功！");
                       refresh();
                   }
                },throwable -> {
                    ToastUtil.showToast("网络错误！");
                });
        mActivity.addDisposable(disposable);
    }

    /**
     * 解绑设备
     */
    public void unbind(){
        Disposable disposable = HttpSet.getInstance().getToecServer().unbindDevice(Constants.userID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(bindResultBean -> {
                    if (bindResultBean.getStatus()==0){
                        ToastUtil.showToast("解绑失败！");
                    }else {
                        ToastUtil.showToast("解绑成功！");
                        refresh();
                    }
                },throwable -> {
                    ToastUtil.showToast("网络错误！");
                });
        mActivity.addDisposable(disposable);
    }
}
