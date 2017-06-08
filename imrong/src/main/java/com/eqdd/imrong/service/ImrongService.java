package com.eqdd.imrong.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.eqdd.common.base.App;
import com.eqdd.imrong.listener.ConnectionStatusListener;
import com.eqdd.imrong.listener.ReceiveMessageListener;
import com.eqdd.imrong.rongyun.MingpianMessage;
import com.eqdd.imrong.rongyun.MingpianMessageItemProvider;
import com.eqdd.imrong.rongyun.MyExtensionModel;
import com.eqdd.library.bean.rongyun.GroupBean;
import com.eqdd.library.utils.GreenDaoUtil;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 * Created by lvzhihao on 17-6-1.
 */
@Route(path = "/imrongservice/application")
public class ImrongService implements IProvider {
    @Override
    public void init(Context context) {

    }
    public void initImrong(Context context){
        initRongIm(context);
        RongIM.setConnectionStatusListener(new ConnectionStatusListener());
        RongIM.setOnReceiveMessageListener(new ReceiveMessageListener());
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
        RongIM.setGroupInfoProvider( s-> {
                System.out.println("融云服务器来获取group数据了" + s);
                GroupBean groupById = GreenDaoUtil.getGroupById(s);
                return groupById == null ? null : new Group(groupById.getGroupid(), groupById.getGroupname(), Uri.parse("0"));
            }
        , true);
        List<IExtensionModule> extensionModules = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultExtensionModule = null;
        if (extensionModules != null) {
            for (IExtensionModule extensionModule : extensionModules) {
                if (extensionModule instanceof DefaultExtensionModule) {
                    defaultExtensionModule = extensionModule;
                    break;
                }
            }
            if (defaultExtensionModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultExtensionModule);
            }
            RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModel());
        }
        RongIM.registerMessageType(MingpianMessage.class);
        RongIM.getInstance().registerMessageTemplate(new MingpianMessageItemProvider());
    }

    private void initRongIm(Context context) {
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (App.INSTANCE.getApplicationInfo().packageName.equals(getCurProcessName(App.INSTANCE.getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(context);
            RongIMClient.init(context);

            if (App.INSTANCE.getApplicationInfo().packageName.equals(getCurProcessName(App.INSTANCE.getApplicationContext()))) {

                DemoContext.init(context);
            }
        }
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
