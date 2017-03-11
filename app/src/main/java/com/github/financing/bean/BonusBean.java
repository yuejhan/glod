package com.github.financing.bean;

import java.io.Serializable;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/11
 * 描述：
 *******************************************/
public class BonusBean implements Serializable{
    private Integer bonusId;

    public Integer getBonusId() {
        return bonusId;
    }

    public void setBonusId(Integer bonusId) {
        this.bonusId = bonusId;
    }
}
