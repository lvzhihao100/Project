package com.eqdd.library.bean;

/**
 * Created by lvzhihao on 17-4-13.
 */

public class CheckFriendBean {
    FriendBean friendBean;
    boolean isCheck;

    public CheckFriendBean(FriendBean friendBean) {
        this.friendBean = friendBean;
        isCheck=false;
    }

    public FriendBean getFriendBean() {
        return friendBean;
    }

    public void setFriendBean(FriendBean friendBean) {
        this.friendBean = friendBean;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
