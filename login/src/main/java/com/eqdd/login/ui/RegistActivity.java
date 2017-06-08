package com.eqdd.login.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.login.R;
import com.eqdd.login.RegistActivityCustom;
import com.eqdd.login.utils.RxCountDown;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Route(path =Config.LOGIN_REGISTER)
public class RegistActivity extends CommonActivity {
    private RegistActivityCustom dataBinding;
    private int mode;
    private Intent regist;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_regist);
        mode = getIntent().getIntExtra(Config.MODE, -1);
        switch (mode) {
            case 0:
                initTopTitleBar(View.VISIBLE, "注册手机验证");
                break;
            case 1:
                initTopTitleBar(View.VISIBLE, "注册公司验证");
                break;
            case 2:
                initTopTitleBar(View.VISIBLE, "忘记密码验证");
                break;
        }
        init();
    }

    @Override
    public void initData() {

    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_yanzheng) {
            String phone = dataBinding.etPhone.getText().toString().trim();
            if (!TextUtils.isEmpty(phone)) {
                RxCountDown.countdown(60)
                        .doOnSubscribe(() ->
                                dataBinding.tvYanzheng.setEnabled(false)
                        )
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                dataBinding.tvYanzheng.setText("获取验证码");
                                dataBinding.tvYanzheng.setEnabled(true);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                dataBinding.tvYanzheng.setText("倒计时" + integer);
                            }
                        });
                SMSSDK.getVerificationCode("86", phone);// 获取短信
            } else {
                ToastUtil.showShort("输入手机号");
            }

        } else if (i == R.id.tv_regeist) {
            String telphone = dataBinding.etPhone.getText().toString().trim();
//                String yzm = et_yanzheng.getText().toString().trim();
//                if (!TextUtils.isEmpty(telphone)&& ValidatorUtil.isMobile(telphone)) {
//                    if (!TextUtils.isEmpty(yzm)) {
//                        SMSSDK.submitVerificationCode("+86", telphone, yzm);// 验证短信
//                    } else {
//                        UIHelper.ToastMessage(this, "输入验证码");
//                    }
//                } else {
//                    UIHelper.ToastMessage(this, "请输入有效的手机号");
//                }
            if (!TextUtils.isEmpty(telphone)) {
                Bundle bundle = new Bundle();
                bundle.putString("phone", telphone);
                switch (mode) {
                    case Config.MODE_REGISTER:
                        startActivity(RegistTwo.class, bundle);
                        finish();
                        break;
                    case Config.MODE_REGISTER_QIYE:
                        new HttpPresneter.Builder()
                                .Url(HttpConfig.IS_CAN_REGISTER_COM)
                                .Params("uid", telphone)
                                .showProgress(RegistActivity.this)
                                .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onNext(Object o) {
                                        BaseBean baseBean = GsonUtils.changeGsonToBean((String) o, BaseBean.class);
                                        ToastUtil.showShort(baseBean.getMsg());
                                        if (baseBean.getStatus() == 200) {
                                            ARouter.getInstance().build(Config.LOGIN_QIYE_REGISTER)
                                                    .with(bundle)
                                                    .navigation();
                                            finish();

                                        }
                                    }
                                })
                                .build()
                                .postString();
                        break;
                    case Config.MODE_PASS_FORGET:
                        ARouter.getInstance().build(Config.LOGIN_PHONE_PASSWORD_GET)
                                .with(bundle)
                                .navigation();
                        finish();


                        break;

                }
            } else {
                ToastUtil.showShort("输入手机号");
            }

        }
    }

    // 获取短信信息
    private void init() {
        try {
            EventHandler eventHandler = new EventHandler() {
                public void afterEvent(int event, int result, Object data) {
                    final Message msg = new Message();
                    msg.arg1 = event;
                    msg.arg2 = result;
                    msg.obj = data;
                    Observable.just(msg)
                            .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                            .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                            .subscribe(new Observer<Message>() {
                                @Override
                                public void onNext(Message msg) {
//
                                    handleMsg(msg);
                                }

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            });
                }
            };
            SMSSDK.registerEventHandler(eventHandler); // 注册短信回调
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean handleMsg(Message msg) {
        int event = msg.arg1;
        int result = msg.arg2;
        if (result == SMSSDK.RESULT_COMPLETE) {
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//验证码验证成功
                String phone = dataBinding.etPhone.getText().toString().trim();
                String enterClass=null;
                if (mode == 0) {
                    enterClass = Config.LOGIN_REGISTER_TWO;
                } else if (mode == 1) {
                    enterClass = Config.LOGIN_PHONE_PASSWORD_GET;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Config.LOGIN_PHONE, phone);
                ARouter.getInstance().build(enterClass)
                        .with(bundle)
                        .navigation();
                finish();
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                ToastUtil.showLong("获取验证码成功");
            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
            }
        } else {
            ToastUtil.showLong("获取验证码出错");
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
