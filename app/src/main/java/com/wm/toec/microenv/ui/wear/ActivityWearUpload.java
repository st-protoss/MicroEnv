package com.wm.toec.microenv.ui.wear;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.haibin.calendarview.Calendar;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.WearResultBean;
import com.wm.toec.microenv.databinding.ActivityWearUploadBinding;
import com.wm.toec.microenv.util.ToastUtil;
import com.wm.toec.microenv.viewmodel.wear.WearUploadCommand;
import com.wm.toec.microenv.viewmodel.wear.WearUploadViewModel;


/**
 * 穿戴设备数据上传界面
 * Created by wm on 2018/7/11.
 */

public class ActivityWearUpload extends BaseActivity<ActivityWearUploadBinding> implements WearUploadCommand {
    private String memberId;
    private Calendar mCalendar;

    private WearUploadViewModel wearUploadViewModel;
    private String situation = null;//当日健康状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_upload);
        wearUploadViewModel = new WearUploadViewModel(this);
        handleIntent();
        setTitle("数据上传");
        wearUploadViewModel.getWearDayData(memberId, String.valueOf(mCalendar.getYear()),
                String.valueOf(mCalendar.getMonth()), String.valueOf(mCalendar.getDay()));
    }

    /**
     * 处理Intent
     */
    private void handleIntent() {
        Intent intent = getIntent();
        memberId = intent.getStringExtra("memberId");
        mCalendar = (Calendar) intent.getSerializableExtra("date");
    }

    @Override
    public void loadSuccess(WearResultBean wearResultBean) {
        wearUploadViewModel.refreshBoard(wearResultBean);
        situation = wearResultBean.getHealthCondition().getConditionDescription();
    }

    @Override
    public void networkError() {
        //待完善
        ToastUtil.showToast("网络错误，请尝试重新连接");
    }

    /**
     * 编辑睡眠时间
     */
    public void editSleepTime() {
        new MaterialDialog.Builder(this)
                .title("编辑睡眠时间")
                .content("请填写设备上记录的睡眠时间")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(1, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }

    /**
     * 编辑深度睡眠时间
     */
    public void editDeepSleepTime() {
        new MaterialDialog.Builder(this)
                .title("编辑深度睡眠时间")
                .content("请填写设备上记录的深度睡眠时间")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(2, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }

    /**
     * 编辑心跳
     */
    public void editHeartbeat() {
        new MaterialDialog.Builder(this)
                .title("编辑心率")
                .content("请填写设备上记录的心率")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(6, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }

    /**
     * 编辑血压高压
     */
    public void editBloodPressureH() {
        new MaterialDialog.Builder(this)
                .title("编辑血压高压")
                .content("请填写设备上记录的高压")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(3, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }

    /**
     * 编辑血压低压
     */
    public void editBloodPressureL() {
        new MaterialDialog.Builder(this)
                .title("编辑血压低压")
                .content("请填写设备上记录的低压")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(4, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }

    /**
     * 编辑血糖
     */
    public void editGlucose() {
        new MaterialDialog.Builder(this)
                .title("编辑血糖")
                .content("请填写设备上记录的血糖值")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(5, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();

    }

    /**
     * 编辑步数
     */
    public void editFootstep() {
        new MaterialDialog.Builder(this)
                .title("编辑当日步数")
                .content("请填写设备上记录的步数")
                .input("", "", false, ((dialog, input) -> {
                }))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    wearUploadViewModel.refreshItem(7, str);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }

    /**
     * 编辑健康状况
     */
    public void editSituation() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("编辑健康状况")
                .positiveText("确认")
                .customView(R.layout.layout_health_situation, false)
                .onPositive(((dialog, which) -> {
                    View v = dialog.getCustomView();
                    RadioGroup situationGroup = v.findViewById(R.id.situation_group);
                    AppCompatEditText description = v.findViewById(R.id.health_description);
                    String str = "优";
                    int condition = 1;
                    String desStr = null;
                    switch (situationGroup.getCheckedRadioButtonId()) {
                        case R.id.situation_good:
                            str = "优";
                            condition = 1;
                            break;
                        case R.id.situation_mid:
                            str = "良";
                            condition = 2;
                            break;
                        case R.id.situation_bad:
                            str = "疾病";
                            condition = 3;
                            break;
                    }
                    wearUploadViewModel.refreshItem(8, str);
                    if (description.getText() != null) {
                        desStr = description.getText().toString();
                    } else {
                        desStr = "暂无描述";
                    }
                    wearUploadViewModel.setSituation(condition, desStr);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                })).build();
        View v = materialDialog.getCustomView();
        AppCompatEditText description = v.findViewById(R.id.health_description);
        if (situation != null) {
            description.setText(situation);
        } else {
            description.setText("");
        }
        materialDialog.show();
    }

    /**
     * 同步穿戴设备数据
     */
    public void sync() {
        wearUploadViewModel.uploadWearDayData(memberId, mCalendar);
    }
}
