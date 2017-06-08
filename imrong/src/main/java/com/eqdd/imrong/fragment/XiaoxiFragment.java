package com.eqdd.imrong.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.base.App;
import com.eqdd.common.base.BaseFragment;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.imrong.R;
import com.eqdd.imrong.XiaoxiFragmentCustom;
import com.eqdd.imrong.service.DemoContext;
import com.eqdd.library.base.Config;
import com.eqdd.library.utils.GreenDaoUtil;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by lvzhihao on 17-4-7.
 */
@Route(path = Config.IMRONG_FRAGMENT_XIAOXI)
public class XiaoxiFragment extends BaseFragment {
    XiaoxiFragmentCustom dataBinding;
    private Conversation.ConversationType[] mConversationsTypes;
    private boolean isDebug=false;
    private ConversationListFragment listFragment;

    @Override
    protected void setView() {

    }

    @Override
    protected void initData() {
        isReconnect();
    }

    @Override
    public ViewDataBinding initBinding(LayoutInflater inflater) {
        return dataBinding = DataBindingUtil.inflate(inflater, R.layout.imrong_fragment_xiaoxi, null, false);

    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {


        if (listFragment==null) {
            listFragment = new ConversationListFragment();
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.framelayout, listFragment);
            ft.show(listFragment);
            ft.commit();
        }
        listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri;
        if (isDebug) {
            uri = Uri.parse("rong://" + App.INSTANCE.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };

        } else {
            uri = Uri.parse("rong://" + App.INSTANCE.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "true")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM
            };
        }
        listFragment.setUri(uri);
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect() {

        Intent intent = getActivity().getIntent();
        String token = null;

        if (DemoContext.getInstance() != null) {

            token = SPUtil.getToken(GreenDaoUtil.getUser().getUname());
        }

        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment();
                }
            }
        }
        enterFragment();
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getActivity().getApplicationInfo().packageName.equals(getCurProcessName(App.INSTANCE))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}
