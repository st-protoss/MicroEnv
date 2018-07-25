package com.wm.toec.microenv.ui.member;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.DatePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.databinding.ActivityFamilyDetailBinding;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.util.ToastUtil;
import com.wm.toec.microenv.viewmodel.member.FamilyDetailCommand;
import com.wm.toec.microenv.viewmodel.member.MemberDetailViewModel;

import java.util.Calendar;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by toec on 2018/7/9.
 */

public class ActivityMemberDetail extends BaseActivity<ActivityFamilyDetailBinding> implements FamilyDetailCommand{
    FamilyMemberBean familyMemberBean;//家庭成员的bean
    public String familyName;
    public String weight;
    public String height;
    public String birthday;
    int intentType;

    private MemberDetailViewModel memberDetailViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_detail);
        handleIntent();
        memberDetailViewModel = new MemberDetailViewModel(this);
        registeRxBus();

    }

    /**
     * 处理intent
     */
    private void handleIntent() {
        Intent intent = getIntent();
        intentType = intent.getIntExtra("TYPE",1);
        if (intentType==2){
            familyMemberBean = (FamilyMemberBean) intent.getSerializableExtra("bean");
            if (familyMemberBean.getFamilyName()==null){
                familyName = "尚未填写";
            }else{
                familyName = familyMemberBean.getFamilyName();
            }
            if (familyMemberBean.getWeight()==null){
                weight = "0.0";
            }else{
                weight = familyMemberBean.getWeight();
            }
            if (familyMemberBean.getHeight()==null){
                height = "0.0";
            }else{
                height = familyMemberBean.getHeight();
            }
            if (familyMemberBean.getFamilyName()==null){
                birthday = "尚未选择";
            }else{
                birthday = familyMemberBean.getBirthday();
            }
            memberDetailViewModel.refreshBoard(familyName,weight,height,birthday);
            mBindView.familyBtnSync.setText("同步数据");
        }
        if (intentType ==1){
            familyName = "尚未填写";
            weight = "0.0";
            height = "0.0";
            birthday = "尚未选择";
            memberDetailViewModel.refreshBoard(familyName,weight,height,birthday);
            mBindView.familyBtnSync.setText("添加成员");
        }
    }
    /**
     * 编辑昵称
     */
    public void editName(View v) {
        //手动选择 弹框
        new MaterialDialog.Builder(this)
                .title("编辑家庭称呼")
                .content("请拟定该成员的家庭称呼")
                .input("","", false,((dialog, input) ->{} ))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String familyName = dialog.getInputEditText().toString();
                    memberDetailViewModel.refreshBoard(familyName,weight,height,birthday);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }
    /**
     * 编辑身高
     */
    public void editHeight(View v) {
        new MaterialDialog.Builder(this)
                .title("编辑身高")
                .content("请填写该成员的身高数值")
                .input("","", false,((dialog, input) ->{} ))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    memberDetailViewModel.refreshBoard(familyName,weight,str,birthday);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();

    }
    /**
     * 编辑体重
     */
    public void editWeight(View v) {
        new MaterialDialog.Builder(this)
                .title("编辑体重")
                .content("请填写该成员的体重数值")
                .input("","", false,((dialog, input) ->{} ))
                .positiveText("确认")
                .onPositive(((dialog, which) -> {
                    String str = dialog.getInputEditText().toString();
                    memberDetailViewModel.refreshBoard(familyName,str,height,birthday);
                    dialog.dismiss();
                }))
                .negativeText("取消")
                .onNegative(((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
    }
    /**
     * 编辑生日
     */
    public void editBirthday(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityMemberDetail.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (monthOfYear == 12) {
                    monthOfYear = 1;
                } else {
                    monthOfYear++;
                }
                //如果月份小于10要加上前缀0
                String month = String.valueOf(monthOfYear);
                if (monthOfYear < 10) {
                    month = "0" + month;
                }
                //如果日趋小于10要加上前缀0
                String day = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    day = "0" + day;
                }
                String birthday = year + "-" + month + "-" + day;
                memberDetailViewModel.refreshBoard(familyName,weight,height,birthday);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    /**
     * 同步信息/添加成员
     */
    public void sync(View v) {
        if (intentType ==1){
            //添加成员
            memberDetailViewModel.addMember(familyName,weight,height,birthday);
        }else if(intentType ==2){
            //编辑成员
            memberDetailViewModel.editMember(familyMemberBean.getMemberId(), familyName, weight, height, birthday);
        }
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
                            ActivityMemberDetail.this.finish();
                        }
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void addSuccess() {
        ToastUtil.showToast("添加成功！");
        this.finish();
    }

    @Override
    public void oprateFail() {
        ToastUtil.showToast("操作失败！请重试");
    }

    @Override
    public void editSuccess() {
        ToastUtil.showToast("编辑成功！");
        this.finish();
    }

    @Override
    public void networkError() {
        mBindView.familyDetailContent.setVisibility(View.GONE);
        mBindView.networkError.setVisibility(View.VISIBLE);
        AppCompatButton btn_reset = mBindView.networkError.findViewById(R.id.reset);
        btn_reset.setOnClickListener(v->{
            mBindView.familyDetailContent.setVisibility(View.VISIBLE);
            mBindView.networkError.setVisibility(View.GONE);
            handleIntent();
        });

    }
}
