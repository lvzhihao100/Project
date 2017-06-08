package com.eqdd.login.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.eqdd.library.base.Config;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

/**
 * Created by lvzhihao on 17-5-31.
 */
@Route(path = Config.LOGIN_SERVICE_APPLICATION)
public class LoginService implements IProvider {
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "1b8496b059b30";
    private static String APPSECRET = "daa0b3099e8bfd736af50955e79da286";
    @Override
    public void init(Context context) {

    }
    public void initLogin(Context context){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
        SMSSDK.initSDK(context,APPKEY,APPSECRET,false);
    }
}
