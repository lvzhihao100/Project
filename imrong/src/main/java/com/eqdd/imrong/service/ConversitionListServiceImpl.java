package com.eqdd.imrong.service;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.SelectBean;
import com.eqdd.library.service.ConversitionListService;
import com.eqdd.library.service.OnDataGet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.tools.CharacterParser;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by lvzhihao on 17-6-5.
 */
@Route(path = Config.IMRONG_SERVICE_CONVERSITION_LIST)
public class ConversitionListServiceImpl implements ConversitionListService {


    @Override
    public void init(Context context) {

    }

    @Override
    public void getConversitionListAsyn(OnDataGet<List<SelectBean>> onDataGet) {
        ArrayList<SelectBean> selectBeans = new ArrayList<>();

        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations == null) {
                    onDataGet.sendData(null);
                    return;
                }
                for (Conversation conversation : conversations) {
                    String portrait = null;
                    String title;
                    title = conversation.getConversationTitle();
                    if (TextUtils.isEmpty(title)) {
                        title = RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName()).getTitle(conversation.getTargetId());
                    }

                    portrait = conversation.getPortraitUrl();
                    if (TextUtils.isEmpty(portrait)) {
                        Uri groupUserInfo = RongContext.getInstance().getConversationTemplate(conversation.getConversationType().getName()).getPortraitUri(conversation.getTargetId());
                        portrait = groupUserInfo != null ? groupUserInfo.toString() : null;
                    }
                    title = conversation.getConversationType() == Conversation.ConversationType.GROUP ?
                            title : title + "(" + conversation.getTargetId() + ")";
                    SelectBean selectBean = new SelectBean();
                    selectBean.setHead(portrait);
                    selectBean.setContent(title);
                    selectBean.setId(conversation.getTargetId());
                    selectBean.setType(conversation.getConversationType() ==
                            Conversation.ConversationType.GROUP ? SelectBean.GROUP : SelectBean.PRIVITE);
                    String sortString = "#";
                    //汉字转换成拼音
                    String pinyin = CharacterParser.getInstance().getSelling(selectBean.getContent());

                    if (pinyin != null) {
                        if (pinyin.length() > 0) {
                            sortString = pinyin.substring(0, 1).toUpperCase();
                        }
                    }
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        selectBean.setLetter(sortString.toUpperCase());
                    } else {
                        selectBean.setLetter("#");
                    }
                    selectBeans.add(selectBean);
                }

                Collections.sort(selectBeans, PinyinComparator.getInstance());
                onDataGet.sendData(selectBeans);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public static class PinyinComparator implements Comparator<SelectBean> {


        public static PinyinComparator instance = null;

        public static PinyinComparator getInstance() {
            if (instance == null) {
                instance = new PinyinComparator();
            }
            return instance;
        }

        public int compare(SelectBean o1, SelectBean o2) {
            if (o1.getLetter().equals("@") || o2.getLetter().equals("#")) {
                return -1;
            } else if (o1.getLetter().equals("#") || o2.getLetter().equals("@")) {
                return 1;
            } else {
                return o1.getLetter().compareTo(o2.getLetter());
            }
        }

    }
}
