package com.eqdd.login.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.create.library.ActionSheetDialog;
import com.create.library.ChooseSheetUtil;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.EncryptUtil;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.common.utils.StatusBarUtil;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.User;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.library.service.JPushService;
import com.eqdd.library.service.RongyunService;
import com.eqdd.library.service.UserService;
import com.eqdd.login.LoginActivityCustom;
import com.eqdd.login.R;
import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.StaticScheme;
import com.github.yoojia.inputs.ValueScheme;
import com.github.yoojia.inputs.WidgetAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

@Route(path = Config.LOGIN_LOGIN)
public class LoginActivity extends CommonActivity {
    private LoginActivityCustom dataBinding;

    private AndroidNextInputs inputs;
    private WidgetAccess access;
    private ActionSheetDialog bottomChoose;
    private ActionSheetDialog bottomPassChoose;
    @Autowired
    UserService userService;
    @Autowired
    JPushService jPushService;
    @Autowired
    RongyunService rongyunService;

    @Override
    public void initBinding() {
        StatusBarUtil.setStatusBarDarkMode(this, true);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_login);
        ARouter.getInstance().inject(LoginActivity.this);
    }

    @Override
    public void initData() {
        if (user != null) {
            dataBinding.etUsername.setText(user.getUname());
            if (user.getAuthen() != 0) {
//                enterMainActivity();
            }
        }
        inputs = new AndroidNextInputs();
        access = new WidgetAccess(this);
        inputs  // 必选，手机号
                .add(access.findEditText(R.id.et_username))
                .with(StaticScheme.Required(), StaticScheme.ChineseMobile())
                // 必选 ，密码8~18
                .add(access.findEditText(R.id.et_password))
                .with(StaticScheme.Required(), ValueScheme.RangeLength(8, 18));
    }

    @Override
    public void setView() {

        dataBinding.etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginRequest();
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_login) {
            loginRequest();

        } else if (i == R.id.tv_register) {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.MODE, Config.MODE_REGISTER);

            ARouter.getInstance().build(Uri.parse(Config.APP_BASE_URL + Config.LOGIN_REGISTER))
                    .with(bundle).navigation();

        } else if (i == R.id.tv_forget_pass) {
            showPassChoose();

        } else if (i == R.id.tv_register_qiye) {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.MODE, Config.MODE_REGISTER_QIYE);

            ARouter.getInstance().build(Uri.parse(Config.APP_BASE_URL + Config.LOGIN_REGISTER_QIYE))
                    .with(bundle).navigation();
        }
    }

    private void loginRequest() {
        String username = dataBinding.etUsername.getText().toString().trim();
        String pass = dataBinding.etPassword.getText().toString().trim();
        if (inputs.test()) {
            sendRequest(username, pass);
        }
    }

    private void showPassChoose() {
        if (bottomPassChoose == null) {
            bottomPassChoose = ChooseSheetUtil.getBottomChoose(this, "找回密码方式",
                    new String[]{"手机号找回", "邮箱找回", "身份证找回"},
                    (ob, position) -> {
                        System.out.println(position);
                        switch (position) {
                            case 1:
                                Bundle bundle = new Bundle();
                                bundle.putInt(Config.MODE, Config.MODE_PASS_FORGET);
                                ARouter.getInstance().build(Config.LOGIN_REGISTER)
                                        .with(bundle).navigation();
                                break;
                            case 2:
                                ARouter.getInstance().build(Config.LOGIN_EMAIL_CHECK)
                                        .navigation();
                                break;
                        }
                    }
            );
        }
        bottomPassChoose.show();
    }

    private void showChoose() {
        if (bottomChoose == null) {
            bottomChoose = ChooseSheetUtil.getBottomChoose(this, "游客不会记录信息", new String[]{"游客", "实名认证"},
                    (ob, position) -> {
                        System.out.println(position);
                        if (position == 1) {
                            enterMainActivity();
                        } else if (position == 2) {
                            ARouter.getInstance().build(Uri.parse(Config.APP_BASE_URL + Config.MINE_AU))
                                    .navigation(LoginActivity.this, Config.MINE_AU);
                        }
                    }
            );
        }
        bottomChoose.show();
    }

    private void sendRequest(final String username, final String pass) {
        HashMap<String, String> map = new HashMap<>();
        map.put("u1", username);
        map.put("u2", EncryptUtil.sha1(pass + "EQD"));
        map.put("uid", username);
        new HttpPresneter.Builder()
                .Url(HttpConfig.BASE_URL + HttpConfig.LOGIN)
                .showProgress(this)
                .Params(map)
                .setOnHttpListener(new HttpPresneter.OnHttpListener<String>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort("服务器异常");
                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            int status = new JSONObject(s).getInt("status");
                            String msg = new JSONObject(s).getString("msg");
                            if (status == 200) {
                                BaseBean<User> bean = GsonUtils.changeGsonToBaseBean(s, User.class);
                                userService.insert(bean.getItems().get(0));
                                SPUtil.setParam(Config.PASS, pass);
                                if (bean.getItems() != null && bean.getItems().get(0).getAuthen() == 0) {
                                    showChoose();
                                } else {
                                    enterMainActivity();
                                }
                            } else {
                                ToastUtil.showShort(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort("服务器异常");
                        }
                    }
                })
                .build()
                .postString();
    }

    private void enterMainActivity() {
        user = userService.getUser();
        jPushService.setJPushAlias();
        rongyunService.connect();
        ARouter.getInstance().build(Config.APP_MAIN)
                .navigation();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.MINE_AU://个人认证
                if (resultCode == Config.SUCCESS) {//个人认证成功
                    enterMainActivity();
                }
                break;
        }
    }
}
