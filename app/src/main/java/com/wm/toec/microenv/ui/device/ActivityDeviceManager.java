package com.wm.toec.microenv.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.DeviceInfoBean;
import com.wm.toec.microenv.databinding.ActivityDeviceManagerBinding;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.util.DebugUtil;
import com.wm.toec.microenv.util.ToastUtil;
import com.wm.toec.microenv.viewmodel.device.DeviceManagerCommand;
import com.wm.toec.microenv.viewmodel.device.DeviceManagerViewModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wm on 2018/7/3.
 * 设备管理界面
 */

public class ActivityDeviceManager extends BaseActivity<ActivityDeviceManagerBinding> implements DeviceManagerCommand{
    public final static int QRCODE = 1;
    private DeviceManagerViewModel deviceManagerViewModel;
    private FrameLayout contentFrame;//内容的容器

    private Disposable disposable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);
        registeRxBus();
        contentFrame = (FrameLayout)mBindView.getRoot().findViewById(R.id.device_manager_container);
        deviceManagerViewModel = new DeviceManagerViewModel(this);
        deviceManagerViewModel.setDeviceManagerCommand(this);
        //根据获取的设备情况显示不同的界面
        deviceManagerViewModel.refresh();
    }

    /**
     * 刷新界面
     */
    public void refresh(){
        deviceManagerViewModel.refresh();
    }

    /**
     * 注册事件总线
     */
    private void registeRxBus() {
        disposable = Rxbus.getInstance().toObservable(BaseMessage.class)
                .subscribe(new Consumer<BaseMessage>() {
                    @Override
                    public void accept(BaseMessage baseMessage) throws Exception {
                        if (baseMessage.getmCode()==2){
                            //设备解绑 绑定 更改位置后刷新界面
                            refresh();
                        }
                        if (baseMessage.getmCode()==4){
                            //退出该app
                            ActivityDeviceManager.this.finish();
                        }
                    }
                });
        addDisposable(disposable);
    }
    @Override
    public void showBind(DeviceInfoBean deviceInfo) {
        mBaseView.toolbar.inflateMenu(R.menu.menu_device_manager);
        mBaseView.toolbar.setOnMenuItemClickListener(item->{
            switch (item.getItemId()) {
                case R.id.action_refresh:// 刷新
                    refresh();
                    break;
            }
            return false;
        });
        LinearLayout bindView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_device_main,contentFrame,false);
        contentFrame.removeAllViews();
        contentFrame.addView(bindView);
        //改变卡片的样式
        CardView cv_device = (CardView)bindView.findViewById(R.id.device_card);
        TextView tv_status = (TextView)bindView.findViewById(R.id.device_card_status);
        TextView tv_location = (TextView)bindView.findViewById(R.id.device_card_location);
        TextView tv_id = (TextView)bindView.findViewById(R.id.device_card_id);
        if (deviceInfo.getStatus()==0){
            //离线样式
            cv_device.setCardBackgroundColor(getResources().getColor(R.color.colorDeviceOffline));
            tv_status.setText("设备处于离线中");
        }else{
            //在线样式
            cv_device.setCardBackgroundColor(getResources().getColor(R.color.colorDeviceOnline));
            tv_status.setText("正在采集数据");
        }
        if (deviceInfo.getLocationInfo()!=null){
            tv_location.setText(deviceInfo.getLocationInfo());
        }else{
            tv_location.setText("暂无位置信息");
        }
        if (deviceInfo.getDeviceId()!=null){
            tv_id.setText(deviceInfo.getDeviceId());
        }else{
            tv_id.setText("暂无设备ID号");
        }

        AppCompatButton btn_unbind = bindView.findViewById(R.id.device_card_unbind);
        AppCompatButton btn_changeLocation = bindView.findViewById(R.id.device_card_changelocation);
        btn_unbind.setOnClickListener(view -> {
            //弹出弹框 确认是否解绑设备
            new MaterialDialog.Builder(this)
                    .title("解绑设备")
                    .content("确认解绑当前设备？\n一旦解绑当前微环境数据将丢失")
                    .positiveText("解绑")
                    .negativeText("取消")
                    .onPositive((dialog, which) -> {
                deviceManagerViewModel.unbind();
                dialog.dismiss();
                    })
                    .onNegative((dialog, which) -> {
                dialog.dismiss();
                    })
                    .show();
        });
        btn_changeLocation.setOnClickListener(view -> {
            //开启地图activity
        });
    }

    @Override
    public void showUnbind() {
        LinearLayout bindView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_unbind_status,contentFrame,false);
        contentFrame.removeAllViews();
        contentFrame.addView(bindView);
        AppCompatButton btn_add = bindView.findViewById(R.id.add_device);
        btn_add.setOnClickListener(view -> {
            //bottomsheet弹起
            BottomSheet.Builder builder = new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog)
                    .title("添加方式选择")
                    .sheet(R.menu.menu_device_bottom_sheet)
                    .listener((dialog,i)->{
                switch (i){
                    case R.id.manual_select:{
                        //手动选择 弹框
                        new MaterialDialog.Builder(this)
                                .title("手动绑定")
                                .content("请输入设备ID")
                                .input("在设备盒子上的ID","", ((dialog1, input) ->{} ))
                                .positiveText("添加")
                                .onPositive(((dialog1, which) -> {
                                    String deviceId = dialog1.getInputEditText().toString();
                                    deviceManagerViewModel.bind(deviceId);
                                    dialog1.dismiss();
                                }))
                                .negativeText("取消")
                                .onNegative(((dialog1, which) -> {
                                    dialog1.dismiss();
                                }))
                                .show();
                        break;
                    }
                    case R.id.qrcode_select:{
                        //二维码选择
                        Intent intent = new Intent(ActivityDeviceManager.this, CaptureActivity.class);
                        this.startActivityForResult(intent,QRCODE);
                        break;
                    }
                }
                    });
            if (builder!=null){
                builder.show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCODE){
            if (data!=null){
                Bundle bundle = data.getExtras();
                if (bundle==null){
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    DebugUtil.error("二维码解析结果:" + result);
                    deviceManagerViewModel.bind(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    DebugUtil.error("二维码解析失败");
                    ToastUtil.showToast("二维码解析失败");
                }
            }
        }
    }
}
