package com.eqdd.imrong.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.FileUtil;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.common.utils.ShareUtil;
import com.eqdd.imrong.Constent;
import com.eqdd.imrong.R;
import com.eqdd.imrong.service.DemoContext;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.FriendBean;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.library.utils.GreenDaoUtil;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.rong.callkit.RongCallKit;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.manager.SendImageManager;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import rx.android.schedulers.AndroidSchedulers;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;


/**
 * Created by vzhihao on 2017/2/25.
 */
@Route(path = Config.IMRONG_CONVERSITION)
public class ConversationActivity extends FragmentActivity {

    private static final String[] longPress = new String[]{
            "转入记事本",
            "转入任务",
            "复制",
            "发送给朋友/同事",
            "收藏",
            "删除",
            "更多"
    };
    private static final String TITLE = "易企点";
    private TextView mTitle;
    private ImageView mBack;
    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGETID_TITLE = 3;

    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private TextView mTextView;
    private String title;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    mTitle.setText("对方正在输入...");
                    break;
                case SET_VOICE_TYPING_TITLE:
                    mTitle.setText("对方正在说话...");
                    break;
                case SET_TARGETID_TITLE:
                    mTitle.setText(title);
                    break;
            }
        }
    };
    private TextView ivRight;
    private String conversationTitle;
    private ConversationFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imrong_conversation);
        Intent intent = getIntent();
        setActionBar();

        getIntentDate(intent);

        isReconnect(intent);
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {

            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGETID_TITLE);
                    }
                }
            }
        });
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", userInfo);
                ARouter.getInstance().build(Config.TONGXUNLU_FRIEND_DETAIL_INFO)
                        .with(bundle)
                        .navigation();
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, io.rong.imlib.model.Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, io.rong.imlib.model.Message message) {

                showLongClickDialog(context, view, message);
                return true;
            }
        });
    }

    private void showLongClickDialog(Context context, View view, io.rong.imlib.model.Message message) {
//        TextMessageItemProvider.ViewHolder holder = (TextMessageItemProvider.ViewHolder)view.getTag();
//        holder.longClick = true;
        if (message.getContent() instanceof TextMessage) {
            if (view instanceof TextView) {
                CharSequence items = ((TextView) view).getText();
                if (items != null && items instanceof Spannable) {
                    Selection.removeSelection((Spannable) items);
                }
            }

            long deltaTime = RongIM.getInstance().getDeltaTime();
            long normalTime = System.currentTimeMillis() - deltaTime;
            boolean enableMessageRecall = false;
            int messageRecallInterval = -1;
            boolean hasSent = !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENDING) && !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.FAILED);

            try {
                enableMessageRecall = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_message_recall);
                messageRecallInterval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_message_recall_interval);
            } catch (Resources.NotFoundException var15) {
                RLog.e("TextMessageItemProvider", "rc_message_recall_interval not configure in rc_config.xml");
                var15.printStackTrace();
            }

            String[] items1;
            if (hasSent && enableMessageRecall && normalTime - message.getSentTime() <= (long) (messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && !message.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.SYSTEM) && !message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
                items1 = new String[]{"转入记事本", "转入任务", "复制", "发送给朋友/同事", "收藏", "删除", "更多", "撤回消息"};
            } else {
                items1 = new String[]{"转入记事本", "转入任务", "复制", "发送给朋友/同事", "收藏", "删除", "更多"};
            }

            OptionsPopupDialog.newInstance(view.getContext(), items1).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    switch (which) {
                        case 0:
                        case 1:
                            break;
                        case 2:
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(((TextMessage) message.getContent()).getContent());
                            break;
                        case 3:
                            Constent.messageContent = message.getContent();
                            ShareUtil.shareMsgText(ConversationActivity.this, "activitytitle", "msgtitle",
                                    ((TextMessage) message.getContent()).getContent());
                            break;
                        case 4:
                            break;
                        case 5:
                            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
                            break;
                        case 6:
                            break;
                        case 7:
                            RongIM.getInstance().recallMessage(message, getPushContent(view.getContext(), message));

                    }
                }
            }).show();
        } else if (message.getContent() instanceof ImageMessage) {//图片
            long deltaTime = RongIM.getInstance().getDeltaTime();
            long normalTime = System.currentTimeMillis() - deltaTime;
            boolean enableMessageRecall = false;
            int messageRecallInterval = -1;
            boolean hasSent = !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENDING) && !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.FAILED);

            try {
                enableMessageRecall = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_message_recall);
                messageRecallInterval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_message_recall_interval);
            } catch (Resources.NotFoundException var14) {
                RLog.e("ImageMessageItemProvider", "rc_message_recall_interval not configure in rc_config.xml");
                var14.printStackTrace();
            }

            String[] items;
            if (hasSent && enableMessageRecall && normalTime - message.getSentTime() <= (long) (messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && !message.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.SYSTEM) && !message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
                items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多", "撤回消息"};
            } else {
                items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多"};
            }

            OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    switch (which) {
                        case 0:
                        case 1:
                            break;
                        case 2:
                            Constent.messageContent = message.getContent();
                            ShareUtil.shareMsgText(ConversationActivity.this, "activitytitle", "msgtitle"
                                    , ((ImageMessage) message.getContent()).getRemoteUri().toString());
                            break;
                        case 3:
                            break;
                        case 4:
                            SendImageManager.getInstance().cancelSendingImage(message.getConversationType(), message.getTargetId(), message.getMessageId());
                            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
                            break;
                        case 5:
                            break;
                        case 6:
                            RongIM.getInstance().recallMessage(message, getPushContent(view.getContext(), message));
                            break;
                    }
                }
            }).show();
        } else if (message.getContent() instanceof FileMessage) {//文件
            long deltaTime = RongIM.getInstance().getDeltaTime();
            long normalTime = System.currentTimeMillis() - deltaTime;
            boolean enableMessageRecall = false;
            int messageRecallInterval = -1;

            try {
                enableMessageRecall = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_message_recall);
                messageRecallInterval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_message_recall_interval);
            } catch (Resources.NotFoundException var13) {
                RLog.e("FileMessageItemProvider", "rc_message_recall_interval not configure in rc_config.xml");
                var13.printStackTrace();
            }

