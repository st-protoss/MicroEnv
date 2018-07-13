package com.wm.toec.microenv.viewmodel.wear;

import android.arch.lifecycle.ViewModel;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.data.http.HttpSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by toec on 2018/7/9.
 */

public class WearManagerViewModel extends ViewModel {
    private BaseActivity mActivity;
    private WearManagerCommand wearManagerCommand;
    public void setWearManagerCommand(WearManagerCommand wearManagerCommand) {
        this.wearManagerCommand = wearManagerCommand;
    }
    public WearManagerViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
    public void loadFamilyList(){
        Disposable disposable = HttpSet.getInstance().getToecServer().getFamilyList(Constants.userID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(familyMemberList -> {
                    wearManagerCommand.loadFamilyListSuccess(familyMemberList);
                },throwable -> {
                    wearManagerCommand.networkError();
                });
        mActivity.addDisposable(disposable);
    }
}
