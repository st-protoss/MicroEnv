package com.wm.toec.microenv.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.bean.FamilyMemberBean;
import com.wm.toec.microenv.ui.device.ActivityLocation;
import com.wm.toec.microenv.ui.member.ActivityFamilyManager;
import com.wm.toec.microenv.ui.member.ActivityMemberDetail;
import com.wm.toec.microenv.viewmodel.member.FamilyMemberCommand;
import com.wm.toec.microenv.viewmodel.member.ImemberDelete;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toec on 2018/7/5.
 */

public class FamilyMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public final int EDIT = 2;
    private List<FamilyMemberBean> list;

    private ImemberDelete memberDelete;
    public FamilyMemberAdapter(Context mContext){
        this.mContext = mContext;
        list = new ArrayList<>();
    }

    public void setMemberDelete(ImemberDelete memberDelete) {
        this.memberDelete = memberDelete;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_family_member,parent,false);
        return new FamilyMemberViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FamilyMemberViewHolder){
            FamilyMemberViewHolder familyMemberViewHolder = (FamilyMemberViewHolder)holder;
            familyMemberViewHolder.bindItem(list.get(position),position);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<FamilyMemberBean> familyList){
        //因为不需要保留以前的列表项 所以清空即可
        this.list.clear();
        this.list.addAll(familyList);
    }
    public void setList(List<FamilyMemberBean> familyList){
        this.list.clear();
        this.list = familyList;
    }
    private class FamilyMemberViewHolder extends RecyclerView.ViewHolder{
        TextView tv_family_name;
        AppCompatCheckBox cb_check;
        AppCompatButton btn_delete;
        public FamilyMemberViewHolder(View itemView) {
            super(itemView);
            tv_family_name = itemView.findViewById(R.id.family_member_name);
            //cb_check = itemView.findViewById(R.id.family_member_check);
            btn_delete = itemView.findViewById(R.id.family_member_delete);
        }
        public void bindItem(FamilyMemberBean bean,int position){
            tv_family_name.setText(bean.getFamilyName());
            itemView.setOnClickListener(v->{
                Intent intent = new Intent(mContext, ActivityMemberDetail.class);
                intent.putExtra("TYPE",EDIT);
                intent.putExtra("BEAN",bean);
                mContext.startActivity(intent);
            });
            //点击删除按钮事件
            btn_delete.setOnClickListener(v->{
                memberDelete.deleteFamilyMember(bean,position);
            });
        }
    }
}
