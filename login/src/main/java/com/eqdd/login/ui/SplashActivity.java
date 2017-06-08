package com.eqdd.login.ui;

import android.databinding.DataBindingUtil;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.base.BaseActivity;
import com.eqdd.common.utils.StatusBarUtil;
import com.eqdd.library.base.Config;
import com.eqdd.login.R;
import com.eqdd.login.SplashActivityCustom;

@Route(path = Config.LOGIN_SPLASH)
public class SplashActivity extends BaseActivity implements Animation.AnimationListener {
    private SplashActivityCustom dataBinding;


    @Override
    public void initBinding() {
        StatusBarUtil.setStatusBarDarkMode(this, true);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_splash);
    }

    @Override
    public void initData() {
    }

    @Override
    public void setView() {
        startAnim();

    }

    private void startAnim() {
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(1500);
        anim.setFillAfter(true);
        anim.setAnimationListener(this);
        dataBinding.splash.startAnimation(anim);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
// TODO: 添加跳转逻辑

        ARouter.getInstance().build(Config.LOGIN_GUIDE)
                .navigation();
        finish();


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }
}
