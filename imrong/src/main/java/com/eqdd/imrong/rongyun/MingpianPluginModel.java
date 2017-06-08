package com.eqdd.imrong.rongyun;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.imrong.R;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.SelectBean;
import com.eqdd.library.bean.StaffBean;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.library.ui.select.SelectActivity;

import java.util.ArrayList;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by lvzhihao on 17-4-30.
 */

public class MingpianPluginModel implements IPluginModule {
    Conversation.ConversationType conversationType;
    String targetId;
    private Context context;

    @Override
    public Drawable obtainDrawable(Context context) {
        //设置插件 Plugin 图标
        this.context = context;
        return ContextCompat.getDrawable(context, R.drawable.rc_ext_plugin_image_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        //设置插件 Plugin 展示文字
        return "名片";
    }

    @Override
    public void onClick(final Fragment currentFragment, RongExtension extension) {
        //示例获取 会话类型、targetId、Context,此处可根据产品需求自定义逻辑，如:开启新的 Activity 等。
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();
        Intent intent = new Intent(currentFragment.getActivity(), SelectActivity.class);
        intent.putExtra(Config.MAX_NUM, 1);
        extension.startActivityForPluginResult(intent, Config.SELECT, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Config.SELECT) {
            if (resultCode == Config.SUCCESS) {
                ArrayList<SelectBean> selectBeans = data.getParcelableArrayListExtra(Config.SELECTED_BEANS);
                for (SelectBean selectBean : selectBeans) {

                    if (selectBean.getType() == SelectBean.PRIVITE) {
                        new HttpPresneter.Builder()
                                .Url(HttpConfig.SELECT_STAFF)
                                .Params("uid", selectBean.getId())
                                .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                                    @Override
                                    public void onError(Throwable e) {
                                        System.out.println(e.toString());
                                    }

                                    @Override
                                    public void onNext(Object o) {

                                        BaseBean<StaffBean> baseBean = GsonUtils.changeGsonToBaseBean((String) o, StaffBean.class);
                                        Content content = null;
                                        if (baseBean.getItems() != null && baseBean.getItems().size() > 0) {
                                            content = new Content(selectBean.getContent(), selectBean.getHead() == null ? "0" : selectBean.getHead(),
                                                    baseBean.getItems().get(0).getPostname(), baseBean.getItems().get(0).getCareername(),
                                                    baseBean.getItems().get(0).getCname(), selectBean.getId(), baseBean.getItems().get(0).getCid());
                                        } else {
                                            content = new Content(selectBean.getContent(), selectBean.getHead() == null ? "0" : selectBean.getHead(),
                                                    "0", "0", "0", selectBean.getId(), "0");
                                        }
                                        MingpianMessage obtain = MingpianMessage.obtain(content);
                                        Message message = Message.obtain(targetId, conversationType, obtain);
                                        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
                                            @Override
                                            public void onAttached(Message message) {

                                            }

                                            @Override
                                            public void onSuccess(Message message) {
                                                System.out.println("发送成功");
                                            }

                                            @Override
                                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                                System.out.println(errorCode.toString());
                                            }

                                        });
                                    }
                                })
                                .build()
                                .postString();
                    }
                }
            }
        }
    }
}
