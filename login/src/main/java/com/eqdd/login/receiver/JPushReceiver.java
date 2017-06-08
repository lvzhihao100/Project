package com.eqdd.login.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lvzhihao on 17-5-31.
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            System.out.println("JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("接受到推送下来的自定义消息");
            String string = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            System.out.println(string);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("接受到推送下来的通知");
            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println( "用户点击打开了通知");

            openNotification(context,bundle);

        } else {
            System.out.println("Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        System.out.println( " title : " + title);
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        System.out.println( "alert : " + alert);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        System.out.println( "extras : " + extras);
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        System.out.println( "message : " + message);
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            System.out.println("Unexpected: extras is not a valid json");
            return;
        }
//        if (TYPE_THIS.equals(myValue)) {
//            Intent mIntent = new Intent(context, ThisActivity.class);
//            mIntent.putExtras(bundle);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mIntent);
//        } else if (TYPE_ANOTHER.equals(myValue)){
//            Intent mIntent = new Intent(context, AnotherActivity.class);
//            mIntent.putExtras(bundle);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mIntent);
//        }
    }
}
