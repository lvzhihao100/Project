package com.eqdd.yiqidian;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.create.library.ActionSheetDialog;
import com.create.library.ChooseSheetUtil;
import com.eqdd.common.base.App;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.imrong.fragment.XiaoxiFragment;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.User;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.library.utils.GreenDaoUtil;
import com.roughike.bottombar.BottomBarTab;
import com.tbruyelle.rxpermissions.RxPermissions;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import me.leolin.shortcutbadger.ShortcutBadger;

@Route(path = Config.APP_MAIN)
public class MainActivity extends CommonActivity implements RongIM.UserInfoProvider, IUnReadMessageObserver {


    private static final int REQUEST_CODE = 1;
    private static final String QUNZU = "1";
    private static final String HAOYOU = "2";
    private MainActivityCustom dataBinding;
    private User user;
    private String result;
    private String fragment1Tag = "消息";
    private String fragment2Tag = "通讯录";
    private String fragment3Tag = "功能";
    private String fragment4Tag = "发现";
    private String fragment5Tag = "我的";
    private BottomBarTab nearby;
    private ActionSheetDialog bottomChoose;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.app_activity_main);
    }

    @Override
    public void initData() {
        user = GreenDaoUtil.getUser();
        initRong();
        initBottomBar();
        initChooseDialog();
    }

    @Override
    public void setView() {

    }

    private void initBottomBar() {
        nearby = dataBinding.bottomBar.getTabWithId(R.id.tab_xiaoxi);
        dataBinding.bottomBar.setDefaultTab(R.id.tab_function);
        dataBinding.bottomBar.setOnTabSelectListener((tabId) -> onChecked(tabId));
    }

    public void onChecked(int checkedId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment1 = fm.findFragmentByTag(fragment1Tag);
        Fragment fragment2 = fm.findFragmentByTag(fragment2Tag);
        Fragment fragment3 = fm.findFragmentByTag(fragment3Tag);
        Fragment fragment4 = fm.findFragmentByTag(fragment4Tag);
        Fragment fragment5 = fm.findFragmentByTag(fragment5Tag);
        if (fragment1 != null) {
            ft.hide(fragment1);
        }
        if (fragment2 != null) {
            ft.hide(fragment2);
        }
        if (fragment3 != null) {
            ft.hide(fragment3);
        }
        if (fragment4 != null) {
            ft.hide(fragment4);
        }
        if (fragment5 != null) {
            ft.hide(fragment5);
        }
        switch (checkedId) {
            case R.id.tab_xiaoxi:
                if (fragment1 == null) {
                    fragment1 = new XiaoxiFragment();
                    ft.add(R.id.contentContainer, fragment1, fragment1Tag);
                } else {
                    ft.show(fragment1);
                }
                ((TextView) findViewById(R.id.tv_title)).setText("消息");
                break;
            case R.id.tab_tongxunlu:
                if (fragment2 == null) {
                    fragment2 = new XiaoxiFragment();
                    ft.add(R.id.contentContainer, fragment2, fragment2Tag);
                } else {
                    ft.show(fragment2);
                }
                ((TextView) findViewById(R.id.tv_title)).setText("通讯录");


                break;
            case R.id.tab_function:
                if (fragment3 == null) {
                    fragment3 = new XiaoxiFragment();
                    ft.add(R.id.contentContainer, fragment3, fragment3Tag);
                } else {
                    ft.show(fragment3);
                }
                ((TextView) findViewById(R.id.tv_title)).setText("易企点工业互联网平台");


                break;
            case R.id.tab_faxian:
                if (fragment4 == null) {
                    fragment4 = new XiaoxiFragment();
                    ft.add(R.id.contentContainer, fragment4, fragment4Tag);
                } else {
                    ft.show(fragment4);
                }
                ((TextView) findViewById(R.id.tv_title)).setText("发现");


                break;
            case R.id.tab_my:
                if (fragment5 == null) {
                    fragment5 = new XiaoxiFragment();
                    ft.add(R.id.contentContainer, fragment5, fragment5Tag);
                } else {
                    ft.show(fragment5);
                }
                ((TextView) findViewById(R.id.tv_title)).setText("我的");
                break;
            default:
                break;
        }
        ft.commit();
    }

    private void initRong() {
        RongIM.setUserInfoProvider(this, true);
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };
        System.out.println("更新融云数据");
        RongIM.getInstance().setMessageAttachedUserInfo(true);

        /**
         * 刷新用户缓存数据。
         *
         * @param userInfo 需要更新的用户缓存数据。
         */
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(user.getUname(), user.getPname(), Uri.parse(HttpConfig.BASE_URL_NO + user.getIphoto())));
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUname(), user.getPname(), Uri.parse(HttpConfig.BASE_URL_NO + user.getIphoto())));
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
    }


    private void initChooseDialog() {
        bottomChoose = ChooseSheetUtil.getBottomChoose(this, null, new String[]{"扫一扫"}, (o, position) -> {


            RxPermissions.getInstance(this).request(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE
                    , android.Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        // 当R.id.enableCamera被点击的时候触发获取权限
                        if (granted) {
                            System.out.println("已授权");
                            ARouter.getInstance().build(Config.APP_SCAN).navigation(MainActivity.this,REQUEST_CODE);
                        }else {
                            ToastUtil.showLong("请在设置中开启照相权限");
                        }
                    });
//            new IntentIntegrator(this).initiateScan();
        });
    }
    @Override
    public UserInfo getUserInfo(String s) {
        return new UserInfo(user.getUname(), user.getPname(),
                Uri.parse(HttpConfig.BASE_URL_NO+user.getIphoto()));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
    }

    @Override
    public void onCountChanged(int i) {
        System.out.println("未读消息树" + i);
        if (i <= 0) {
            nearby.removeBadge();
            ShortcutBadger.removeCount(App.INSTANCE);
        } else {
            nearby.setBadgeCount(i);
            ShortcutBadger.applyCount(App.INSTANCE, i);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                bottomChoose.show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
    }
}

