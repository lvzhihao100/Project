package com.eqdd.imrong.bean;


import io.rong.imlib.model.Conversation;

/**
 * Created by lvzhihao on 17-4-13.
 */

public class CheckConversationBean {
    Conversation conversation;
    boolean isCheck;

    public CheckConversationBean(Conversation conversation) {
        this.conversation = conversation;
        this.isCheck = false;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
