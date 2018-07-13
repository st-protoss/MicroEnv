package com.wm.toec.microenv.viewmodel.wear;

import com.wm.toec.microenv.bean.FamilyMemberBean;

import java.util.List;

/**
 * Created by toec on 2018/7/9.
 */

public interface WearManagerCommand {
    public void loadFamilyListSuccess(List<FamilyMemberBean> familyMemberBeanList);//又分为有列表 列表大小为0
    public void networkError();
}
