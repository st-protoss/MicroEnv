package com.wm.toec.microenv.viewmodel.member;

import com.wm.toec.microenv.bean.FamilyMemberBean;

import java.util.List;

/**
 * Created by wm on 2018/7/5.
 */

public interface FamilyMemberCommand {
    public void loadFamilyListSuccess(List<FamilyMemberBean> familyMemberBeanList);//又分为有列表 列表大小为0
    public void networkError();
    public void deleteSuccess();//删除成员成功后的回调
}
