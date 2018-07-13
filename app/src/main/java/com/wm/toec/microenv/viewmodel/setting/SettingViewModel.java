package com.wm.toec.microenv.viewmodel.setting;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.support.design.widget.Snackbar;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.util.FileUtil;
import com.wm.toec.microenv.util.SPUtil;
import com.wm.toec.microenv.util.ToastUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/13.
 */

public class SettingViewModel extends ViewModel {
    public ObservableField<String> cache = new ObservableField<>();

    private BaseActivity mActivity;
    private SettingCommand settingCommand;

    public SettingViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setSettingCommand(SettingCommand settingCommand) {
        this.settingCommand = settingCommand;
    }

    /**
     * 改变推送开关
     * @param isChecked
     */
    public void switchPush(boolean isChecked){
        if (isChecked){
            SPUtil.putBoolean(Constants.PUSH_SWITCH,true);
        }else{
            SPUtil.putBoolean(Constants.PUSH_SWITCH,false);
        }
    }

    /**
     *删除网络缓存
     */
    public void deleteCache(){
        //清除网络缓存
        Disposable disposable = Observable.just(FileUtil.delete(new File(Constants.NET_CACHE_URL)))
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    ToastUtil.showToast("缓存已经清除！");
                    displayCache();
                });
        mActivity.addDisposable(disposable);
    }
    /**
     * 显示缓存
     */
    public void displayCache(){
        String fileSize = FileUtil.getAutoFileOrFilesSize(Constants.NET_CACHE_URL);
        cache.set(fileSize);
    }

    /**
     * 退出本app
     */
    public void exit(){
        SPUtil.putBoolean(Constants.IS_LOGIN,false);
        SPUtil.putString(Constants.USER_ID,"");
        Constants.isLogin = false;
        Constants.userID = "";
        Rxbus.getInstance().post(new BaseMessage(4,""));
    }
    /**
     * 查询版本更新
     */
    public void checkUpdate(){
        String currentVersion = SPUtil.getString(Constants.VERSION,"1.0.0");
       Disposable disposable =  HttpSet.getInstance().getToecServer().checkVersion(currentVersion)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(versionResultBean -> {
                    if (versionResultBean.getNeedUpdate()==0){
                        ToastUtil.showToast("当前已经是最新版本!");
                    }else {
                        settingCommand.update(versionResultBean.getTargetVersion(),versionResultBean.getTargetSize());
                    }
                },throwable -> {
                    ToastUtil.showToast("网络错误");
                });
       mActivity.addDisposable(disposable);
    }
}