//            if (!message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENDING) || ((FileMessage)(message.getContent())).getProgress() >= 100) {
            if (!message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENDING)) {
                String[] items;
                if (message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENT) && enableMessageRecall && normalTime - message.getSentTime() <= (long) (messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && !message.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.SYSTEM) && !message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
                    items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多", "撤回消息"};
                } else {
                    items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多"};
                }

                OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                    public void onOptionsItemClicked(int which) {
                        switch (which) {
                            case 0:
                            case 1:
                                break;
                            case 2:
                                Constent.messageContent = message.getContent();
                                ShareUtil.shareMsgText(ConversationActivity.this, "activitytitle", "msgtitle"
                                        , ((FileMessage) message.getContent()).getFileUrl().toString());
                                break;
                            case 3:
                                break;
                            case 4:
                                RongIM.getInstance().cancelSendMediaMessage(message, (RongIMClient.OperationCallback) null);
                                RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
                                break;
                            case 5:
                                break;
                            case 6:
                                RongIM.getInstance().recallMessage(message, getPushContent(view.getContext(), message));
                                break;
                        }


                    }
                }).show();
            }
        } else if (message.getContent() instanceof VoiceMessage) {//语音
            long deltaTime = RongIM.getInstance().getDeltaTime();
            long normalTime = System.currentTimeMillis() - deltaTime;
            boolean enableMessageRecall = false;
            int messageRecallInterval = -1;
            boolean hasSent = !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENDING) && !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.FAILED);
            try {
                enableMessageRecall = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_message_recall);
                messageRecallInterval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_message_recall_interval);
            } catch (Resources.NotFoundException var14) {
                RLog.e("VoiceMessageItemProvider", "rc_message_recall_interval not configure in rc_config.xml");
                var14.printStackTrace();
            }

            String[] items;
            if (hasSent && enableMessageRecall && normalTime - message.getSentTime() <= (long) (messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && !message.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.SYSTEM) && !message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
                items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多", "撤回消息"};
            } else {
                items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多"};
            }

            OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    switch (which) {
                        case 0:
                        case 1:
                            break;
                        case 2:
                            Constent.messageContent = message.getContent();
                            byte[] encode = ((VoiceMessage) message.getContent()).encode();
                            ShareUtil.shareMsgVoice(ConversationActivity.this, TITLE, "", FileUtil.byte2File(encode, ".wav"));
                            break;
                        case 3:
                            break;
                        case 4:
                            AudioPlayManager.getInstance().stopPlay();
                            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
                            break;
                        case 5:
                            break;
                        case 6:
                            RongIM.getInstance().recallMessage(message, getPushContent(view.getContext(), message));
                            break;
                    }

                }
            }).show();
        } else if (message.getContent() instanceof LocationMessage) {//地图
            long deltaTime = RongIM.getInstance().getDeltaTime();
            long normalTime = System.currentTimeMillis() - deltaTime;
            boolean enableMessageRecall = false;
            int messageRecallInterval = -1;
            boolean hasSent = !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.SENDING) && !message.getSentStatus().equals(io.rong.imlib.model.Message.SentStatus.FAILED);

            try {
                enableMessageRecall = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_message_recall);
                messageRecallInterval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_message_recall_interval);
            } catch (Resources.NotFoundException var14) {
                RLog.e("LocationMessageItemProvider", "rc_message_recall_interval not configure in rc_config.xml");
                var14.printStackTrace();
            }

            String[] items;
            if (hasSent && enableMessageRecall && normalTime - message.getSentTime() <= (long) (messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && !message.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) && !message.getConversationType().equals(Conversation.ConversationType.SYSTEM) && !message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
                items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多", "撤回消息"};
            } else {
                items = new String[]{"转入记事本", "转入任务", "发送给朋友/同事", "收藏", "删除", "更多"};
            }


            OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    switch (which) {
                        case 0:
                        case 1:
                            break;
                        case 2:
                            Constent.messageContent = message.getContent();
                            ARouter.getInstance().build("/imrong/shareEnter")
                                    .navigation();
                            break;
                        case 3:
                            break;
                        case 4:
                            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback) null);
                            break;
                        case 5:
                            break;
                        case 6:
                            RongIM.getInstance().recallMessage(message, getPushContent(view.getContext(), message));
                    }
                }
            }).show();
        }
    }

    public String getPushContent(Context context, io.rong.imlib.model.Message message) {
        String userName = "";
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
        if (userInfo != null) {
            userName = userInfo.getName();
        }

        return context.getString(io.rong.imkit.R.string.rc_user_recalled_message, new Object[]{userName});
    }


    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        conversationTitle = intent.getData().getQueryParameter("conversationTitle");
        if (conversationTitle != null) {
            System.out.println(conversationTitle + "conversationTitle");
        }
