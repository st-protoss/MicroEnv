package com.wm.toec.microenv.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.app.Constants;
import com.wm.toec.microenv.bean.FamilyMemberIndexBean;
import com.wm.toec.microenv.customview.MainCircleView;
import com.wm.toec.microenv.data.http.HttpSet;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.ui.device.ActivityDeviceManager;
import com.wm.toec.microenv.ui.member.ActivityFamilyManager;
import com.wm.toec.microenv.ui.setting.ActivitySetting;
import com.wm.toec.microenv.ui.wear.ActivityWearManager;
import com.wm.toec.microenv.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2018/7/3.
 * 主界面
 */

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {
    TextView year;
    TextView day;
    TextView week;
    TextView txt;
    ImageView notice;
    ImageView setting;
    ImageView change;
    ImageView refresh;
    FloatingActionButton member;
    FloatingActionButton device;
    FloatingActionButton upload;
    MainCircleView mcv;

    //复合的订阅 方便统一取消订阅
    private CompositeDisposable mCompositeDisposable;
    //家庭健康状况列表
    private List<FamilyMemberIndexBean> familyMemberIndexBeans = new ArrayList<>();
    private int currentPos = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initWidgets();
        refreshBoard();
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        handleData();
        registeRxBus();
    }

    /**
     * 处理数据
     */
    private void handleData() {
        Disposable disposable = HttpSet.getInstance().getToecServer().getFamilyListWithIndex(Constants.userID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(FamilyMemberIndexBeans -> {
                    txt.setText("欢迎来到健康微环境助手！");
                    familyMemberIndexBeans.clear();
                    familyMemberIndexBeans.addAll(FamilyMemberIndexBeans);
                    if (FamilyMemberIndexBeans.size() == 0) {
                        mcv.setValueWithAnim(0f);
                        mcv.setUnit("暂无");
                    } else {
                        mcv.setValueWithAnim(FamilyMemberIndexBeans.get(0).getHealthIndex());
                        mcv.setUnit(FamilyMemberIndexBeans.get(0).getFamilyName());
                    }
                }, throwable -> {
                    mcv.setUnit("暂无！");
                    mcv.setValueWithAnim(0f);
                    txt.setText("网络连接错误，请尝试刷新");
                });
        mCompositeDisposable.add(disposable);
    }

    /**
     * 刷新界面
     */
    private void refreshBoard() {

    }

    /**
     * 初始化控件
     */
    private void initWidgets() {
        year = (TextView) findViewById(R.id.main_year);
        day = (TextView) findViewById(R.id.main_day);
        week = (TextView) findViewById(R.id.main_week);
        txt = (TextView) findViewById(R.id.main_txt);
        notice = (ImageView) findViewById(R.id.main_notice);
        setting = (ImageView) findViewById(R.id.main_setting);
        change = (ImageView) findViewById(R.id.main_change);
        refresh = (ImageView) findViewById(R.id.main_refresh);
        member = (FloatingActionButton) findViewById(R.id.main_member);
        device = (FloatingActionButton) findViewById(R.id.main_device);
        upload = (FloatingActionButton) findViewById(R.id.main_upload);
        mcv = (MainCircleView) findViewById(R.id.main_cv);

    }

    /**
     * 开始main activity
     * @param context
     */
    public static void start(Context context){
        Intent intent = new Intent(context,ActivityMain.class);
        context.startActivity(intent);
    }
    /**
     * 注册事件总线
     */
    private void registeRxBus() {
        Disposable disposable = Rxbus.getInstance().toObservable(BaseMessage.class)
                .subscribe(new Consumer<BaseMessage>() {
                    @Override
                    public void accept(BaseMessage baseMessage) throws Exception {
                        if (baseMessage.getmCode()==4){
                            //退出该app
                            ActivityMain.this.finish();
                        }
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_notice: {
                Intent intent = new Intent(ActivityMain.this, ActivitySetting.class);
                startActivity(intent);
                break;
            }
            case R.id.main_setting: {
                Intent intent = new Intent(ActivityMain.this, ActivitySetting.class);
                startActivity(intent);
                break;
            }
            case R.id.main_cv: {
                if (familyMemberIndexBeans.size() == 0) {
                    break;
                }
                String memberId = familyMemberIndexBeans.get(currentPos).getMemberId();
                Intent intent = new Intent(ActivityMain.this, ActivityReport.class);
                intent.putExtra("memberId", memberId);
                startActivity(intent);
                break;
            }
            case R.id.main_member: {
                Intent intent = new Intent(ActivityMain.this, ActivityFamilyManager.class);
                startActivity(intent);
                break;
            }
            case R.id.main_device: {
                Intent intent = new Intent(ActivityMain.this, ActivityDeviceManager.class);
                startActivity(intent);
                break;
            }
            case R.id.main_upload: {
                Intent intent = new Intent(ActivityMain.this, ActivityWearManager.class);
                startActivity(intent);
                break;
            }
            case R.id.main_refresh: {
                handleData();
                break;
            }
            case R.id.main_change: {
                if (familyMemberIndexBeans.size() == 0) {
                    ToastUtil.showToast("未找到有效成员！");
                    break;
                }
                if (familyMemberIndexBeans.size() == 1) {
                    break;
                }
                if (familyMemberIndexBeans.size() > 1) {
                    if (++currentPos < familyMemberIndexBeans.size()) {
                        mcv.setValueWithAnim(familyMemberIndexBeans.get(currentPos).getHealthIndex());
                        mcv.setUnit(familyMemberIndexBeans.get(currentPos).getFamilyName());
                    }
                    if (currentPos == familyMemberIndexBeans.size()) {
                        currentPos = 0;
                        mcv.setValueWithAnim(familyMemberIndexBeans.get(currentPos).getHealthIndex());
                        mcv.setUnit(familyMemberIndexBeans.get(currentPos).getFamilyName());
                    }
                }

                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeDisposable != null || !this.mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
        }
    }
}
