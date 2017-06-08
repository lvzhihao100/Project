package com.eqdd.login.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.base.App;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.User;
import com.eqdd.library.service.JPushService;
import com.eqdd.library.service.UserService;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by lvzhihao on 17-5-30.
 */
@Route(path = Config.LOGIN_SERVICE_JPUSH)
public class JPushServiceImpl implements JPushService {
    @Autowired
    UserService userService;
    private User user;
    private Subscription subscribe;

    @Override
    public void setJPushAlias() {
        ARouter.getInstance().inject(this);
        user = userService.getUser();
        subscribe = Observable.interval(0,1, TimeUnit.MINUTES)
                .observeOn(Schedulers.newThread())
                .subscribe((i) -> {
                    System.out.println("设置极光别名中。。。");
                    JPushInterface.setAliasAndTags(App.INSTANCE, user.getUname(),
                            null,
                            mAliasCallback);
                });
    }

    @Override
    public void init(Context context) {

    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0://设置别名成功
                    System.out.println("设置别名成功");
                    SPUtil.saveJPushAlias(user.getUname());
                    subscribe.unsubscribe();
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002://设置别名超时
                    // 延迟 60 秒来调用 Handler 设置别名
                    System.out.println("设置别名超时");
                    break;
                default://错误
                    System.out.println("设置别名错误码" + code);
                    subscribe.unsubscribe();
                    break;
            }
        }
    };
}
