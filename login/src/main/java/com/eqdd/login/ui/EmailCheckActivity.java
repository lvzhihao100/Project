package com.eqdd.login.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.eqdd.login.EmailCheckActivityCustom;
import com.eqdd.login.R;
import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.StaticScheme;
import com.github.yoojia.inputs.WidgetAccess;
@Route(path = Config.LOGIN_EMAIL_CHECK)
public class EmailCheckActivity extends CommonActivity {
    private EmailCheckActivityCustom dataBinding;
    private AndroidNextInputs inputs;
    private WidgetAccess access;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_email_check);
        initTopTitleBar(View.VISIBLE, "邮箱找回密码");
    }

    @Override
    public void initData() {
        inputs = new AndroidNextInputs();
        access = new WidgetAccess(this);
        inputs  // 必选，手机号
                .add(access.findEditText(R.id.et_phone))
                .with(StaticScheme.Required(), StaticScheme.ChineseMobile())
                // 必选 ，密码8~18
                .add(access.findEditText(R.id.et_email))
                .with(StaticScheme.Required(), StaticScheme.Email());
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_submit) {
            if (inputs.test()) {
                new HttpPresneter.Builder()
                        .Url(HttpConfig.USER_EMAIL_CHECK)
                        .showProgress(EmailCheckActivity.this)
                        .Params("temail", dataBinding.etEmail.getText().toString())
                        .Params("aname", dataBinding.etPhone.getText().toString())
                        .setOnHttpListener(new HttpPresneter.OnHttpListener<String>() {
                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {

                                BaseBean baseBean = GsonUtils.changeGsonToBean(s, BaseBean.class);
                                ToastUtil.showShort(baseBean.getMsg());
                                if (baseBean.getStatus() == 200) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phone", dataBinding.etPhone.getText().toString().trim());
                                    ARouter.getInstance().build(Config.LOGIN_EMAIL_PASSWORD_GET)
                                            .with(bundle)
                                            .navigation();
                                    finish();
                                }

                            }
                        })
                        .build()
                        .postString();
            }
        }
    }
}
