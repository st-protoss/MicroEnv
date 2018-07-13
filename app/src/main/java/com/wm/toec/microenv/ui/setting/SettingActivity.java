package com.wm.toec.microenv.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.app.App;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.databinding.ActivitySettingBinding;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.viewmodel.setting.SettingCommand;
import com.wm.toec.microenv.viewmodel.setting.SettingViewModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by toec on 2018/7/12.
 */

public class SettingActivity extends BaseActivity<ActivitySettingBinding> implements SettingCommand {
    private SettingViewModel settingViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingViewModel = new SettingViewModel(this);
        settingViewModel.setSettingCommand(this);
        settingViewModel.displayCache();
        registeRxBus();
    }
    public void about(){
        new MaterialDialog.Builder(this)
                .title("关于微环境助手")
                .customView(R.layout.layout_about,true)
                .positiveText("关闭")
                .onPositive(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }
    public void deleteCache(){
        settingViewModel.deleteCache();
    }
    public void checkUpdate(){
        settingViewModel.checkUpdate();
    }

    public void exit(){
        new MaterialDialog.Builder(this)
                .title("退出系统")
                .content("确认退出微环境助手么？")
                .positiveText("退出")
                .negativeText("取消")
                .onPositive(((dialog, which) -> {
                    //退出本app
                    settingViewModel.exit();
                }))
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
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
                            SettingActivity.this.finish();
                        }
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void update(String targetVersion ,String targetSize) {
        new MaterialDialog.Builder(this)
                .title("检测到版本更新")
                .content("检测到新的版本，是否进行更新？")
                .positiveText("下载")
                .negativeText("忽略")
                .onPositive(((dialog, which) -> {
                    //开启应用市场
                    Uri uri = Uri.parse("market://details?id="+ App.getAppContext().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }))
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }
}
