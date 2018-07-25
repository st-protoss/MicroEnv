package com.wm.toec.microenv.ui.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wm.toec.microenv.R;
import com.wm.toec.microenv.adapter.FamilyMemberAdapter;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.databinding.ActivityFamilyManagerBinding;
import com.wm.toec.microenv.eventbus.BaseMessage;
import com.wm.toec.microenv.eventbus.Rxbus;
import com.wm.toec.microenv.ui.device.ActivityLocation;
import com.wm.toec.microenv.viewmodel.member.FamilyMemberCommand;
import com.wm.toec.microenv.viewmodel.member.FamilyMemberViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wm on 2018/7/5.
 */

public class ActivityFamilyManager extends BaseActivity<ActivityFamilyManagerBinding> implements FamilyMemberCommand {
    public final int ADD_MEMBER = 1;

    public FamilyMemberAdapter mAdapter;
    public FamilyMemberViewModel familyMemberViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_manager);
        setTitle("家庭成员管理");
        familyMemberViewModel = new FamilyMemberViewModel(this);
        familyMemberViewModel.setFamilyMemberCommand(this);
        initList();
        loadFamilyList();
        registeRxBus();


    }
    /**
     * 注册事件总线
     */
    private void registeRxBus() {
        Disposable disposable = Rxbus.getInstance().toObservable(BaseMessage.class)
                .subscribe(new Consumer<BaseMessage>() {
                    @Override
                    public void accept(BaseMessage baseMessage) throws Exception {
                        if (baseMessage.getmCode()==3){
                            //刷新家庭成员列表
                            loadFamilyList();
                        }
                        if (baseMessage.getmCode()==4){
                            //退出该app
                            ActivityFamilyManager.this.finish();
                        }
                    }
                });
        addDisposable(disposable);
    }

    /**
     * 初始化recylcerview
     */
    private void initList() {
        mBindView.familyRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FamilyMemberAdapter(this);
        mBindView.familyRv.setAdapter(mAdapter);
        mAdapter.setMemberDelete((bean, position) ->{
            //弹出弹框 确认是否解绑设备
            new MaterialDialog.Builder(this)
                    .title("删除成员")
                    .content("确认删除称呼为"+bean.getFamilyName()+"的成员?+\n一旦删除将不可撤销")
                    .positiveText("确认删除")
                    .negativeText("取消")
                    .onPositive((dialog, which) -> {
                        familyMemberViewModel.deleteMember(bean);
                        dialog.dismiss();
                    })
                    .onNegative((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    /**
     * 添加家庭成员
     */
    public void addFamilyMember(View v) {
        Intent intent = new Intent(ActivityFamilyManager.this, ActivityLocation.class);
        intent.putExtra("TYPE",ADD_MEMBER);
        startActivity(intent);
    }
    /**
     * 加载当前账号下的家庭成员列表
     */
    private void loadFamilyList(){
        familyMemberViewModel.loadFamilyList();
    }

    /**
     * 加载家庭成员列表成功回调
     */
    @Override
    public void loadFamilyListSuccess(List<FamilyMemberBean> familyMemberList) {
        if (familyMemberList.size()==0){
            //尚未添加任何家庭成员
            mBindView.familyRv.setVisibility(View.GONE);
            mBindView.familyNoMember.setVisibility(View.VISIBLE);
            mBindView.networkError.setVisibility(View.GONE);
        }else if (familyMemberList.size()>0){
            mBindView.familyRv.setVisibility(View.VISIBLE);
            mBindView.familyNoMember.setVisibility(View.GONE);
            mBindView.networkError.setVisibility(View.GONE);
            mAdapter.addAll(familyMemberList);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 网络错误回调
     */
    @Override
    public void networkError() {
        mBindView.familyRv.setVisibility(View.GONE);
        mBindView.networkError.setVisibility(View.VISIBLE);
        AppCompatButton btn_reset = mBindView.networkError.findViewById(R.id.reset);
        btn_reset.setOnClickListener(v->{
            loadFamilyList();
        });
    }
    /**
     * 删除某一家庭成员成功
     */
    @Override
    public void deleteSuccess() {
        //删除成功后刷新列表
        loadFamilyList();
    }


}
