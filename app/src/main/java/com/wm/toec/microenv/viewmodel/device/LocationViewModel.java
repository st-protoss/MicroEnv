package com.wm.toec.microenv.viewmodel.device;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/4.
 */

public class LocationViewModel extends ViewModel {
    public final ObservableField<String> location = new ObservableField<>();
    private BaseActivity mActivity;
    private LocationCommand locationCommand;

    public LocationViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
    public void setLocationCommand(LocationCommand locationCommand) {
        this.locationCommand = locationCommand;
    }

    public void confirmLocation() {
        String locationStr = location.get();
        Disposable disposable = HttpSet.getInstance().getToecServer().changeLocation(Constants.userID, locationStr)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(locationBean -> {
                    if (locationBean.getResult()==1){
                        locationCommand.bindSuccess();
                        //通知devicemanager界面刷新界面
                        Rxbus.getInstance().post(new BaseMessage(2,""));
                    }else{
                        locationCommand.bindFail();
                    }
                        },
                        throwable -> {
                            ToastUtil.showToast("网络错误！更改未成功");
                        });
        mActivity.addDisposable(disposable);
    }

    /**
     * 定位失败
     */
    public void firstLocateFail(){
        location.set("初始定位失败");
    }
    /**
     * 定位成功
     */
    public void firstLocateSuccess(String mlocation){
        location.set(mlocation);
    }
    /**
     * 正在定位中
     */
    public void locating(){
        location.set("正在定位中...");
    }
    /**
     * 逆向geo失败
     */
    public void reverseGeoFail(){
        location.set("定位失败，请重试");
    }
    /**
     * 逆向geo成功
     */
    public void reverseGeoSuccess(String mLocation){
        location.set("当前位置："+mLocation);
    }


}
