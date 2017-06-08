package com.eqdd.common.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.eqdd.databind.percent.WindowUtil;
import com.facebook.stetho.Stetho;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zxy.tiny.Tiny;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by lvzhihao on 17-5-31.
 */
@Route(path = "/commonservice/application")
public class ComonService implements IProvider{
    @Override
    public void init(Context context) {

    }
    public void initCommon(Context context){
        App.INSTANCE.registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
        WindowUtil.init(context);
        Tiny.getInstance().init(App.INSTANCE);
        ZXingLibrary.initDisplayOpinion(context);
        Stetho.initializeWithDefaults(context);
        ShortcutBadger.applyCount(context, 7);
    }

}