//        RongIMClient.getInstance().getConversation();
        System.out.println("mTargetId" + mTargetId + "##");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        title = getIntent().getData().getQueryParameter("title");
        enterFragment(mConversationType, mTargetId);
        setActionBarTitle(mTargetId);
        if (mConversationType == Conversation.ConversationType.GROUP) {
            System.out.println("当前对话为群组");
            ivRight.setVisibility(View.VISIBLE);
            RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
                @Override
                public ArrayList<String> getMemberList(String groupId, RongCallKit.OnGroupMembersResult result) {
                    new HttpPresneter.Builder()
                            .Url(HttpConfig.GROUP_USER_LIST)
                            .Params("groupid", mTargetId)
                            .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                                @Override
                                public void onError(Throwable e) {
                                    System.out.println(e.toString());
                                }

                                @Override
                                public void onNext(Object o) {
                                    BaseBean<FriendBean> friendBeanBaseBean = GsonUtils.changeGsonToBaseBean((String) o, FriendBean.class);
                                    ArrayList<String> userInfos = new ArrayList<>();
                                    if (friendBeanBaseBean.getItems() != null) {
                                        for (FriendBean friendBean : friendBeanBaseBean.getItems()) {
                                            UserInfo userInfo = new UserInfo(friendBean.getUname(), friendBean.getPname(), Uri.parse(friendBean.getIphoto()));
                                            userInfos.add(userInfo.getUserId());
                                        }
                                        result.onGotMemberList(userInfos);
                                    }
                                }
                            })
                            .build()
                            .postString();
                    return null;
                }
            });
            RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
                @Override
                public void getGroupMembers(String s, RongIM.IGroupMemberCallback iGroupMemberCallback) {
                    new HttpPresneter.Builder()
                            .Url(HttpConfig.GROUP_USER_LIST)
                            .Params("groupid", mTargetId)
                            .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                                @Override
                                public void onError(Throwable e) {
                                    System.out.println(e.toString());
                                }

                                @Override
                                public void onNext(Object o) {
                                    BaseBean<FriendBean> friendBeanBaseBean = GsonUtils.changeGsonToBaseBean((String) o, FriendBean.class);
                                    ArrayList<UserInfo> userInfos = new ArrayList<>();
                                    if (friendBeanBaseBean.getItems() != null) {
                                        for (FriendBean friendBean : friendBeanBaseBean.getItems()) {
                                            UserInfo userInfo = new UserInfo(friendBean.getUname(), friendBean.getPname(), Uri.parse(friendBean.getIphoto()));
                                            userInfos.add(userInfo);
                                        }
                                        iGroupMemberCallback.onGetGroupMembersResult(userInfos);
                                    }
                                }
                            })
                            .build()
                            .postString();
                }
            });
        }

    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


        String token = null;

        if (DemoContext.getInstance() != null) {
            token = SPUtil.getToken(GreenDaoUtil.getUser().getUname());
        }

        //push或通知过来
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
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }

    /**
     * 设置 actionbar 事件
     */
    private void setActionBar() {

        mTitle = (TextView) findViewById(R.id.txt1);
        ivRight = (TextView) findViewById(R.id.img3);
        mBack = (ImageView) findViewById(R.id.img1);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RxView.clicks(ivRight)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((i) -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("groupid", mTargetId);
                    bundle.putString("groupname", title);
                    ARouter.getInstance().build(Config.TONGXUNLU_TEAM_CHEAT_INFO)
                            .with(bundle)
                            .navigation();
                });
    }


    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {

        mTitle.setText(title);
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

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}
