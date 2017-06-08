package com.eqdd.imrong.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.imrong.R;
import com.eqdd.imrong.service.DemoContext;
import com.eqdd.library.base.Config;
import com.eqdd.library.utils.GreenDaoUtil;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;


/**
 * Created by Bob on 15/8/18.
 * 会话列表
 */
@Route(path = Config.IMRONG_CONVERSITION_LIST)
public class ConversationListActivity extends FragmentActivity {

    private TextView mTitle;
    private RelativeLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imrong_conversationlist);

        setActionBarTitle();

        isReconnect();
    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {

        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBarTitle() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mBack = (RelativeLayout) findViewById(R.id.back);

        mTitle.setText("会话列表");

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect() {

        Intent intent = getIntent();
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
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

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
