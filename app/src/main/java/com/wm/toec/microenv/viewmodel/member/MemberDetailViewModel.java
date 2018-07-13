package com.wm.toec.microenv.viewmodel.member;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.data.http.HttpSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by toec on 2018/7/9.
 */

public class MemberDetailViewModel extends ViewModel {
    public ObservableField<String> familyName = new ObservableField<>();
    public ObservableField<String> weight = new ObservableField<>();
    public ObservableField<String> height = new ObservableField<>();
    public ObservableField<String> birthday = new ObservableField<>();

    private BaseActivity mActivity;
    private FamilyDetailCommand familyDetailCommand;

    public void setFamilyDetailCommand(FamilyDetailCommand familyDetailCommand) {
        this.familyDetailCommand = familyDetailCommand;
    }
    public MemberDetailViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void refreshBoard(String name,String weigh,String heigh,String birth){
        familyName.set(name);
        weight.set(weigh);
        height.set(heigh);
        birthday.set(birth);
    }
    public void addMember(String name,String weigh,String heigh,String birt){
        Disposable disposable = HttpSet.getInstance().getToecServer().addMember(Constants.userID,name,heigh,weigh,birt)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(memberResultBean->{
                    if (memberResultBean.getResult()==0){
                        familyDetailCommand.oprateFail();
                    }else if (memberResultBean.getResult()==1){
                        familyDetailCommand.addSuccess();
                    }
                },throwable ->{
                    familyDetailCommand.networkError();
                });
        mActivity.addDisposable(disposable);
    }
    public void editMember(String name,String weigh,String heigh,String birt){
       Disposable disposable =  HttpSet.getInstance().getToecServer().editMember(Constants.userID,name,heigh,weigh,birt)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(memberResultBean->{
                    if (memberResultBean.getResult()==0){
                        familyDetailCommand.oprateFail();
                    }else if (memberResultBean.getResult()==1){
                        familyDetailCommand.editSuccess();
                    }
                },throwable ->{
                    familyDetailCommand.networkError();
                });
       mActivity.addDisposable(disposable);
    }

}
