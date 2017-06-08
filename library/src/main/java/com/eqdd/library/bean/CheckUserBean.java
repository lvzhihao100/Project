package com.eqdd.library.bean;

/**
 * Created by lvzhihao on 17-4-13.
 */

public class CheckUserBean {
    FriendApplyBean usersBean;
    boolean isCheck;

    public CheckUserBean(FriendApplyBean usersBean) {
        this.usersBean = usersBean;
        isCheck=false;
    }

    public FriendApplyBean getUsersBean() {
        return usersBean;
    }

    public void setUsersBean(FriendApplyBean usersBean) {
        this.usersBean = usersBean;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
