package com.wm.toec.microenv.bean;

import java.io.Serializable;

/**
 * Created by toec on 2018/7/5.
 */

public class FamilyMemberIndexBean implements Serializable {
    String familyName;
    String memberId;
    int healthIndex;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getHealthIndex() {
        return healthIndex;
    }

    public void setHealthIndex(int healthIndex) {
        this.healthIndex = healthIndex;
    }
}
