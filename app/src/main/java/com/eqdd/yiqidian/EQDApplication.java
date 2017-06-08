package com.eqdd.yiqidian;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.base.App;
import com.eqdd.common.base.ComonService;
import com.eqdd.imrong.service.ImrongService;
import com.eqdd.library.service.LibraryService;
import com.eqdd.login.service.LoginService;

/**
 * Created by lvzhihao on 17-5-31.
 */

public class EQDApplication extends Application {
    @Autowired
    LoginService loginService;
    @Autowired
    LibraryService libraryService;
    @Autowired
    ComonService comonService;
    @Autowired
    ImrongService imrongService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isApkInDebug(this)) {
            System.out.println("debug");
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(App.INSTANCE); // 尽可能早，推荐在Application中初始化
        ARouter.getInstance().inject(this);
        loginService.initLogin(this);
        libraryService.initLibrary(this);
        comonService.initCommon(this);
        imrongService.initImrong(this);
    }

    public boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
