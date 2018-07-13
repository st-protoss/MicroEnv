package com.wm.toec.microenv.viewmodel.wear;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.haibin.calendarview.Calendar;
import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.WearResultBean;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/11.
 */

public class WearUploadViewModel extends ViewModel {
    public ObservableField<String> sleepTime = new ObservableField<>();
    public ObservableField<String> deepSleepTime = new ObservableField<>();
    public ObservableField<String> heartbeat = new ObservableField<>();
    public ObservableField<String> bloodPressureH = new ObservableField<>();
    public ObservableField<String> bloodPressureL = new ObservableField<>();
    public ObservableField<String> glocuse = new ObservableField<>();
    public ObservableField<String> footstep = new ObservableField<>();

    public ObservableField<String> situation = new ObservableField<>();

    private BaseActivity mActivity;
    private WearUploadCommand wearUploadCommand;

    private WearResultBean.HealthCondition healthSituation;

    public void setWearUploadCommand(WearUploadCommand wearUploadCommand) {
        this.wearUploadCommand = wearUploadCommand;
    }

    public WearUploadViewModel(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setSituation(int condition,String description){
        if (healthSituation == null){
            healthSituation = new WearResultBean.HealthCondition();
        }
        healthSituation.setCondition(condition);
        healthSituation.setConditionDescription(description);
    }

    public void refreshItem(int type, String content){
        switch (type){
            case 1://sleeptime
                sleepTime.set(content+"h");
                break;
            case 2://deepsleeptime
                deepSleepTime.set(content+"h");
                break;
            case 3://bloodpressureH
                bloodPressureH.set(content+"mmhg");
                break;
            case 4://bloodpressureL
                bloodPressureL.set(content+"mmhg");
                break;
            case 5://glocuse
                glocuse.set(content+"mmo/l");
                break;
            case 6://heartbeat
                heartbeat.set(content+"次");
                break;
            case 7://footstep
                footstep.set(content+"步");
                break;
            case 8://situation
                situation.set(content);
                break;
        }
    }
    public void refreshBoard(WearResultBean wearResultBean){
        WearResultBean.WearData wearData = wearResultBean.getWearData();
        sleepTime.set(wearData.getSleepTime()==null?"尚未填写":wearData.getSleepTime()+"h");
        deepSleepTime.set(wearData.getDeepSleepTime()==null?"尚未填写":wearData.getDeepSleepTime()+"h");
        bloodPressureH.set(wearData.getBloodPressureH()==null?"尚未填写":wearData.getBloodPressureH()+"mmHg");
        bloodPressureL.set(wearData.getBloodPressureL()==null?"尚未填写":wearData.getBloodPressureL()+"mmHg");
        glocuse.set(wearData.getBloodGlucose()==null?"尚未填写":wearData.getBloodGlucose()+"mmo/l");
        footstep.set(wearData.getFootStep()==null?"尚未填写":wearData.getFootStep()+"步");
        heartbeat.set(wearData.getHeartbeat()==null?"尚未填写":wearData.getHeartbeat()+"次");

        WearResultBean.HealthCondition condition = wearResultBean.getHealthCondition();
        if (condition==null){
            situation.set("尚未填写");
        }else{
            switch (condition.getCondition()){
                case 1:situation.set("优");break;
                case 2:situation.set("良");break;
                case 3:situation.set("疾病");break;
            }

        }
    }
    public void getWearDayData(String memberId,String year,String month,String day){
        Disposable disposable = HttpSet.getInstance().getToecServer().getDayWearData(Constants.userID,memberId,year,month,day)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(wearResultBean -> {
                    wearUploadCommand.loadSuccess(wearResultBean);
                },throwable -> {
                    wearUploadCommand.networkError();
                });
        mActivity.addDisposable(disposable);
    }
    public void uploadWearDayData(String memberId, Calendar calendar){
        WearResultBean uploadBean = new WearResultBean();
        //设置穿戴数据上传
        WearResultBean.WearData uploadWearData = new WearResultBean.WearData();
        if(sleepTime.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setSleepTime(null);
        }else{
            uploadWearData.setSleepTime(sleepTime.get());
        }
        if(deepSleepTime.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setDeepSleepTime(null);
        }else{
            uploadWearData.setDeepSleepTime(deepSleepTime.get());
        }
        if(bloodPressureH.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setBloodPressureH(null);
        }else{
            uploadWearData.setBloodPressureH(bloodPressureH.get());
        }
        if(bloodPressureL.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setBloodPressureL(null);
        }else{
            uploadWearData.setBloodPressureL(bloodPressureL.get());
        }
        if(glocuse.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setBloodGlucose(null);
        }else{
            uploadWearData.setBloodGlucose(glocuse.get());
        }
        if(heartbeat.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setHeartbeat(null);
        }else{
            uploadWearData.setHeartbeat(heartbeat.get());
        }
        if(footstep.get().equalsIgnoreCase("尚未填写")){
            uploadWearData.setFootStep(null);
        }else{
            uploadWearData.setFootStep(footstep.get());
        }
        uploadBean.setHealthCondition(healthSituation);
        uploadBean.setDay(String.valueOf(calendar.getDay()));
        uploadBean.setMonth(String.valueOf(calendar.getMonth()));
        uploadBean.setYear(String.valueOf(calendar.getYear()));
        Disposable disposable = HttpSet.getInstance().getToecServer().uploadDayWearData(Constants.userID,memberId,uploadBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(integer -> {
                    if (integer.intValue()==1){
                        ToastUtil.showToast("上传成功");
                    }else if(integer.intValue()==2){
                        ToastUtil.showToast("上传失败");
                    }
                },throwable -> {
                    wearUploadCommand.networkError();
                });
        mActivity.addDisposable(disposable);
    }
}
