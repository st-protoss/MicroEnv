package com.wm.toec.microenv.ui.wear;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.adapter.FamilyMemberAdapter;
import com.wm.toec.microenv.adapter.WearMemberAdapter;
import com.wm.toec.microenv.base.BaseActivity;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.databinding.ActivityWearManagerBinding;
import com.wm.toec.microenv.viewmodel.member.FamilyMemberViewModel;
import com.wm.toec.microenv.viewmodel.wear.WearManagerCommand;
import com.wm.toec.microenv.viewmodel.wear.WearManagerViewModel;

import java.util.List;

/**
 * Created by toec on 2018/7/9.
 */

public class ActivityWearManager extends BaseActivity<ActivityWearManagerBinding> implements WearManagerCommand{
    public WearMemberAdapter mAdapter;
    public WearManagerViewModel familyMemberViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_manager);
        setTitle("穿戴数据上传");
        familyMemberViewModel = new WearManagerViewModel(this);
        initList();
        loadFamilyList();
    }

    private void loadFamilyList() {
        familyMemberViewModel.loadFamilyList();
    }

    private void initList() {
        mBindView.wearRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WearMemberAdapter(this);
        mBindView.wearRv.setAdapter(mAdapter);
    }

    @Override
    public void loadFamilyListSuccess(List<FamilyMemberBean> familyMemberList) {
        if (familyMemberList.size()==0){
            //尚未添加任何家庭成员
            mBindView.wearRv.setVisibility(View.GONE);
            mBindView.familyNoMember.setVisibility(View.VISIBLE);
            mBindView.networkError.setVisibility(View.GONE);
        }else if (familyMemberList.size()>0){
            mBindView.wearRv.setVisibility(View.VISIBLE);
            mBindView.familyNoMember.setVisibility(View.GONE);
            mBindView.networkError.setVisibility(View.GONE);
            mAdapter.addAll(familyMemberList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void networkError() {
        mBindView.wearRv.setVisibility(View.GONE);
        mBindView.networkError.setVisibility(View.VISIBLE);
        AppCompatButton btn_reset = mBindView.networkError.findViewById(R.id.reset);
        btn_reset.setOnClickListener(v->{
            loadFamilyList();
        });
    }
}
