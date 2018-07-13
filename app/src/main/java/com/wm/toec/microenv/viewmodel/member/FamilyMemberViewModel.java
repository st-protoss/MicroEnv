package com.wm.toec.microenv.viewmodel.member;

import android.arch.lifecycle.ViewModel;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.bean.MemberResultBean;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/5.
 */

public class FamilyMemberViewModel extends ViewModel {
    private FamilyMemberCommand familyMemberCommand;
    private BaseActivity mActivity;

    public FamilyMemberViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
    public void setFamilyMemberCommand(FamilyMemberCommand familyMemberCommand) {
        this.familyMemberCommand = familyMemberCommand;
    }
    public void loadFamilyList(){
        Disposable disposable = HttpSet.getInstance().getToecServer().getFamilyList(Constants.userID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(familyMemberList -> {
                    familyMemberCommand.loadFamilyListSuccess(familyMemberList);
                },throwable -> {
                    familyMemberCommand.networkError();
                });
        mActivity.addDisposable(disposable);
    }
    public void deleteMember(FamilyMemberBean bean){
        Disposable disposable = HttpSet.getInstance().getToecServer().deleteMember(Constants.userID,bean.getMemberId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(memberResultBean -> {
                    if (memberResultBean.getResult()==1){
                        familyMemberCommand.deleteSuccess();
                    }else {
                        ToastUtil.showToast("删除失败！");
                    }
                },throwable -> {
                    familyMemberCommand.networkError();
                });
        mActivity.addDisposable(disposable);
    }
}
