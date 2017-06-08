package com.eqdd.library.bean;


import com.eqdd.library.bean.rongyun.GroupBean;

/**
 * Created by lvzhihao on 17-4-13.
 */

public class CheckGroupBean {
    GroupBean groupBean;
    boolean isCheck;

    public CheckGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;
        isCheck=false;
    }

    public GroupBean getGroupBean() {
        return groupBean;
    }

    public void setGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
